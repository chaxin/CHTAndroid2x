package com.damenghai.chahuitong2.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.damenghai.chahuitong2.R;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageManager {
	private Context mContext;
	private String mPath;
	private View mView;
	private Bitmap mBitmap;
	private BitmapUtils mBu;
	private File mTempFile;

	public ImageManager(Context context) {
		mContext = context;
	}

	/**
	 * 检测Uri是否可用
	 * 
	 * @param uri
	 * @return
	 */
	public boolean checkUri(Uri uri) {
		String strUri = uri.toString();

		File file = null;
		if (strUri.contains("content")) {
            file = new File(uri.getPath());
            L.d("path: " + file.getAbsolutePath());
        } else {
			try {
				file = new File(new URI(strUri));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		if (file != null && file.exists()) {
			mPath = file.getAbsolutePath();
			return true;
		}
		return false;
	}

	/**
	 * 通过当前系统时间获取文件名
	 * 
	 * @return
	 */
	private String getTmpName() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSSSS");
		return dateFormat.format(date);
	}

	/**
	 * 通过文件路径得到压缩后的图片文件
	 * 
	 * @return
	 */
	public File getCompressFile() {
		if (mBitmap == null)
			mBitmap = CommonTool.getBitmapFromPath(mPath);

		if (mTempFile != null)
			return mTempFile;

		String tmpFileName = getTmpName() + ".jpg";
		CompressFormat compressFormat = CompressFormat.JPEG;
		int quality = 60;
		OutputStream stream = null;
		boolean success = false;
		File file = null;
		try {
			file = new File(
					mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
					tmpFileName);
			stream = new FileOutputStream(file);
			success = mBitmap.compress(compressFormat, quality, stream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (success) {
			mTempFile = file;
			return file;
		} else
			return null;
	}

	/**
	 * 删除临时文件
	 */
	public void deleteTempFile() {
		if (mTempFile != null)
			mTempFile.delete();
	}

	/**
	 * 格式以Content开头的Uri转换成File开头的文件路径
	 * 
	 * @param contentUri
	 *            以content开头的Uri
	 * @return 以File开头的文件路径
	 */
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(mContext, contentUri, proj,
				null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * 在控件显示图片
	 * 
	 * @param v
	 */
	public void display(View v) {
		mView = v;
		mBu = new BitmapUtils(mContext);
		mBu.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		if (!TextUtils.isEmpty(mPath)) {
			mBu.display(v, mPath);
		}
	}

	/**
	 * 重新设置控件显示的图片
	 *
	 */
	public void displayDefault() {
		if (mView != null) {
			((ImageView) mView)
					.setImageResource(R.drawable.publish_add_picture);
		}

		if (mBitmap != null) {
			mBitmap.recycle();
			mBitmap = null;
		}
		if(mBu != null) {
			mBu.cancel();
			mBu = null;
		}
	}

	public String getPath() {
		return mPath;
	}

	public void setPath(String path) {
		this.mPath = path;
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public void setBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}

	public void setView(View mView) {
		this.mView = mView;
	}

}
