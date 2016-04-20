## AndroidTakePhoto
此库内部将拍照后的图片压缩输出，可选输出缩略图，可自定义输出图片大小，
有效解决拍照后内存溢出，界面异常摧毁及屏幕旋转导致数据丢失。


##使用Gradle构建时添加一下依赖即可:
```javascript
compile 'com.mylhyl:takephoto:1.0.3'
```
####如果使用eclipse可以 [点击这里下载aar文件](https://dl.bintray.com/mylhyl/maven/com/mylhyl/takephoto/), 然后用zip解压, 取出jar包.

##需要权限
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

##使用说明
Activity 中 onCreate(Bundle savedInstanceState) 或 Fragment 中 onActivityCreated(Bundle savedInstanceState) 使用
```java    
        TakePhotoManager.getInstance().createForResult(savedInstanceState, new TakePhotoResult() {
            @Override
            public void onFailure(String message) {
                Toast.makeText(TakeActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResultFile(File originalFile, final File compressedFile) {
                Bitmap bitmap = BitmapFactory.decodeFile(compressedFile.getAbsolutePath());
                iv.setImageBitmap(bitmap);
            }
        });
```
在拍照事件的中调用 requestForResult 方法，TakePhotoOptions 参数可指定输出图片大小
```java
    @Override
    public void onClick(View v) {
           // TakePhotoManager.getInstance().request(this,
           //         new TakePhotoOptions.Builder().setThumbnailSize().build());
        TakePhotoManager.getInstance().request(this);
    }
```
 在Activity 或 Fragment 中重写 onActivityResult 方法，调用 notifyTakePhotoChange 方法通知回调
```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TakePhotoManager.getInstance().activityResult(requestCode,resultCode,data);
    }
```
数据恢复
```java
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        TakePhotoManager.getInstance().saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
```
详细见demo