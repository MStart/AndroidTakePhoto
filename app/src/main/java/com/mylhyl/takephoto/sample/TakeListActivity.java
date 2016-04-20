package com.mylhyl.takephoto.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mylhyl.takephoto.TakePhotoManager;
import com.mylhyl.takephoto.TakePhotoOptions;
import com.mylhyl.takephoto.TakePhotoResult;
import com.mylhyl.takephoto.TakePhotoUtil;

import java.io.File;

public class TakeListActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take);
        findViewById(R.id.button).setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.imageView);
        TakePhotoManager.getInstance().createForResult(savedInstanceState, new TakePhotoResult() {
            @Override
            public void onFailure(String message) {
                Toast.makeText(TakeListActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResultFile(final File compressedFile) {
                Bitmap bitmap = BitmapFactory.decodeFile(compressedFile.getAbsolutePath());
                iv.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TakePhotoManager.getInstance().activityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        File takePhotoDir = TakePhotoUtil.getCacheDir("takePhoto", this);
        File takePhotoFile = new File(takePhotoDir, "takePhoto.jpg");
        TakePhotoManager.getInstance().request(this, new TakePhotoOptions.Builder()
                .setTakePhotoDir(takePhotoDir)
                .setTakePhotoFile(takePhotoFile)
                .build());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        TakePhotoManager.getInstance().saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        Log.e("onDestroy", "onDestroy");
        super.onDestroy();
    }

    public static void gotoActivity(Activity activity) {
        activity.startActivity(new Intent(activity, TakeListActivity.class));
    }
}
