package com.pk.smartphotoeditor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {
AppCompatButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(AppCompatButton) findViewById(R.id.selectbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

    }
    private  void checkPermission(){
        int permission= ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)
        {
            picImage();
        }else{
            if (permission!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },100);
            }
            else {
                picImage();
            }

        }
    }
    private void picImage(){
            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            picImage();
        }else {
            Toast.makeText(this, "Permision denied", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri=data.getData();
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case 100:
                    Intent intent=new Intent(MainActivity.this, DsPhotoEditorActivity.class);
                    intent.setData(uri);
                    intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,"images");
                    intent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR,getResources().getColor(R.color.purple_500));
                    intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,getResources().getColor(R.color.purple_500));
                    intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,new int[]{DsPhotoEditorActivity.TOOL_WARMTH,
                    DsPhotoEditorActivity.TOOL_PIXELATE});
                    startActivityForResult(intent, 101);
                    break;
                case 101:
                    Toast.makeText(this, "image saved..", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }
}