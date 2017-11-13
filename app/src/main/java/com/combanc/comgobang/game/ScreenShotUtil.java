package com.combanc.comgobang.game;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import top.zibin.luban.Luban;

/**
 * @author zhaojun (Email:laobadaozjj@gmail.com)
 *         <p>
 *         2017/11/13 9:10
 */

public class ScreenShotUtil {
    public static String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
    public static String SDCARD_CLASSROOM_PATH = SDCARD_PATH + "/chess";
    public static String SDCARD_CLASSROOM_PATH_SCREENSHOT = SDCARD_CLASSROOM_PATH + "/screen";
    public static String SDCARD_CLASSROOM_PATH_SCREENSHOT_File = SDCARD_CLASSROOM_PATH_SCREENSHOT + "/screenshot.png";
    public static String SDCARD_CLASSROOM_PATH_SCREENSHOT_File_zip = SDCARD_CLASSROOM_PATH_SCREENSHOT + "/screenshot.zip";
    private static final String TAG = ScreenShotUtil.class.getSimpleName();

    public static void takeScreenShot(Activity childActivity) {
        Log.d(TAG, "takeScreenShot");
        Bitmap bitmap = cutStatusBarScreen(childActivity);
        if (bitmap != null) {
            try {
                saveBitmap(bitmap);

            } catch (Exception e) {
                Log.d(TAG, "takeScreenShot Exception");
                e.printStackTrace();
            }
        }

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public static void saveBitmap(Bitmap bitmap) {
        Log.d(TAG, "saveBitmap");
        FileUtils.createOrExistsDir(SDCARD_CLASSROOM_PATH_SCREENSHOT);
        File f = new File(SDCARD_CLASSROOM_PATH_SCREENSHOT_File);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
            out.flush();
            out.close();
            ImageCompress(f);
            bitmap.recycle();
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            Log.d(TAG, "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "IOException");
            e.printStackTrace();
        }

    }

    /**
     * 去掉状态栏
     */
    public static Bitmap cutStatusBarScreen(Activity childActivity) {
        View view = childActivity.getWindow().getDecorView();
        FileUtils.deleteFile(SDCARD_CLASSROOM_PATH_SCREENSHOT_File);
        FileUtils.deleteFile(SDCARD_CLASSROOM_PATH_SCREENSHOT_File_zip);
        view.buildDrawingCache();
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        DisplayMetrics dm = new DisplayMetrics();
        childActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widths = dm.widthPixels;
        int heights = dm.heightPixels;
        view.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);
        view.destroyDrawingCache();
        return bmp;
    }

    public static void ImageCompress(File file) {
        File file1 = null;
        try {
            file1 = Luban.with(Utils.getContext()).load(file).get();
            FileUtils.deleteFile(SDCARD_CLASSROOM_PATH_SCREENSHOT_File);
            boolean issu = FileUtils.copyFile(file1, new File(SDCARD_CLASSROOM_PATH_SCREENSHOT_File));
            Log.d(TAG, "ImageCompress: issu==" + issu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}