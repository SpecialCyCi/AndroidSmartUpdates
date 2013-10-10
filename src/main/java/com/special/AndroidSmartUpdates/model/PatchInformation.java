package com.special.AndroidSmartUpdates.model;

import android.util.Log;
import com.special.AndroidSmartUpdates.helper.SystemHelper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: special
 * Date: 13-10-4
 * Time: 下午1:17
 * Mail: specialcyci@gmail.com
 */
public class PatchInformation {

    private int applicationId;
    private int versionCode;
    private String versionName;
    private String description;
    private long apkUpdatedAt;
    private long apkPatchSize;

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getApkUpdatedAt() {
        return apkUpdatedAt;
    }

    public void setApkUpdatedAt(long apkUpdatedAt) {
        this.apkUpdatedAt = apkUpdatedAt;
    }

    public long getApkPatchSize() {
        return apkPatchSize;
    }

    public void setApkPatchSize(long apkPatchSize) {
        this.apkPatchSize = apkPatchSize;
    }

    public String getUpdateTimeString() {
        return SystemHelper.getTimeStringFromTimeStamp(this.apkUpdatedAt);
    }

    public String getPatchSizeString() {
        return SystemHelper.getFileSizeFromByteSize(this.apkPatchSize);
    }

    public static PatchInformation parseFrom(String information) {
        try {
            JSONObject jsonObject = new JSONObject(information);
            return parseJsonObject(jsonObject);
        } catch (Exception e) {
            Log.e("parse error", information);
        }
        return null;
    }

    private static PatchInformation parseJsonObject(JSONObject jsonObject) throws Exception {
        PatchInformation patchInformation = new PatchInformation();
        patchInformation.applicationId = jsonObject.getInt("application_id");
        patchInformation.versionCode = jsonObject.getInt("version_code");
        patchInformation.versionName = jsonObject.getString("version_name");
        patchInformation.description = jsonObject.getString("description");
        patchInformation.apkUpdatedAt = jsonObject.getLong("apk_updated_at");
        patchInformation.apkPatchSize = jsonObject.getLong("apk_patch_size");
        return patchInformation;
    }

}
