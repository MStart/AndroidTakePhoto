# AndroidTakePhoto
一键拍照

#使用说明
在拍照事件的中调用 requestTakePhotoForResult 方法
TakePhotoOptions 参数可指定输出图片大小

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
        
在Activity 或 Fragment 中重写 onActivityResult 方法
调用 notifyTakePhotoChange 方法通知回调

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TakePhotoManager.getInstance().notifyTakePhotoChange(requestCode,resultCode,data);
    }
