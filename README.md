# AndroidTakePhoto
一键拍照

引用
compile 'com.mylhyl:takePhoto:1.0.0'

需要权限
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

使用说明
在拍照事件的中调用 requestTakePhotoForResult 方法
TakePhotoOptions 参数可指定输出图片大小
```java
       TakePhotoManager.getInstance().requestTakePhotoForResult(this, new TakePhotoOptions.Builder().build(),
       new TakePhotoResult() {
            @Override
            public void onFailure(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResultFile(File originalFile, File compressedFile) {
                iv.setImageBitmap(BitmapFactory.decodeFile(compressedFile.getAbsolutePath()));
            }
        });
```
在Activity 或 Fragment 中重写 onActivityResult 方法
调用 notifyTakePhotoChange 方法通知回调
```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TakePhotoManager.getInstance().notifyTakePhotoChange(requestCode,resultCode,data);
    }
```
