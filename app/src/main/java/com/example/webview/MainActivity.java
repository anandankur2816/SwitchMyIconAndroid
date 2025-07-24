package com.example.webview;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        WebView webView = findViewById(R.id.webview);
        // Find the EditText and Button in your layout
        EditText urlInput = findViewById(R.id.url_input);
        Button loadButton = findViewById(R.id.load_button);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setAllowContentAccess(true);
//        webSettings.setAllowFileAccess(true);
        String userAgent = "Mozilla/5.0 (Linux; Android 10; Pixel 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Mobile Safari/537.36";
//        String userAgent = "Mozilla/5.0 (Linux; Android 13; RMX3360 Build/TP1A.220905.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/126.0.6478.134 Mobile Safari/537.36";
        webSettings.setUserAgentString(userAgent);

        Button javascriptEnableSettingsButton = findViewById(R.id.javascript_enable_settings_button);
        javascriptEnableSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEnabled = !webSettings.getJavaScriptEnabled(); // Toggle the setting
                webSettings.setJavaScriptEnabled(isEnabled);
                updateButtonColor(isEnabled, javascriptEnableSettingsButton.getId());
            }
        });

        Button domEnableSettingsButton = findViewById(R.id.local_storage_enable_settings_button);
        domEnableSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEnabled = !webSettings.getDomStorageEnabled(); // Toggle the setting
                Log.d("Permission", "Inside this permission not granted"+ webSettings.getUserAgentString());
                webSettings.setDomStorageEnabled(isEnabled);
                updateButtonColor(isEnabled, domEnableSettingsButton.getId());
            }
        });

        Button userAgentEnableSettingsButton = findViewById(R.id.user_agent_enable_settings_button);
        userAgentEnableSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEnabled = webSettings.getUserAgentString().equals(userAgent); // Toggle the setting
                Log.d("User_Agent", webSettings.getUserAgentString()+ "is enabled: "+ isEnabled);
                webSettings.setUserAgentString(!isEnabled ? userAgent: null);
                Log.d("User_Agent", webSettings.getUserAgentString());
                updateButtonColor(!isEnabled, userAgentEnableSettingsButton.getId());
            }
        });

        Button contentAccessEnableSettingsButton = findViewById(R.id.content_access_enable_settings_button);
        contentAccessEnableSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEnabled = !webSettings.getAllowContentAccess(); // Toggle the setting
                webSettings.setAllowContentAccess(isEnabled);
                updateButtonColor(isEnabled, contentAccessEnableSettingsButton.getId());
            }
        });

        Button fileAccessEnableSettingsButton = findViewById(R.id.file_access_enable_settings_button);
        fileAccessEnableSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEnabled = !webSettings.getAllowFileAccess(); // Toggle the setting
                webSettings.setAllowFileAccess(isEnabled);
                updateButtonColor(isEnabled, fileAccessEnableSettingsButton.getId());
            }
        });

        Button cameraAccessButton = findViewById(R.id.allow_camera_access);
        cameraAccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndEnableCameraPermission();
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                urlInput.setVisibility(View.GONE);
                loadButton.setVisibility(View.GONE);
                javascriptEnableSettingsButton.setVisibility(View.GONE);
                domEnableSettingsButton.setVisibility(View.GONE);
                userAgentEnableSettingsButton.setVisibility(View.GONE);
//                contentAccessEnableSettingsButton.setVisibility(View.GONE);
                cameraAccessButton.setVisibility(View.GONE);
//                fileAccessEnableSettingsButton.setVisibility(View.GONE);
                }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                String[] requestedPermissions = request.getResources();

                for (String permission : requestedPermissions) {
                    Log.d("Permission", "Inside this permission not granted"+ permission);
                    if (permission.equals(android.Manifest.permission.CAMERA) || permission.equals("android.webkit.resource.VIDEO_CAPTURE")) {
                        if(checkCameraPermission()==false){
                            enableCameraPermission();
                            webView.reload();
                        }
                    }
                }
                request.grant(request.getResources());
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlInput.getText().toString();
                if (!url.isEmpty()) {
                    webView.loadUrl(url);
                }
            }
        });

        // Apply insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "please click on leave this page or go back", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }
    }
    private void updateButtonColor(boolean isEnabled, int buttonId) {
        Button button = findViewById(buttonId);
        int buttonColor = isEnabled ?
                ContextCompat.getColor(this, R.color.button_enabled_color) :
                ContextCompat.getColor(this, R.color.button_disabled_color);
        button.setBackgroundColor(buttonColor);
    }

    private boolean checkCameraPermission(){
        return ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ? false: true;
    }
    private void enableCameraPermission(){
            Log.d("CameraPermission", "Inside this permission not granted");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
    }

    private void checkAndEnableCameraPermission(){
        Log.d("CameraPermission", "Inside this permission not granted");
        if(checkCameraPermission() == false){
            enableCameraPermission();
        }
    }


}
