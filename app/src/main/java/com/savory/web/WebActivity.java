package com.savory.web;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.savory.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {

    private static final String TERMS_OF_USE = "terms_of_use";
    private static final String PRIVACY_POLICY = "privacy_policy";
    private static final String CONTENT_GUIDELINES = "content_guidelines";

    private static final String TERMS_OF_USE_URL =
            "https://github.com/jchio001/Savory/blob/master/Terms%20of%20Use.md";
    private static final String PRIVACY_POLICY_URL =
            "https://github.com/jchio001/Savory/blob/master/Privacy%20Policy.md";
    private static final String CONTENT_GUIDELINES_URL =
            "https://github.com/jchio001/Savory/blob/master/Content%20Guidelines.md";

    @BindView(R.id.web_view) WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()
                .setHomeAsUpIndicator(new IconDrawable(this, IoniconsIcons.ion_android_close)
                        .colorRes(R.color.white)
                        .actionBarSize());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        if (getIntent().getData() != null) {
            // Coming from here an implicit intent via clicking on a dish title
            String path = getIntent().getData().getPath();

            // Remove the / that starts the path
            String pageType = path.substring(1);
            setTitle(getTitleFromPageType(pageType));
            webView.loadUrl(getUrlFromPageType(pageType));
        }
    }

    private static @StringRes int getTitleFromPageType(String pageType) {
        switch (pageType) {
            case TERMS_OF_USE:
                return R.string.terms_of_use;
            case PRIVACY_POLICY:
                return R.string.privacy_policy;
            case CONTENT_GUIDELINES:
                return R.string.content_guidelines;
            default:
                throw new IllegalArgumentException("Web link type isn't supported");
        }
    }

    private static String getUrlFromPageType(String pageType) {
        switch (pageType) {
            case TERMS_OF_USE:
                return TERMS_OF_USE_URL;
            case PRIVACY_POLICY:
                return PRIVACY_POLICY_URL;
            case CONTENT_GUIDELINES:
                return CONTENT_GUIDELINES_URL;
            default:
                throw new IllegalArgumentException("Web link type isn't supported");
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
