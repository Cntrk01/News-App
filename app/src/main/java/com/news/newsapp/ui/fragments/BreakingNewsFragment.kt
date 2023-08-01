package com.news.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.news.newsapp.adapters.NewsAdapter
import com.news.newsapp.data.Article
import com.news.newsapp.databinding.FragmentBreakingNewsBinding
import com.news.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(), NewsAdapter.OnItemClickListener {
    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()

    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMethod()
        observeMethod()
        setOnClick()
    }

    private fun initMethod() {
        adapter = NewsAdapter()
        //bunu unuttuğum için ekranda veri gelmedi
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addOnScrollListener(scrollListener)
    }

    private fun setOnClick() {
        adapter.setOnItemClickCallBack(object : NewsAdapter.OnItemClickListener {
            override fun article(article: Article?) {
                if (article != null && article.isValid()) {
                    val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article)
                    findNavController().navigate(action)
                } else {
                    // Handle the case when the article or its properties are null or empty
                    Toast.makeText(requireContext(), "Article is invalid.", Toast.LENGTH_SHORT).show()
                    // Or display a snackbar, a dialog, or perform any other action you want for error handling.
                }
            }
        })
    }


    override fun article(article: Article?) {
        article?.let {
            val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article)
            findNavController().navigate(action)
        }
    }

    private fun observeMethod() {
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resources.Success -> {
                    hideProgressBar()
                    it.data?.let { newResponse ->
                        //mutable list yaptık data classı ondan toList ekledik
                        adapter.differ.submitList(newResponse.articles.toList())
                        val totalPage=newResponse.totalResults/20+2
                        isLastPage=viewModel.breakingPage==totalPage
                        if (isLastPage){
                            binding.recyclerView.setPadding(0,0,0,0)
                        }
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

    var isLoading=false
    var isLastPage=false
    var isScrolling=false

    private val scrollListener=object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState ==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstItemVisibleItemPos=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndLastPage=!isLoading && !isLastPage
            val isAtLastItem=firstItemVisibleItemPos+visibleItemCount >= totalItemCount
            val isNotAtBegining=firstItemVisibleItemPos >=0
            val isTotalMoreThanVisibility=totalItemCount >= 20

            val shouldPaginet=isNotLoadingAndLastPage && isAtLastItem && isNotAtBegining && isTotalMoreThanVisibility && isScrolling

            if (shouldPaginet){
                viewModel.getBreakingNews("us")
                isScrolling=false
            }else{

            }
        }
    }


    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        isLoading=false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading=true
    }



}