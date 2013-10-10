import android.app.*;
import android.content.Context;
import android.os.Environment;
import android.widget.Button;
import com.special.AndroidSmartUpdates.Constant;
import com.special.AndroidSmartUpdates.R;
import com.special.AndroidSmartUpdates.SmartUpdates;
import com.special.AndroidSmartUpdates.helper.ApiHelper;
import com.special.AndroidSmartUpdates.helper.PatchHelper;
import com.special.AndroidSmartUpdates.impl.UpdateListener;
import com.special.AndroidSmartUpdates.model.PatchInformation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.*;
import java.io.File;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * User: special
 * Date: 13-9-16
 * Time: 上午10:14
 * Mail: specialcyci@gmail.com
 */
@RunWith(RobolectricTestRunner.class)
public class TestSmartUpdates{

    private static final String UPDATE_SERVER_ADDRESS = "http://192.168.1.171:8080/smartupdates/";
    private ShadowActivity activity;
    private Application application;
    private AlertDialog alertDialog;
    private SmartUpdates smartUpdates;

    public void startCheckUpdate() throws Exception {
        setUpActivity();
        setUpApplication();
        checkForUpdate();
        setUpDialog();
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED);
    }

    private void setUpActivity(){
        activity = new ShadowActivity();
        activity.onCreate(null);
    }

    private void setUpApplication(){
        application = activity.getApplication();
    }

    private void setUpDialog(){
        alertDialog = ShadowAlertDialog.getLatestAlertDialog();
    }

    private void checkForUpdate(){
        smartUpdates = new SmartUpdates(activity.getApplicationContext());
        smartUpdates.setServerAddress(UPDATE_SERVER_ADDRESS);
        smartUpdates.checkForUpdate();
    }

    @Test
    public void testRequsetFailed() throws Exception{
        Robolectric.setDefaultHttpResponse(500, "");
        startCheckUpdate();
        assertNull(alertDialog);
        assertFalse(activity.isFinishing());
    }

    @Test
    public void testHasUpdate() throws Exception{
        startHasUpdateSituation();
        shouldHasUpdateDialog();
    }

    @Test
    public void testUpdateListenerWhenHasUpdate() throws Exception{
        pauseBackgroundWork();
        startHasUpdateSituation();
        UpdateListener listener = mockUpdateListener();
        contiuneBackgroundWork();
        verify(listener, times(1)).hasUpdate(any(PatchInformation.class));
        verify(listener, never()).hasNoUpdate();
    }

    @Test
    public void testUpdateListenerWhenNoUpdate() throws Exception{
        pauseBackgroundWork();
        startNoUpdateSituation();
        UpdateListener listener = mockUpdateListener();
        contiuneBackgroundWork();
        verify(listener, never()).hasUpdate(any(PatchInformation.class));
        verify(listener, times(1)).hasNoUpdate();
    }

    private UpdateListener mockUpdateListener(){
        UpdateListener listener = mock(UpdateListener.class);
        smartUpdates.setUpdateListener(listener);
        return listener;
    }

    @Test
    public void testSetShowUpdateDialog() throws Exception{
        pauseBackgroundWork();
        startHasUpdateSituation();
        smartUpdates.setShowUpdateDialog(false);
        contiuneBackgroundWork();
        assertFalse(isDialogShowing());
    }

    @Test
    public void testParseErrorSituaction() throws Exception{
        startParseSituation();
        assertTrue(isToastTextEqualTo(R.string.toast_parse_error));
        assertFalse(isDialogShowing());
    }

    @Test
    public void testApplicationNotExistedSituaction() throws Exception{
        startApplicationNotExistedSituation();
        assertTrue(isToastTextEqualTo(R.string.toast_application_not_existed));
        assertFalse(isDialogShowing());
    }

    @Test
    public void testCurrentVersionNotExistedSituation() throws Exception{
        startCurrentVersionNotExistedSituation();
        assertTrue(isToastTextEqualTo(R.string.toast_current_version_not_existed));
        assertFalse(isDialogShowing());
    }

    @Test
    public void testPackageNameNotMatchSituation() throws Exception{
        startPackageNameNotMatchSituation();
        assertTrue(isToastTextEqualTo(R.string.toast_package_name_not_match));
        assertFalse(isDialogShowing());
    }


    @Test
    public void testHasUpdateAndPressOkButton() throws Exception {
        startHasUpdateSituation();
        stubDownLoadUrl("http://www.baidu.com/");
        stubPathFileUrl(getNewApkPath());
        pauseBackgroundWork();
        pressingOnDialogUpdateButton();
        assertTrue(isNotificationShowing());
        contiuneBackgroundWork();
        checkPatchFileIsDelete();
    }

    private void checkPatchFileIsDelete(){
        File patchFile = new File(ShadowEnvironment.getExternalStorageDirectory() +
             File.separator + Constant.SAVE_FOLDER + File.separator
                + Constant.SAVE_FILENAME);
        assertFalse(patchFile.exists());
    }

    @Test
    public void testHasUpdateAndPressCancelButton() throws Exception {
        startHasUpdateSituation();
        pressingDialogCancelButton();
        assertFalse(isNotificationShowing());
    }

    @Test
    public void testHasUpdateAndPressUpdateButtonButSDCardNotExisted() throws Exception{
        startHasUpdateSituation();
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_UNMOUNTED);
        pressingOnDialogUpdateButton();
        assertTrue(isToastTextEqualTo(R.string.toast_sdcard_not_existed));
        assertFalse(isNotificationShowing());
    }

    @Test
    public void testHasUpdateAndPressUpdateButtonButDownloadUrlNotExisted() throws Exception{
        startHasUpdateSituation();
        stubDownLoadUrl("http://255.255.255.255/");
        pressingOnDialogUpdateButton();
        assertTrue(isToastTextEqualTo(R.string.toast_download_error));
        assertFalse(isNotificationShowing());
    }

    @Test
    public void testNoUpdate() throws Exception{
        startNoUpdateSituation();
        assertFalse(isDialogShowing());
    }

    private void shouldHasUpdateDialog() throws Exception{
        ShadowAlertDialog shadowAlertDialog = Robolectric.shadowOf(alertDialog);
        String dialog_title = application.getString(R.string.dialog_title);
        assertEquals(shadowAlertDialog.getTitle(), dialog_title);
        System.out.println(shadowAlertDialog.getMessage());
        checkHasStringInDialog(shadowAlertDialog, "1.1");
        checkHasStringInDialog(shadowAlertDialog,"version 2");
        checkHasStringInDialog(shadowAlertDialog,"2.15K");
        checkHasStringInDialog(shadowAlertDialog, "2013-10-04");
    }

    private void checkHasStringInDialog(ShadowAlertDialog dialog,String wantString){
        assertNotSame(dialog.getMessage().toString().indexOf(wantString), -1);
    }

    private void stubPathFileUrl(String fileUrl){
        PatchHelper patchHelper = mock(PatchHelper.class);
        stub(patchHelper.patch(getPatchFilePath())).toReturn(fileUrl);
        smartUpdates.setMockPatchHelper(patchHelper);
    }

    private String getPatchFilePath(){
        return getStorePath() + Constant.SAVE_FILENAME;
    }

    private String getNewApkPath(){
        return getStorePath() + Constant.NEWAPK_FILENAME;
    }

    private String getStorePath(){
        return ShadowEnvironment.getExternalStorageDirectory()
                + File.separator + Constant.SAVE_FOLDER + File.separator;
    }

    private void stubDownLoadUrl(String downloadUrl) throws Exception {
        ApiHelper api = mock(ApiHelper.class);
        stub(api.getDownloadUrl()).toReturn(downloadUrl);
        smartUpdates.setMockApiHelper(api);
    }

    private void startHasUpdateSituation() throws Exception {
        String response = "{\"message\":\"has_update\",\"information\":{\"application_id\":1,\"version_code\":2,\"version_name\":\"1.1\",\"description\":\"version 2\",\"apk_updated_at\":1380823151,\"apk_patch_size\":2206}}";
        Robolectric.setDefaultHttpResponse(200,response);
        startCheckUpdate();
    }

    private void startNoUpdateSituation() throws Exception{
        String response =  "{\"message\":\"no_update\",\"information\":null}";
        setResponseAndStartCheckUpdate(response);
    }

    private void startParseSituation() throws Exception{
        String response =  "{";
        setResponseAndStartCheckUpdate(response);
    }


    private void startApplicationNotExistedSituation() throws Exception{
        String response =  "{\"message\":\"application_not_existed\",\"information\":null}";
        setResponseAndStartCheckUpdate(response);
    }

    private void startCurrentVersionNotExistedSituation() throws Exception{
        String response =  "{\"message\":\"current_version_not_existed\",\"information\":null}";
        setResponseAndStartCheckUpdate(response);
    }

    private void startPackageNameNotMatchSituation() throws Exception{
        String response =  "{\"message\":\"package_name_not_match\",\"information\":null}";
        setResponseAndStartCheckUpdate(response);
    }

    private void setResponseAndStartCheckUpdate(String response) throws Exception {
        Robolectric.setDefaultHttpResponse(200,response);
        startCheckUpdate();
    }

    private void pressingOnDialogUpdateButton() throws Exception{
        Button updateButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
        Robolectric.clickOn(updateButton);
        assertTrue(!alertDialog.isShowing());
    }

    private void pressingDialogCancelButton() throws Exception{
        Button cancelButton = alertDialog.getButton(Dialog.BUTTON_NEGATIVE);
        Robolectric.clickOn(cancelButton);
        assertTrue(!alertDialog.isShowing());
    }

    private boolean isDialogShowing(){
        return ShadowAlertDialog.getLatestAlertDialog() != null;
    }

    private boolean isNotificationShowing(){
        Notification shadow = getNotification();
        return shadow != null;
    }

    private boolean isToastTextEqualTo(int stringResourceId){
        String title = ShadowToast.getTextOfLatestToast();
        String toastTitle = application.getString(stringResourceId);
        return title.equals(toastTitle);
    }

    private Notification getNotification(){
        NotificationManager notificationManager = (NotificationManager)
                Robolectric.application.getSystemService(Context.NOTIFICATION_SERVICE);
        ShadowNotificationManager shadow = Robolectric.shadowOf(notificationManager);
        return shadow.getNotification(null, Constant.NOTIFICATION_ID);
    }

    private void pauseBackgroundWork(){
        Robolectric.getBackgroundScheduler().pause();
    }

    private void contiuneBackgroundWork(){
        Robolectric.runBackgroundTasks();
    }

}
