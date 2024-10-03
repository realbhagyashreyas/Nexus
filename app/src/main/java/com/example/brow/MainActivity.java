package com.example.brow;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private EditText urlEditText;
    ImageView youtubeLogo, googleLogo, instagramLogo, xLogo, clearUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlEditText = findViewById(R.id.urlEditText);
        googleLogo = findViewById(R.id.googlelogo);
        youtubeLogo = findViewById(R.id.youtubelogo);
        instagramLogo = findViewById(R.id.instagramlogo);
        xLogo = findViewById(R.id.xlogo);
        clearUrl = findViewById(R.id.clearurl);
//        youtubeLinkEditText = findViewById(R.id.youtubeLinkEditText);

        // Handle Enter key press in the EditText
        urlEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String url = urlEditText.getText().toString().trim();

                    if (!url.isEmpty()) {
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "https://www.google.com/search?q=" + url;
                        }
                        openWebView(url);
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter a URL or search query.", Toast.LENGTH_SHORT).show();
                    }
                    return true; // Consume the event
                }
                return false;
            }
        });



        clearUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlEditText.setText("");
            }
        });

        googleLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView("https://www.google.com");
            }
        });

        youtubeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView("https://www.youtube.com");
            }
        });

        instagramLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView("https://www.instagram.com");
            }
        });

        xLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView("https://www.x.com");
            }
        });
    }

    private void openWebView(String url) {
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }


}
