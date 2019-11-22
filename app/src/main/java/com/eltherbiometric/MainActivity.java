package com.eltherbiometric;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eltherbiometric.ui.login.LoginActivity;
import com.eltherbiometric.ui.ocr.OcrActivity;
import com.eltherbiometric.ui.presence.FaceRecognitionActivity;
import com.eltherbiometric.ui.upload.UploadActivity;
import com.eltherbiometric.utils.Config;
import com.orhanobut.hawk.Hawk;

public class MainActivity extends AppCompatActivity {

    private EditText etSetting;
    private Button btnRegistration, btnFaceRecognition, btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hawk.init(MainActivity.this).build();

        initComponents();
        initEvents();
    }

    private void initComponents() {
        btnRegistration = findViewById(R.id.btnRegistration);
        btnFaceRecognition = findViewById(R.id.btnFaceRecognition);
        btnUpload = findViewById(R.id.btnUpload);
    }

    private void initEvents() {
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OcrActivity.class);
                startActivity(intent);
            }
        });

        btnFaceRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FaceRecognitionActivity.class);
                startActivity(intent);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            case R.id.setting:
                showSettingDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettingDialog(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_setting, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(view);

        etSetting = view.findViewById(R.id.txtSetting);
        etSetting.requestFocus();
        if (Hawk.get(Config.IpAddress) != null) etSetting.setText(Hawk.get(Config.IpAddress).toString());

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String IpAddress = etSetting.getText().toString();
                        Hawk.put(Config.IpAddress, IpAddress);
                        Toast.makeText(MainActivity.this, "IP Address changed to : " + Hawk.get(Config.IpAddress), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.dismiss();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
    }
}
