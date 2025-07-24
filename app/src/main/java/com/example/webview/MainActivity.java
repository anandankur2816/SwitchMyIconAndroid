package com.example.webview;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String defaultIconAlias = "com.example.webview.DefaultIconAlias";
    private static final String cloneDefaultIconAlias = "com.example.webview.CloneDefaultIconAlias";
    private static final String APK_ICON_SELECTED_COMPONENT_NAME = "apk_icon_selected_component_name";
    public SharedPreferences prefs;


    private List<String> apkIconComponentNameEnabledList = new ArrayList<>(Arrays.asList("com.example.webview.DefaultIconAlias",
            "com.example.webview.PixelPopIconAlias"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Spinner iconNameInput = findViewById(R.id.icon_input);
        Button loadButton = findViewById(R.id.load_button);

        if(prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.icon_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iconNameInput.setAdapter(adapter);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newIconName = iconNameInput.getSelectedItem().toString();
                if(!newIconName.isBlank()){
                    int index = iconNameInput.getSelectedItemPosition();
                    Log.d("ApkIconUpdate", "NAmes: " +  newIconName + " comp: " + apkIconComponentNameEnabledList.get(index));
                    changeApkIcon(apkIconComponentNameEnabledList.get(index));
                }

            }
        });


    }

    private void changeApkIcon(String componentName) {
        String apkComponentName = prefs.getString(APK_ICON_SELECTED_COMPONENT_NAME, defaultIconAlias);

        Log.d("TAG: ", "changeApkIcon: " + apkComponentName + " " + componentName);
        if (apkComponentName.equals(componentName)) {
            return;
        }

        if (componentName.equals(defaultIconAlias)) {
            setComponentEnabled(apkComponentName, getApplicationContext(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            setComponentEnabled(cloneDefaultIconAlias, getApplicationContext(), PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
            prefs.edit().putString(APK_ICON_SELECTED_COMPONENT_NAME, cloneDefaultIconAlias).apply();
        } else {
            setComponentEnabled(apkComponentName, getApplicationContext(), apkComponentName.contains(defaultIconAlias) ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            setComponentEnabled(componentName, getApplicationContext(), PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
            prefs.edit().putString(APK_ICON_SELECTED_COMPONENT_NAME, componentName).apply();
        }
    }

    private void setComponentEnabled(String componentName, Context context, int state){
        PackageManager manager = getPackageManager();
        manager.setComponentEnabledSetting(new ComponentName(context, componentName), state, PackageManager.DONT_KILL_APP);
    }


}
