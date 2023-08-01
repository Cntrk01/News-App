package com.news.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.news.newsapp.R
import com.news.newsapp.adapters.NewsAdapter
import com.news.newsapp.data.Article
import com.news.newsapp.databinding.FragmentBreakingNewsBinding
import com.news.newsapp.databinding.FragmentSavedNewsBinding
import com.news.newsapp.viewmodel.SavedViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@AndroidEntryPoint
class SavedNewsFragment : Fragment() {

    private var _binding : FragmentSavedNewsBinding?=null
    private val binding get() = _binding!!
    private val viewModel: SavedViewModel by viewModels()
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMethod()
        observeArticle()
        setOnClick()
        itemTouchHelper()
    }

    private fun itemTouchHelper(){
        val itemTouchHelper=object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                val article=adapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(requireView(),"Article Delete Successfully", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding.recyclerView)
        }
    }

    private fun setOnClick() {
        adapter.setOnItemClickCallBack(object : NewsAdapter.OnItemClickListener {
            override fun article(article: Article?) {
                if (article != null && article.isValid()) {
                    val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
                    findNavController().navigate(action)
                } else {
                    // Handle the case when the article or its properties are null or empty
                    Toast.makeText(requireContext(), "Article is invalid.", Toast.LENGTH_SHORT).show()
                    // Or display a snackbar, a dialog, or perform any other action you want for error handling.
                }
            }
        })
    }

    private fun initMethod(){
        adapter=NewsAdapter()
        //bunu unuttuğum için ekranda veri gelmedi
        binding.recyclerView.adapter=adapter
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
    }

    private fun observeArticle(){
        viewModel.getArticle().observe(viewLifecycleOwner, Observer {
            adapter.differ.submitList(it)
        })
    }


}