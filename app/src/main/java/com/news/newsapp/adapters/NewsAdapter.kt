package com.news.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.news.newsapp.data.Article
import com.news.newsapp.databinding.ItemArticleBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder( var binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root){}

    private val differCallBack=object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inf=ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArticleViewHolder(inf)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        Glide.with(holder.itemView).load(article?.urlToImage).into(holder.binding.ivArticleImage)
        holder.binding.tvSource.text = article?.source?.name ?: "Unknown Source"
        holder.binding.tvTitle.text = article?.title ?: "No Title"
        holder.binding.tvDescription.text = article?.description ?: "No Description"
        holder.binding.tvPublishedAt.text = article?.publishedAt ?: "Unknown Date"

        holder.itemView.rootView.setOnClickListener {
            onItemClickCallBack?.article(article)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickCallBack : OnItemClickListener?=null
    fun setOnItemClickCallBack(onItemClickListener: OnItemClickListener){
        this.onItemClickCallBack=onItemClickListener
    }


    interface OnItemClickListener{
        fun article(article: Article?)
    }

}