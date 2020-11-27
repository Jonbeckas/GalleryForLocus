package net.tetraowl.galleryinlocus.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import net.tetraowl.galleryinlocus.R


class OpenSourceLicences : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_source_licences)
        val webview = findViewById<View>(R.id.web) as WebView
        webview.settings.javaScriptEnabled = true
        webview.loadUrl("file:///android_asset/open_source_licenses.html")
    }
}
