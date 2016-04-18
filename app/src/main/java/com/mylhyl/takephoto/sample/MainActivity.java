package com.mylhyl.takephoto.sample;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mylhyl.takephoto.TakePhotoManager;
import com.mylhyl.takephoto.TakePhotoOptions;
import com.mylhyl.takephoto.TakePhotoResult;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TakePhotoManager.getInstance().notifyTakePhotoChange(requestCode,resultCode,data);
    }

    @Override
    public void onClick(View v) {
        TakePhotoManager.getInstance().requestTakePhotoForResult(this, new TakePhotoOptions.Builder().build(), new TakePhotoResult() {
            @Override
            public void onFailure(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResultFile(File originalFile, File compressedFile) {
                iv.setImageBitmap(BitmapFactory.decodeFile(compressedFile.getAbsolutePath()));
            }
        });
    }
}
