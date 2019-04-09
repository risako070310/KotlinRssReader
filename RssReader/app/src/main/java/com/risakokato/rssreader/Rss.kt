package com.risakokato.rssreader

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import org.w3c.dom.NodeList
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

data class Article(val title: String, val link: String, val pubDate: Date)

data class Rss(val title: String, val pubDate: Date, val articles: List<Article>)

fun parseRss(stream: InputStream): Rss{
    val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream)
    stream.close()

    val xPath = XPathFactory.newInstance().newXPath()

    val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)

    val items = xPath.evaluate("/res/channel//item", doc, XPathConstants.NODESET) as NodeList

    val articles = arrayListOf<Article>()

    for(i in 0 until items.length){
        val item = items.item(i)

        val article = Article(title = xPath.evaluate("./title/text()",item), link = xPath.evaluate("./link./text()",item), pubDate = formatter.parse(xPath.evaluate("./pubDate/text()",item)))

        articles.add(article)

    }

    return Rss(title =  xPath.evaluate("rss/channel/title/text()",doc),pubDate = formatter.parse(xPath.evaluate("/rss/channel/pubDate/text()",doc)), articles = articles)

}
class RssLoader(context: Context):AsyncTaskLoader<Rss>(context){

    private var cache : Rss? = null

    override fun loadInBackground(): Rss? {
        val response = httpGet("https://www.sbbit.jp/rss/HotTopics.rss")

        if(response != null){
            return parseRss(response)

        }

        return null

    }

    override fun deliverResult(data: Rss?) {
        if(isReset || data == null) return

        cache = data
        super.deliverResult(data)

    }

    override fun onStartLoading() {
        if(cache != null){
            deliverResult(cache)
        }

        if(takeContentChanged() || cache == null){
            forceLoad()
        }
    }

    override fun onStopLoading() {
        cancelLoad()

    }

    override fun onReset() {
        super.onReset()
        onStopLoading()
        cache = null
    }

}
