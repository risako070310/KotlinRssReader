package com.risakokato.rssreader

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ArticlesAdapter (private val context: Context, private val articles: List<Article>, private val onArticleClicked: (Article) -> Unit) : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.title)
        val pubDate = view.findViewById<TextView>(R.id.pubDate)

    }
    override fun getItemCount() = articles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {

        val view = inflater.inflate(R.layout.grid_article_cell, parent, false)

        val viewHolder = ArticleViewHolder(view)

        view.setOnClickListener{

            val position = viewHolder.adapterPosition

            val article = articles[position]

            onArticleClicked(article)
        }

        return viewHolder

    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        val article = articles[position]

        holder.title.text = article.title

        holder.pubDate.text = context.getString(R.string.pubDate, article.pubDate)

    }
}