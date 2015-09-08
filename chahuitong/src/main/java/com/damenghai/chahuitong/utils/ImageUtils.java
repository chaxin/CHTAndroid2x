package com.damenghai.chahuitong.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Sgun on 15/8/31.
 */
public class ImageUtils {
    public static final int CAMERA_REQUEST_CODE = 0x500;
    public static final int GALLERY_REQUEST_CODE = 0x501;

    public static Uri imageUriFromCamera;

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
        imageUriFromCamera = createImageUri(activity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);
        activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    // 打开图库
    public static void pickImageFromGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    // 创建一条Uri用于保存拍照后的照片
    public static Uri createImageUri(Context context) {
        String name = "CHT" + System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, name);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return uri;
    }
    // 删除一条Uri
    public static void deleteImageUri(Context context) {
        context.getContentResolver().delete(imageUriFromCamera, null, null);
    }

    /**
	 * 通过路径生成Base64文件
	 * @param uri 路径
	 * @return Base64
	 */
	public static String getBase64FromUri(Uri uri, Context context)
	{
		String base64="";
		try
		{
			File file = new File(getRealPathFromURI(uri, context));
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
    private static String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, proj,
                null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
