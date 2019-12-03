package com.eltherbiometric;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eltherbiometric.ui.facerecog.TinyDB;
import com.eltherbiometric.ui.presence.FingerPrintActivity;
import com.eltherbiometric.ui.login.LoginActivity;
import com.eltherbiometric.ui.ocr.OcrActivity;
import com.eltherbiometric.ui.presence.FaceRecognitionActivity;
import com.eltherbiometric.ui.upload.UploadActivity;
import com.eltherbiometric.utils.AndroidDatabaseManager;
import com.eltherbiometric.utils.Config;
import com.orhanobut.hawk.Hawk;

import org.opencv.core.Mat;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private EditText etSetting;
    private CardView btnRegistration, btnPresensi, btnUpload;
    private CardView btnFaceRecognition, btnFingerPrintRecognition;
    private TextView tvVersion;
    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private View dialogView;
    private String insert;
    private TinyDB tinydb;
    private Mat imageMat;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
//                    cameraView.enableView();
//                    cameraView.setOnTouchListener(FingerPrintActivity.this);
                    imageMat = new Mat();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hawk.init(MainActivity.this).build();

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        }
        else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        // processed images
        tinydb = new TinyDB(this); // Used to store ArrayLists in the shared preferences
        if(Config.processedImages == null) Config.processedImages = new HashMap<String, Mat>();
        List<String> name_list = Hawk.get("name_list");
        if (name_list != null) {
            if (name_list.size() > 0){
                Log.d(TAG, "name List: " + name_list.size());
                for (String name: name_list) {
                    imageMat = tinydb.matFromJson(name);
                    Log.d(TAG, "initialize Image Mat " + name + " : " + imageMat);
                    Config.processedImages.put(name, imageMat);
                }
            }
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                insert = null;
            } else {
                insert = extras.getString("insert");
                if (insert.equals("success")){
                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Registrasi Sukses")
                            .show();
                }
            }
        } else {
            insert = (String) savedInstanceState.getSerializable("insert");
        }

        initComponents();
        initEvents();
    }

    private void initDialog() {
        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.presence_dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        btnFaceRecognition = dialogView.findViewById(R.id.btnFaceRecognition);
        btnFingerPrintRecognition = dialogView.findViewById(R.id.btnFingerPrintRecognition);

        btnFaceRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FaceRecognitionActivity.class);
                startActivity(intent);
            }
        });

        btnFingerPrintRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FingerPrintActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }


    private void initComponents() {
        btnRegistration = findViewById(R.id.btnRegistration);
        btnPresensi = findViewById(R.id.btnPresensi);
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

        btnPresensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
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
            case R.id.db_manager:
                Intent intent1 = new Intent(MainActivity.this, AndroidDatabaseManager.class);
                startActivity(intent1);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
}
