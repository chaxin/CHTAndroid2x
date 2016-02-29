package com.damenghai.chahuitong2.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 图片选择
 *
 * Created by Sgun on 15/8/31.
 */
public class ImageUtils {
    public static final int CAMERA_REQUEST_CODE = 0x500;
    public static final int GALLERY_REQUEST_CODE = 0x501;
    public static final int ZOOM_REQUEST_CODE = 0x502;

    public static Uri imageUri;

    public static void showImagePickDialog(final Activity activity) {
        String[] item = new String[] {"拍照", "图库"};

        new AlertDialog.Builder(activity).setItems(item, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        pickImageFromCamera(activity);
                        break;
                    case 1:
                        pickImageFromGallery(activity);
                        break;
                }
            }

        }).show();
    }

    // 打开照相机
    public static void pickImageFromCamera(Activity activity) {
        imageUri = createImageUri(activity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    // 打开图库
    public static void pickImageFromGallery(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    // 打开裁剪图片界面
    public static void showZoomImage(Activity activity, Uri uri) {
        imageUri = createImageUri(activity);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, ZOOM_REQUEST_CODE);
    }

    // 创建一条Uri用于保存拍照后的照片
    public static Uri createImageUri(Context context) {
        String name = "CHT" + System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, name);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    // 删除一条Uri
    public static void deleteImageUri(Context context) {
        if (imageUri != null) context.getContentResolver().delete(imageUri, null, null);
    }

    /**
	 * 通过路径生成Base64文件
	 * @param uri 路径
	 * @return Base64
	 */
	public static String getBase64FromUri(Context context, Uri uri)
	{
        File file = new File(getRealPathFromURI(uri, context));
        return getBase64FromFile(file);
	}

    /**
     * 通过路径生成Base64文件
     * @param file 路径
     * @return Base64
     */
    public static String getBase64FromFile(File file)
    {
        String base64="";
        try
        {
            byte[] buffer = new byte[(int) file.length() + 100];
            @SuppressWarnings("resource")
            int length = new FileInputStream(file).read(buffer);
            base64 = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
    }

    /**
     * 格式以Content开头的Uri转换成File开头的文件路径
     *
     * @param contentUri
     *            以content开头的Uri
     * @return 以File开头的文件路径
     */
    public static String getRealPathFromURI(Uri contentUri, Context context) {
        String uri = contentUri.toString();
        if(uri.startsWith("file")) return uri;

        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, proj,
                null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return column_index == 0 ? "" : cursor.getString(column_index);
    }

    public static String getCameraImagePath(Context context) {
        return getImageAbsolutePath19((Activity) context, imageUri);
    }

    /////////////////////Android4.4以上版本特殊处理如下//////////////////////////////////////

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     * @param context
     * @param imageUri
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getImageAbsolutePath19(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }

        // MediaStore (and general)
        if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
