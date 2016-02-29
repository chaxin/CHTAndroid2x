package com.damenghai.chahuitong2.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class CommonTool {
//	// 缩放/裁剪图片
// 	public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight)
// 	{
// 		// 获得图片宽高
//  	   int width = bm.getWidth();
//  	   int height = bm.getHeight();
//
//  	   float scaleWidth;
//	   float scaleHeight;
//
//  	   if(width > height && (width > newWidth || height > newHeight)) {
//  		   scaleWidth = scaleHeight = ((float) newHeight) / height;
//  	   } else if(width < height && (width > newWidth || height > newHeight)) {
//  		   scaleWidth = scaleHeight = ((float) newWidth) / width;
//  	   } else {
//  		   scaleWidth = scaleHeight = 1;
//  	   }
//  	   L.d("scale: " + String.valueOf(scaleWidth));
//  	   // 计算缩放比例
//
//  	   L.d(String.valueOf(scaleWidth));
//  	   L.d(String.valueOf(scaleHeight));
//  	   // 取得想要缩放的matrix参数
//  	   Matrix matrix = new Matrix();
//  	   matrix.postScale(scaleWidth, scaleHeight);
//  	   // 得到新的图片
//  	   Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
//  	   return newbm;
// 	}
//
// 	// 从路径获取文件名
// 	public static String getFileName(String pathandname){
// 		int start=pathandname.lastIndexOf("/");
//         int end=pathandname.lastIndexOf(".");
//         if(start!=-1 && end!=-1){
//             return pathandname.substring(start+1,end);
//         }else{
//             return null;
//         }
// 	}
//
//	/**
//	 *
//	 * 通过文件路径获取到Bitmap，强制缩放
//	 *
// 	 */
// 	public static Bitmap getBitmapFromPath(String path, int w, int h) {
//		BitmapFactory.Options opts = new BitmapFactory.Options();
//		// 设为true只获取图片信息，如大小，尺寸等，不把图片load到内存
//		opts.inJustDecodeBounds = true;
//		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//		BitmapFactory.decodeFile(path, opts);
//		int width = opts.outWidth;
//		int height = opts.outHeight;
//		float scaleWidth = 0.f, scaleHeight = 0.f;
//		if (width > w || height > h) {
//			// 缩放
//			scaleWidth = ((float) width) / w;
//			scaleHeight = ((float) height) / h;
//		}
//		opts.inJustDecodeBounds = false;
//		float scale = Math.max(scaleWidth, scaleHeight);
//		opts.inSampleSize = (int)scale;
//		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
//		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
//	}
//

    /**
     * 通过文件路径按比例缩放获取Bitmap
     */
    public static Bitmap getBitmapFromPath(String path) {
        // 通过路径Bitmap.Options对象
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // inJustDecodeBounds设为true可不用把图片加载到内存就能获取图片信息
        newOpts.inJustDecodeBounds = true;
        // 此时bm返回空
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);

//        L.d("before compress height:" + bitmap.getHeight() + ", width: " + bitmap.getWidth()
//                    + ", size:" + bitmap.getHeight() * bitmap.getRowBytes());

        // 获取图片宽度和高度
        int outWidth = newOpts.outWidth;
        int outHeight = newOpts.outHeight;
        // 设置一个缩放基数
        float targetWidth = 480f;
        float targetHeight = 800f;
        // 计算缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        // 根据大的边进行缩放
        if (outWidth > outHeight && outWidth > targetHeight) {
            be = (int) (outWidth / targetHeight);
        } else if (outWidth < outHeight && outHeight > targetWidth) {
            be = (int) (outHeight / targetWidth);
        }
        if (be <= 0) be = 1;
        //设置缩放比例
        newOpts.inSampleSize = be;
        newOpts.inJustDecodeBounds = false;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        return BitmapFactory.decodeFile(path, newOpts);
    }
//
//	/**
//	 * 通过路径生成Base64文件
//	 * @param path 路径
//	 * @return Base64
//	 */
//	public static String getBase64FromPath(String path)
//	{
//		String base64="";
//		try
//		{
//			File file = new File(path);
//			byte[] buffer = new byte[(int) file.length() + 100];
//			@SuppressWarnings("resource")
//			int length = new FileInputStream(file).read(buffer);
//			base64 = Base64.encodeToString(buffer, 0, length,  Base64.DEFAULT);
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//		return base64;
//	}
//

    /**
     * 把Bitmap转换成Base64
     */
    public static String getBase64FromBitmap(Bitmap bitmap, int bitmapQuality) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream);
        byte[] bytes = bStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
//
//	/**
//	 * 把Base64转换成Bitmap
//	 * @param string Base64
//	 * @return Bitmap
//	 */
// 	public static Bitmap getBitmapFromBase64(String string)
// 	{
// 		byte[] bitmapArray = null;
// 		try {
// 		bitmapArray = Base64.decode(string, Base64.DEFAULT);
// 		} catch (Exception e) {
// 		e.printStackTrace();
// 		}
// 		return BitmapFactory.decodeByteArray(bitmapArray, 0,bitmapArray.length);
// 	}
//
//	/**
//	 *
//	 * 把Stream转换成String
//	 *
//	 */
// 	public static String convertStreamToString(InputStream is) {
// 		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//			StringBuilder sb = new StringBuilder();
//			String line = null;
//
//			try {
//				while ((line = reader.readLine()) != null) {
//					sb.append(line + "/n");
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					is.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			return sb.toString();
// 	}
//
//	/**
//	 *
//	 * 修改整个界面所有控件的字体
//	 *
// 	 */
// 	public static void changeFonts(ViewGroup root,String path, Activity act) {
//		// path是字体路径
// 		Typeface tf = Typeface.createFromAsset(act.getAssets(),path);
//        for (int i = 0; i < root.getChildCount(); i++) {
//            View v = root.getChildAt(i);
//            if (v instanceof TextView) {
//               ((TextView) v).setTypeface(tf);
//            } else if (v instanceof Button) {
//               ((Button) v).setTypeface(tf);
//            } else if (v instanceof EditText) {
//               ((EditText) v).setTypeface(tf);
//            } else if (v instanceof ViewGroup) {
//               changeFonts((ViewGroup) v, path,act);
//            }
//        }
//     }
//
//	/**
//	 *
//	 * 修改整个界面所有控件的字体大小
//	 *
//	 */
//  	public static void changeTextSize(ViewGroup root,int size, Activity act) {
//         for (int i = 0; i < root.getChildCount(); i++) {
//             View v = root.getChildAt(i);
//             if (v instanceof TextView) {
//                ((TextView) v).setTextSize(size);
//             } else if (v instanceof Button) {
//            	((Button) v).setTextSize(size);
//             } else if (v instanceof EditText) {
//            	((EditText) v).setTextSize(size);
//             } else if (v instanceof ViewGroup) {
//                changeTextSize((ViewGroup) v,size,act);
//             }
//         }
//      }
//
//	/**
//	 *
//	 * 不改变控件位置，修改控件大小
//	 *
//	 */
//	public static void changeWH(View v,int W,int H)
//	{
//		LayoutParams params = (LayoutParams)v.getLayoutParams();
//	    params.width = W;
//	    params.height = H;
//	    v.setLayoutParams(params);
//	}
//
//	/**
//	 *
//	 * 修改控件的高
//	 *
// 	 */
//	public static void changeH(View v,int H)
//	{
//		LayoutParams params = (LayoutParams)v.getLayoutParams();
//	    params.height = H;
//	    v.setLayoutParams(params);
//	}

    /**
     * 检查SD卡是否存在
     */
    public static boolean checkSdCard() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

}