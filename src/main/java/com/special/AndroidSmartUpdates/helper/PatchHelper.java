package com.special.AndroidSmartUpdates.helper;

import android.content.Context;
import android.os.Environment;
import com.special.AndroidSmartUpdates.Constant;

import java.io.File;

/**
 * User: special
 * Date: 13-9-17
 * Time: 下午11:00
 * Mail: specialcyci@gmail.com
 */
public class PatchHelper {

    private final Context context;

    public PatchHelper(Context context) {
        this.context = context;
    }

    public String patch(String patchFilePath){
        System.loadLibrary("Patcher");
        String oldApkFilePath = SystemHelper.getApkFilePath(context);
        String newApkFilePath = Environment.getExternalStorageDirectory() +
                File.separator + Constant.SAVE_FOLDER +
                File.separator + Constant.NEWAPK_FILENAME;
        applyPatch(oldApkFilePath,newApkFilePath,patchFilePath);
        return newApkFilePath;
    }

    public native void applyPatch(String oldApkFilePath,String newApkFilePath,String patchFilePath);

}
