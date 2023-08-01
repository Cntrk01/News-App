package com.news.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.news.newsapp.data.Article
import com.news.newsapp.databinding.FragmentArticleBinding
import com.news.newsapp.viewmodel.SavedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private var _binding : FragmentArticleBinding?=null
    private val binding get() = _binding!!
    private val args:ArticleFragmentArgs by navArgs()
    private val viewModel: SavedViewModel by viewModels()

    var isChechked=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article=args.article
        webViewShow(article)
        binding.floatingActionButton.setOnClickListener {
            saveArticle(article)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }
    private fun webViewShow(article:Article){
        binding.webView.apply {
            webViewClient= WebViewClient()
            article.url?.let { loadUrl(it) }
        }
    }

    //Eklemeyle alakalı problem yaşıyordum.Nasıl çözdüm ifade edeyim.SavedViewmodel içinde return etmem gerkiyormuş.
    //Yani Dao içinde Int tanımlı diye int geliyor ama değer gelmiyordu.Bundan dolayı isArticleExist methodunu return ettim CHATGPT sayesinde çözdüm.
    private fun saveArticle(article: Article) {
        CoroutineScope(Dispatchers.IO).launch{
            //viewmodelde viewmodelscope.launch yaptığım için job donduruyordu onu sildim burda Scope açtım ve değer artık int dönüyor
            val d=viewModel.isArticleExist(article.url!!)
            withContext(Dispatchers.Main){
                if (d>0){
                    Snackbar.make(requireView(),"Already Been In Database",Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.saveArticle(article)
                    Snackbar.make(requireView(),"Saved In Database",Toast.LENGTH_SHORT).show()
                }
            }
        }



    }

}