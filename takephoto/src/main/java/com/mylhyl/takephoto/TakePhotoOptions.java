package com.mylhyl.takephoto;

import android.text.format.DateFormat;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by hupei on 2016/4/14.
 */
public final class TakePhotoOptions implements Serializable {
    private static final int PHOTO_WIDTH = 720; //原图片压缩宽
    private static final int PHOTO_HEIGHT = 1280;//原图片压缩高
    private static final int PHOTO_THUMBNAIL_WIDTH = 180; //缩略图片压缩宽
    private static final int PHOTO_THUMBNAIL_HEIGHT = 320;//缩略图片图片压缩高
    private File mTakePhotoDir;
    private File mOriginalFile;//图始图
    private TakePhoto mCompressedOptions;
    private TakePhoto mThumbnailOptions;
    private boolean mCreateThumbnail;
    private String mDate;

    protected TakePhotoOptions() {
        mDate = new DateFormat().format("yyyy_MMdd_hhmmss", Calendar.getInstance(Locale.CHINA)).toString();
        newCompressedOptions();
    }

    protected void newCompressedOptions() {
        mCompressedOptions = new TakePhoto(PHOTO_WIDTH, PHOTO_HEIGHT);
    }

    protected void newThumbnailOptions() {
        newThumbnailOptions(PHOTO_THUMBNAIL_WIDTH, PHOTO_THUMBNAIL_HEIGHT);
    }

    protected void newThumbnailOptions(int width, int height) {
        mCreateThumbnail = true;
        mThumbnailOptions = new TakePhoto(width, height);
    }

    protected TakePhotoOptions setCompressedFilePath(File photoDir) {
        if (mCompressedOptions != null)
            mCompressedOptions.file = formatCompressedFile(photoDir);
        return this;
    }

    protected TakePhotoOptions setThumbnailFilePath(File photoDir) {
        if (mThumbnailOptions != null)
            mThumbnailOptions.file = formatThumbnailFile(photoDir);
        return this;
    }

    /**
     * 根据目录路径，生成以时间命名的照片路径
     *
     * @param photoDir 拍照根目录路径
     * @return
     */
    private File formatCompressedFile(File photoDir) {
        return new File(photoDir.getAbsolutePath() + "/" + mDate + ".jpg");
    }

    /**
     * 根据目录路径与文件路径，在 compressedFilePath 文件加上 small_
     *
     * @param photoDir 拍照根目录路径
     * @return
     */
    private File formatThumbnailFile(File photoDir) {
        return new File(photoDir.getAbsolutePath() + "/small_" + mDate + ".jpg");
    }

    protected boolean isCreateThumbnail() {
        return mCreateThumbnail;
    }

    protected TakePhoto getCompressedOptions() {
        return mCompressedOptions;
    }

    protected TakePhoto getThumbnailOptions() {
        return mThumbnailOptions;
    }

    protected File getOriginalFile() {
        return mOriginalFile;
    }

    protected File getTakePhotoDir() {
        return mTakePhotoDir;
    }

    public static class Builder {
        private TakePhotoOptions options;

        public Builder() {
            this.options = new TakePhotoOptions();
        }

        public TakePhotoOptions build() {
            if (options.isCreateThumbnail() && options.mTakePhotoDir != null)
                options.setThumbnailFilePath(options.mTakePhotoDir);
            return options;
        }

        public Builder setTakePhotoDir(File dir) {
            options.mTakePhotoDir = dir;
            options.setCompressedFilePath(dir);
            return this;
        }

        public Builder setTakePhotoFile(File file) {
            options.mOriginalFile = file;
            return this;
        }

        /**
         * 设置压缩图
         *
         * @param width
         * @param height
         * @return
         */
        public Builder setCompressedSize(int width, int height) {
            options.mCompressedOptions.width = width;
            options.mCompressedOptions.height = height;
            return this;
        }

        /**
         * 设置缩略图
         *
         * @param width
         * @param height
         * @return
         */
        public Builder setThumbnailSize(int width, int height) {
            options.newThumbnailOptions(width, height);
            return this;
        }

        /**
         * 设置缩略图（180 * 320）
         *
         * @return
         */
        public Builder setThumbnailSize() {
            options.newThumbnailOptions();
            return this;
        }
    }
}
