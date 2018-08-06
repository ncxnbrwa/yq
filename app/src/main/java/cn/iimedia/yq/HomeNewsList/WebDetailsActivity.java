package cn.iimedia.yq.HomeNewsList;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.EmptyUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import cn.iimedia.yq.Base.BaseActivity;
import cn.iimedia.yq.Base.Config;
import cn.iimedia.yq.Base.utils.DLog;
import cn.iimedia.yq.R;

public class WebDetailsActivity extends BaseActivity {
    public static final String TAG = "NewsDetailsActivity";

    @BindView(R.id.root_view)
    LinearLayout mRootView;
    @BindView(R.id.news_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.web_progress)
    ProgressBar progressBar;
    @BindView(R.id.tx_webview)
    WebView txWebView;

    String srcUrl;
    WebSettings webSet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取传值
        Bundle bundle = getIntent().getExtras();
        if (EmptyUtils.isNotEmpty(bundle.getString(Config.LOAD_URL))) {
            srcUrl = bundle.getString(Config.LOAD_URL);
            DLog.w(TAG, "加载url:" + srcUrl);
        }

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        webSet = txWebView.getSettings();
        //设置WebView参数
        webSet.setUseWideViewPort(true);
        webSet.setJavaScriptEnabled(true);
        //设置可以由JavaScript打开新窗口
        webSet.setJavaScriptCanOpenWindowsAutomatically(true);
        webSet.setGeolocationEnabled(true);
        webSet.setAllowContentAccess(true);
        //允许WebView访问文件数据
        webSet.setAllowFileAccess(true);
        //启用应用缓存
        webSet.setAppCacheEnabled(true);
        //启用DOM缓存
        webSet.setDomStorageEnabled(true);
        //启用数据库缓存
        webSet.setDatabaseEnabled(true);
        //缩放
        webSet.setSupportZoom(true);
        //设置编码
        webSet.setDefaultTextEncodingName("utf-8");
        webSet.setSaveFormData(true);

        //设置视频播放
        txWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
//                showToast("网络请求错误");
                DLog.w(Config.LOG_TAG, "错误信息:" + description);
                view.stopLoading();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        txWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i > 5) {
                    progressBar.setProgress(i);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbar.setTitle(title);
            }

        });
        txWebView.loadUrl(srcUrl);

    }

    @Override
    protected void onPause() {
        super.onPause();
        txWebView.onPause();
        if (txWebView.getX5WebViewExtension() != null) {
            txWebView.getX5WebViewExtension().deactive();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        txWebView.onResume();
        if (txWebView.getX5WebViewExtension() != null) {
            txWebView.getX5WebViewExtension().active();
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (txWebView.canGoBack()) {
            txWebView.goBack();
        } else {
            super.onBackPressedSupport();
        }
    }

    @Override
    protected void onDestroy() {
        if (txWebView != null) {
            txWebView.getSettings().setBuiltInZoomControls(true);
            txWebView.setVisibility(View.GONE);
            mRootView.removeView(txWebView);
            txWebView.destroy();
            txWebView = null;
        }
        super.onDestroy();
    }
}
