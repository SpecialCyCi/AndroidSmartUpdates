package com.special.AndroidSmartUpdates.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: special
 * Date: 13-9-16
 * Time: 下午4:29
 * Mail: specialcyci@gmail.com
 */
public class SystemHelper {

    /**
     * return the package name;
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context){
        return context.getPackageName();
    }

    /**
     * get the currentVersion Code;
     * @return versionCode;
     */
    public static int getVersionCode(Context context){
        try {
            return getPackageInfo(context).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getApkFilePath(Context context){
        try {
            return getPackageInfo(context).applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        return  packageManager.getPackageInfo(context.getPackageName(), 0);
    }

    public static void installApk(Context context,String filePath){
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String getTimeStringFromTimeStamp(long timeStamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date(timeStamp * 1000));
    }

    public static String getFileSizeFromByteSize(long fileByteSize) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileByteSize < 1024) {
            fileSizeString = decimalFormat.format((double) fileByteSize) + "B";
        } else if (fileByteSize < 1048576) {
            fileSizeString = decimalFormat.format((double) fileByteSize / 1024) + "K";
        } else if (fileByteSize < 1073741824) {
            fileSizeString = decimalFormat.format((double) fileByteSize / 1048576) + "M";
        } else {
            fileSizeString = decimalFormat.format((double) fileByteSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

}
