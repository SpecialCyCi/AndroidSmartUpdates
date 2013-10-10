package com.special.AndroidSmartUpdates;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;
import com.special.AndroidSmartUpdates.exception.DownloadException;
import com.special.AndroidSmartUpdates.exception.SDCardNotExistedException;
import com.special.AndroidSmartUpdates.exception.UpdateInformationException;
import com.special.AndroidSmartUpdates.helper.*;
import com.special.AndroidSmartUpdates.impl.DownloadListener;
import com.special.AndroidSmartUpdates.impl.UpdateListener;
import com.special.AndroidSmartUpdates.model.PatchInformation;
import com.special.AndroidSmartUpdates.model.UpdateInformation;

import java.io.File;

import static android.content.DialogInterface.*;

/**
 * User: special
 * Date: 13-9-16
 * Time: 上午10:36
 * Mail: specialcyci@gmail.com
 */
public class SmartUpdates{

    public static final int MESSAGE_DOWNLOAD_COMPLETE = 2;
    public static final int MESSAGE_DOWNLOAD_ERROR  = 3;
    public static final int MESSAGE_SDCARD_ERROR    = 4;
    private final Context context;
    private UpdateInformation  updateInfor;
    private NotificationHelper notificationHelper;
    private DownloadHelper downloadHelper;
    private UpdateListener updateListener;
    private PatchHelper patchHelper;
    private ApiHelper api;
    private boolean showUpdateDialog;

    public SmartUpdates(Context context) {
        this.context = context;
        this.showUpdateDialog = true;
        initInstance();
    }

    private void initInstance(){
        patchHelper = new PatchHelper(context);
        api = new ApiHelper();
        api.setPackageName(SystemHelper.getPackageName(context));
        api.setCurrentVersionCode(SystemHelper.getVersionCode(context));
    }

    /**
     * client to the server, check if has update;
     */
    public void checkForUpdate() {
        new checkUpdateThread().execute();
    }

    /**
     * start to download path file, and patch for application;
     */
    public void startUpdate(){
        notificationHelper = new NotificationHelper(context);
        notificationHelper.showNotification();
        new updateThread().execute();
    }

    private class checkUpdateThread extends AsyncTask<Integer, Integer, UpdateInformation> {

        @Override
        protected UpdateInformation doInBackground(Integer... integers) {
            return getUpdateInformation();
        }

        @Override
        protected void onPostExecute(UpdateInformation updateInformation) {
            updateInfor = updateInformation;
            if(!isValidInformation(updateInformation)) return;
            if(isHasUpdate(updateInfor)) {
                notifyHasUpdateListener();
                showUpdateDialog();
            }else{
                notifyNoUpdateListener();
            }
        }
    }

    private boolean isValidInformation(UpdateInformation updateInformation){
        try {
            UpdateInformationException.isValidInformation(context,updateInformation);
            return true;
        }catch (UpdateInformationException e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private UpdateInformation getUpdateInformation(){
        return api.getUpdateInformation();
    }

    private boolean isHasUpdateListener(){
        return updateListener != null;
    }

    private void notifyHasUpdateListener(){
        if (isHasUpdateListener())
            updateListener.hasUpdate(updateInfor.getPatchInformation());
    }

    private void notifyNoUpdateListener(){
        if (isHasUpdateListener())
            updateListener.hasNoUpdate();
    }

    private boolean isHasUpdate(UpdateInformation infor){
        return infor != null && infor.isHasUpdate();
    }

    private void showUpdateDialog(){
        if(!showUpdateDialog) return;
        DialogHelper dialogHelper = new DialogHelper(context);
        dialogHelper.setMessage(buildUpdateMessageString());
        dialogHelper.setUpdateButtonClickEvent(updateButtonEvent);
        dialogHelper.showDialog();
    }

    private String buildUpdateMessageString(){
        StringBuilder builder = new StringBuilder();
        PatchInformation information = updateInfor.getPatchInformation();
        builder.append(context.getString(R.string.dialog_version_name));
        builder.append(information.getVersionName() + "\n");
        builder.append(context.getString(R.string.dialog_update_time));
        builder.append(information.getUpdateTimeString() + "\n");
        builder.append(context.getString(R.string.dialog_patch_file_size));
        builder.append(information.getPatchSizeString() + "\n");
        builder.append(information.getDescription());
        return builder.toString();
    }

    private void showError(int stringResourceId){
        Toast.makeText(context,stringResourceId,Toast.LENGTH_LONG).show();
        if(notificationHelper != null)
         notificationHelper.dismissNotification();
    }

    private OnClickListener updateButtonEvent = new OnClickListener() {

        public void onClick(DialogInterface dialog, int id) {
            startUpdate();
        }
    };

    private class updateThread extends AsyncTask<Integer, Integer, Integer> implements DownloadListener{

        @Override
        public void onDownloading(int progress) {
            publishProgress(progress);
        }

        @Override
        public void onDownloadComplete(String filePath) {
            String newApkFilePath = patchHelper.patch(filePath);
            deleteFile(filePath);
            installNewApk(newApkFilePath);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            downloadHelper = new DownloadHelper(context);
            downloadHelper.setDownloadListener(this);
            return startToDownloadUpdateFile();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            updateNotificationProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer message) {
            if (message == MESSAGE_DOWNLOAD_ERROR)
                showError(R.string.toast_download_error);
            else if (message == MESSAGE_SDCARD_ERROR)
                showError(R.string.toast_sdcard_not_existed);
        }
    }

    private int startToDownloadUpdateFile(){
        try {
            downloadHelper.startDownload(api.getDownloadUrl());
            return MESSAGE_DOWNLOAD_COMPLETE;
        } catch (SDCardNotExistedException e) {
            return MESSAGE_SDCARD_ERROR;
        } catch (DownloadException e) {
            return MESSAGE_DOWNLOAD_ERROR;
        }
    }

    private void updateNotificationProgress(int progress){
        notificationHelper.updateProgress(progress);
    }

    private void deleteFile(String path){
        new File(path).delete();
    }

    private void installNewApk(String newApkFilePath) {
        notificationHelper.dismissNotification();
        SystemHelper.installApk(context,newApkFilePath);
    }

    public void setServerAddress(String serverAddress){
        Config.setServerAddress(serverAddress);
    }

    public String getServerAddress(){
        return Config.getServerAddress();
    }

    public void setApplicationId(int applicationId){
        Config.setApplicationId(applicationId);
    }

    public int getApplicationId(){
        return Config.getApplicationId();
    }

    /**
     * set a update listener, which can listen
     * the application has update or not
     *
     * @param updateListener
     */
    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public UpdateListener getUpdateListener() {
        return updateListener;
    }

    /**
     * set the update information dialog's showing state
     * while has update situation.
     *
     * if you set false, the update dialog will not display
     * when smart update discover than has update information.
     *
     * @param showUpdateDialog
     */
    public void setShowUpdateDialog(boolean showUpdateDialog) {
        this.showUpdateDialog = showUpdateDialog;
    }

    public boolean isShowUpdateDialog() {
        return showUpdateDialog;
    }

    /**
     * helper method make test framework easy to mock
     * patch helper;
     *
     * @param patchHelper
     */
    public void setMockPatchHelper(PatchHelper patchHelper) {
        this.patchHelper = patchHelper;
    }

    /**
     * helper method make test framework easy to mock
     * api helper;
     *
     * @param apiHelper
     */
    public void setMockApiHelper(ApiHelper apiHelper) {
        this.api = apiHelper;
    }
}
