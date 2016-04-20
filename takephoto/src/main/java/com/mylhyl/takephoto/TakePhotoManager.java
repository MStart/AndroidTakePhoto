package com.mylhyl.takephoto;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * Created by hupei on 2016/4/14.
 */
public final class TakePhotoManager {
    private static final String KEY_INTERNAL_SAVED_VIEW_STATE = "internalSavedViewState";
    private static final String KEY_ORIGINAL_FILE = "mOriginalFile";
    private static final String KEY_COMPRESSED_FILE = "mCompressedFile";
    private static final String KEY_THUMBNAIL_FILE = "mThumbnailFile";
    public static final int REQUEST_CODE_TAKE_PHOTO = 0x38;
    private static TakePhotoManager mInstance = null;
    private File mTakePhotoDir;
    private String mOriginalFile;//图始图
    private TakePhotoOptions mTakePhotoOptions;
    private TakePhotoResult mCallBack;

    public static TakePhotoManager getInstance() {
        if (mInstance == null) {
            mInstance = new TakePhotoManager();
        }
        return mInstance;
    }

    private TakePhotoManager() {

    }

    private void initialize(Context context) {
        if (mTakePhotoDir == null)
            mTakePhotoDir = TakePhotoUtil.getCacheDir("pictures", context.getApplicationContext());
        if (mTakePhotoDir != null && mOriginalFile == null)
            mOriginalFile = new File(mTakePhotoDir, "takePhoto.jpg").getAbsolutePath();
    }

    /**
     * 启动相机拍照，只有压缩图（720 * 1280）
     *
     * @param activity
     * @return 返回照片文件
     */
    public void requestTakePhotoForResult(Activity activity) {
        requestTakePhotoForResult(activity, TakePhotoOptions.DEFAULT);
    }

    /**
     * 启动相机拍照
     *
     * @param activity
     * @param options  图片参数
     */
    public void requestTakePhotoForResult(Activity activity, TakePhotoOptions options) {
        initialize(activity);
        if (checkParams(options)) return;
        try {
            activity.startActivityForResult(getIntent(), REQUEST_CODE_TAKE_PHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            mCallBack.onFailure(e.getMessage());
        }
    }

    /**
     * 启动相机拍照，只有压缩图（720 * 1280）
     *
     * @param fragment
     * @return 返回照片文件
     */
    public void requestTakePhotoForResult(Fragment fragment) {
        requestTakePhotoForResult(fragment, TakePhotoOptions.DEFAULT);
    }

    /**
     * 启动相机拍照
     *
     * @param options  图片参数
     * @param fragment
     */
    public void requestTakePhotoForResult(Fragment fragment, TakePhotoOptions options) {
        initialize(fragment.getContext());
        if (checkParams(options)) return;
        try {
            fragment.startActivityForResult(getIntent(), REQUEST_CODE_TAKE_PHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            mCallBack.onFailure(e.getMessage());
        }
    }

    private boolean checkParams(TakePhotoOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("TakePhotoOptions is null");
        }
        if (mTakePhotoDir == null) {
            mCallBack.onFailure("创建缓存目录失败，请检查储存设备！");
            return true;
        } else if (!TakePhotoUtil.isDiskAvailable()) {
            mCallBack.onFailure("SD卡空间不足10m，请及时清理！");
            return true;
        }
        options.setCompressedFilePath(mTakePhotoDir);
        if (options.isCreateThumbnail())
            options.setThumbnailFilePath(mTakePhotoDir);
        mTakePhotoOptions = options;
        return false;
    }

    private Intent getIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mOriginalFile)));
        return intent;
    }

    /**
     * 在 Activity 或 Fragment 的 onActivityResult方法调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void notifyTakePhotoChange(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQUEST_CODE_TAKE_PHOTO) {//拍照
            onTakePhotoDone();
        }
    }

    private void onTakePhotoDone() {
        try {
            // 生成压缩图
            TakePhoto compressedOptions = mTakePhotoOptions.getCompressedOptions();
            TakePhotoUtil.createImageThumbnail(mOriginalFile, compressedOptions.path,
                    compressedOptions.width, compressedOptions.height);
            // 生成缩略图
            if (mTakePhotoOptions.isCreateThumbnail()) {
                TakePhoto thumbnailOptions = mTakePhotoOptions.getThumbnailOptions();
                TakePhotoUtil.createImageThumbnail(mOriginalFile, thumbnailOptions.path,
                        thumbnailOptions.width, thumbnailOptions.height);
            }
            mCallBack.onResult(mOriginalFile, mTakePhotoOptions);
        } catch (Exception e) {
            e.printStackTrace();
            mCallBack.onFailure(e.getMessage());
        }
    }

    public void saveInstanceState(Bundle outState) {
        if (mOriginalFile != null && mTakePhotoOptions != null) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_ORIGINAL_FILE, mOriginalFile);
            bundle.putString(KEY_COMPRESSED_FILE, mTakePhotoOptions.getCompressedOptions().path);
            if (mTakePhotoOptions.isCreateThumbnail())
                bundle.putString(KEY_THUMBNAIL_FILE, mTakePhotoOptions.getThumbnailOptions().path);
            if (outState != null)
                outState.putBundle(KEY_INTERNAL_SAVED_VIEW_STATE, bundle);
        }
    }

    public boolean restoreInstanceState(Bundle savedInstanceState, TakePhotoResult callback) {
        if (callback == null) {
            throw new IllegalArgumentException("TakePhotoResult is null");
        }
        mCallBack = callback;
        if (savedInstanceState != null && mTakePhotoOptions != null) {
            Bundle bundle = savedInstanceState.getBundle(KEY_INTERNAL_SAVED_VIEW_STATE);
            if (bundle != null) {
                mOriginalFile = bundle.getString(KEY_ORIGINAL_FILE);
                mTakePhotoOptions.getCompressedOptions().path = bundle.getString(KEY_COMPRESSED_FILE);
                if (mTakePhotoOptions.isCreateThumbnail())
                    mTakePhotoOptions.getThumbnailOptions().path = bundle.getString(KEY_THUMBNAIL_FILE);
                mCallBack.onResult(mOriginalFile, mTakePhotoOptions);
            }
            return true;

        }
        return false;
    }

    public File getTakePhotoDir() {
        return mTakePhotoDir;
    }

    public void onClear() {
        mTakePhotoOptions = null;
        mCallBack = null;
    }
}
