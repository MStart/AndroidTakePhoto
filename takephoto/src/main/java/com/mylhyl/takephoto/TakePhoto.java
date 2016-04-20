package com.mylhyl.takephoto;

/**
 * Created by hupei on 2016/4/14.
 */
final class TakePhoto {
    public String path;//绝对路径
    public int width;//宽
    public int height;//高

    public TakePhoto() {
    }

    public TakePhoto(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
