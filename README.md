# AndroidTakePhoto
一键拍照

#使用说明
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
        
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TakePhotoManager.getInstance().notifyTakePhotoChange(requestCode,resultCode,data);
    }
