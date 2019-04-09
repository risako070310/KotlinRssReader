package com.risakokato.rssreader

import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun httpGet(url: String): InputStream? {

    val con = URL(url).openConnection() as HttpsURLConnection

    con.apply{
        requestMethod = "GET"
        connectTimeout = 3000
        readTimeout = 5000
        instanceFollowRedirects = true
    }
    con.connect()

    if(con.responseCode in 200..299){
        return BufferedInputStream(con.inputStream)
    }

    return null
}