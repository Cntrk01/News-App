package com.news.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.news.newsapp.R
import com.news.newsapp.adapters.NewsAdapter
import com.news.newsapp.data.Article
import com.news.newsapp.data.NewsResponse
import com.news.newsapp.databinding.FragmentBreakingNewsBinding
import com.news.newsapp.databinding.FragmentSearchNewsBinding
import com.news.newsapp.viewmodel.NewsViewModel
import com.news.newsapp.viewmodel.SavedViewModel
import com.news.newsapp.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class SearchNewsFragment : Fragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var adapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMethod()
        observeMethod()
        searchText()
    }

    private fun initMethod() {
        adapter = NewsAdapter()
        //bunu unuttuğum için ekranda veri gelmedi
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerView.addOnScrollListener(scrollListener)
    }

    private fun observeMethod() {
        viewModel.searchNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resources.Success -> {
                    hideProgressBar()
                    it.data?.let {newsResponse->
                        adapter.differ.submitList(newsResponse.articles.toList())
//                        val totalPage=newsResponse.totalResults/20+2
//                        isLastPage=viewModel.searchNewsPage==totalPage
//                        if (isLastPage){
//                            binding.recyclerView.setPadding(0,0,0,0)
//                        }
                    }

                }
                is Resources.Error -> {
                    hideProgressBar()
                    it.message?.let { error ->
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resources.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

//    private fun setItemClickListener(){
//        adapter.setOnItemClickListener1 {
//            val nav=BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it)
//            findNavController().navigate(nav)
//        }
//    }
    private fun searchText(){
        var job : Job ?=null
        binding.editText.addTextChangedListener {editable->
            job?.cancel()
            job= MainScope().launch {
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
    }

    var isLoading=false
//    var isLastPage=false
//    var isScrolling=false
//
//    private val scrollListener=object : RecyclerView.OnScrollListener(){
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                isScrolling=true
//            }
//        }
//
//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            super.onScrolled(recyclerView, dx, dy)
//            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
//            val firstItemVisibleItemPos=layoutManager.findFirstVisibleItemPosition()
//            val visibleItemCount=layoutManager.childCount
//            val totalItemCount=layoutManager.itemCount
//
//            val isNotLoadingAndLastPage=!isLoading && !isLastPage
//            val isAtLastItem=firstItemVisibleItemPos+visibleItemCount >= totalItemCount
//            val isNotAtBegining=firstItemVisibleItemPos >=0
//            val isTotalMoreThanVisibility=totalItemCount >= 20
//
//            val shouldPaginet=isNotLoadingAndLastPage && isAtLastItem && isNotAtBegining && isTotalMoreThanVisibility && isScrolling
//
//            if (shouldPaginet){
//                viewModel.searchNews(binding.editText.toString())
//                isScrolling=false
//            }else{
//                binding.recyclerView.setPadding(0,0,0,0)
//            }
//        }
//    }
//
//
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        isLoading=false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading=true
    }

}