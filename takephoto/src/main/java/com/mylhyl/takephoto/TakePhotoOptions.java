package com.mylhyl.takephoto;

import android.text.format.DateFormat;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by hupei on 2016/4/14.
 */
public final class TakePhotoOptions {
    /**
     * 只有压缩图(720 * 1280)
     */
    public static final TakePhotoOptions DEFAULT = new TakePhotoOptions();
    private static final int PHOTO_WIDTH = 720; //原图片压缩宽
    private static final int PHOTO_HEIGHT = 1280;//原图片压缩高
    private static final int PHOTO_THUMBNAIL_WIDTH = 180; //缩略图片压缩宽
    private static final int PHOTO_THUMBNAIL_HEIGHT = 320;//缩略图片图片压缩高
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
            mCompressedOptions.path = formatCompressedFilePath(photoDir);
        return this;
    }

    protected TakePhotoOptions setThumbnailFilePath(File photoDir) {
        if (mThumbnailOptions != null)
            mThumbnailOptions.path = formatThumbnailPath(photoDir);
        return this;
    }

    /**
     * 根据目录路径，生成以时间命名的照片路径
     *
     * @param photoDir 拍照根目录路径
     * @return
     */
    private String formatCompressedFilePath(File photoDir) {
        return photoDir.getAbsolutePath() + "/" + mDate + ".jpg";
    }

    /**
     * 根据目录路径与文件路径，在 compressedFilePath 文件加上 small_
     *
     * @param photoDir 拍照根目录路径
     * @return
     */
    private String formatThumbnailPath(File photoDir) {
        return photoDir.getAbsolutePath() + "/small_" + mDate + ".jpg";
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

    public static class Builder {
        private TakePhotoOptions options;

        public Builder() {
            this.options = new TakePhotoOptions();
        }

        public TakePhotoOptions build() {
            return options;
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
