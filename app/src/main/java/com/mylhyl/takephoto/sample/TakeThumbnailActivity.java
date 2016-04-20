package com.mylhyl.takephoto.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.mylhyl.takephoto.TakePhotoManager;
import com.mylhyl.takephoto.TakePhotoOptions;
import com.mylhyl.takephoto.TakePhotoResult;

import java.io.File;

public class TakeThumbnailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, TakeFragment.newInstance())
                    .commitAllowingStateLoss();
    }

    public static class TakeFragment extends Fragment implements View.OnClickListener {

        public static TakeFragment newInstance() {
            return new TakeFragment();
        }

        private ImageView iv;
        private ImageView iv1;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_take, container, false);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getView().findViewById(R.id.button3).setOnClickListener(this);
            iv = (ImageView) getView().findViewById(R.id.imageView2);
            iv1 = (ImageView) getView().findViewById(R.id.imageView3);
            TakePhotoManager.getInstance().createForResult(savedInstanceState, new TakePhotoResult() {
                @Override
                public void onFailure(String message) {
                    Toast.makeText(TakeFragment.this.getContext(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResultFile(File originalFile, File compressedFile, File thumbnailFile) {
                    iv.setImageBitmap(BitmapFactory.decodeFile(compressedFile.getAbsolutePath()));
                    iv1.setImageBitmap(BitmapFactory.decodeFile(thumbnailFile.getAbsolutePath()));
                }
            });
        }

        @Override
        public void onClick(View v) {
            TakePhotoManager.getInstance().request(this,
                    new TakePhotoOptions.Builder().setThumbnailSize().build());
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            TakePhotoManager.getInstance().activityResult(requestCode, resultCode, data);
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            TakePhotoManager.getInstance().saveInstanceState(outState);
            super.onSaveInstanceState(outState);
        }
    }

    public static void gotoActivity(Activity activity) {
        activity.startActivity(new Intent(activity, TakeThumbnailActivity.class));
    }
}
