package com.example.brow;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    ImageView webBack, webForward, webRefresh, webShare;
    private ProgressBar progressBar;
    private Spinner fontFamilySpinner, fontStyleSpinner;
    private Button resetButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        webBack = findViewById(R.id.web_back);
        webForward = findViewById(R.id.web_forward);
        webRefresh = findViewById(R.id.web_refresh);
        webShare = findViewById(R.id.web_share);
        progressBar = findViewById(R.id.progress_bar);
        fontFamilySpinner = findViewById(R.id.fontFamily);
        fontStyleSpinner = findViewById(R.id.fontStyle);
        resetButton = findViewById(R.id.resetFontButton);

        webBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        webForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }
        });

        webRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });

        webShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                intent.setType("text/plain");
                startActivity(intent);
            }
        });


        webView = findViewById(R.id.webView);
        //String url = getIntent().getStringExtra("url");


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient());


        //webView.loadUrl(url);

        handleIntent(getIntent());


        ArrayAdapter<CharSequence> fontFamilyAdapter = ArrayAdapter.createFromResource(this,
                R.array.font_family_options, android.R.layout.simple_spinner_item);
        fontFamilyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontFamilySpinner.setAdapter(fontFamilyAdapter);


        ArrayAdapter<CharSequence> fontStyleAdapter = ArrayAdapter.createFromResource(this,
                R.array.font_style_options, android.R.layout.simple_spinner_item);
        fontStyleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontStyleSpinner.setAdapter(fontStyleAdapter);


        fontFamilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateFont();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        fontStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateFont();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                injectJavaScriptToResetFont();
            }
        });




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    updateScrollProgress();
                }


            });

        }
    }

    private void handleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        if (url == null) {
            // Check if the intent has a data URI (for deep links or external intents)
            url = intent.getDataString();
        }

        // If a valid URL is provided, load it in the WebView
        if (url != null && !url.isEmpty()) {
            webView.loadUrl(url);
        }
    }

    private void updateScrollProgress() {
        int webContentHeight = (int) Math.floor(webView.getContentHeight() * webView.getScale());
        int visibleHeight = webView.getHeight();

        // Calculate the progress as a percentage
        int scrollY = webView.getScrollY();
        int progress = (int) ((float) scrollY / (webContentHeight - visibleHeight) * 100);


        progressBar.setProgress(progress);
    }

    private void updateFont() {
        String fontFamily = fontFamilySpinner.getSelectedItem().toString();
        String fontStyle = fontStyleSpinner.getSelectedItem().toString();


        String cssFontWeight = "400";
        String cssFontStyle = "normal";
        switch (fontStyle) {
            case "Bold":
                cssFontWeight = "700";
                break;
            case "Italic":
                cssFontStyle = "italic";
                break;
            case "Bold Italic":
                cssFontWeight = "700";
                cssFontStyle = "italic";
                break;
        }


        String jsCode = "document.fonts.ready.then(function() {" +
                "const elements = document.querySelectorAll('body, body *');" +
                "elements.forEach(element => {" +
                "element.style.fontFamily = '" + fontFamily + "';" +
                "element.style.fontWeight = '" + cssFontWeight + "';" +
                "element.style.fontStyle = '" + cssFontStyle + "';" +
                "});" +
                "});";


        webView.post(() -> webView.evaluateJavascript(jsCode, null));
    }


    private void injectJavaScriptToResetFont() {
        String jsCode = "const elements = document.querySelectorAll('body, body *');" +
                "elements.forEach(element => {" +
                "element.style.fontFamily = '';" +
                "element.style.fontWeight = '';" +
                "element.style.fontStyle = '';" +
                "});";

        
        webView.post(() -> webView.evaluateJavascript(jsCode, null));
    }




}


