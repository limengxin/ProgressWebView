package com.lmx.progresswebview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lmx.progressweb.ProgressWebView;


public class MainActivity extends AppCompatActivity {
    ProgressBar mProgressBar;
    WebView mWebView;
    ProgressWebView progressWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        初始化
        progressWebView=new ProgressWebView();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mWebView = (WebView) findViewById(R.id.webView);

        // 支持js
        mWebView.getSettings().setJavaScriptEnabled(true);

        // 加载url
        String url = "https://h5.m.taobao.com/";
        mWebView.loadUrl(url);

        // 拦截url
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("=====","===显示==");
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setAlpha(1.0f);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String url = request.getUrl().toString();
                    Log.e("======", "=====" + request.toString());
                    if(url == null) return false;
                    try {
                        if(url.startsWith("taobao://")//其他自定义的scheme
                                ) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            return true;
                        }
                    } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                        return false;
                    }
                    //处理http和https开头的url
                    mWebView.loadUrl(url);
                    return true;
                }else {
                    mWebView.loadUrl(request.toString());
                }
                return true;
            }

        });

        // 获取网页加载进度
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressWebView.setCurrentProgress(mProgressBar.getProgress());
                if (newProgress >= 100 && !progressWebView.getAnimStart()) {
                    // 防止调用多次动画
                    progressWebView.setAnimStart(true);
                    mProgressBar.setProgress(newProgress);
                    // 开启属性动画让进度条平滑消失
                    progressWebView.startDismissAnimation(mProgressBar.getProgress(),mProgressBar);
                } else {
                    // 开启属性动画让进度条平滑递增
                    progressWebView.startProgressAnimation(newProgress,mProgressBar);
                }
            }
        });

    }


    /**
     * 监听back键
     * 在WebView中回退导航
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {  // 返回键的KEYCODE
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;  // 拦截
            } else {
                return super.onKeyDown(keyCode, event);   //  放行
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
