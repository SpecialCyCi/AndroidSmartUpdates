package helper;

import android.util.Base64;
import com.special.AndroidSmartUpdates.Config;
import com.special.AndroidSmartUpdates.helper.ApiHelper;
import com.special.AndroidSmartUpdates.model.PatchInformation;
import com.special.AndroidSmartUpdates.model.UpdateInformation;
import org.apache.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: special
 * Date: 13-9-16
 * Time: 下午2:43
 * Mail: specialcyci@gmail.com
 */
@RunWith(RobolectricTestRunner.class)
public class TestApiHelper {

    private ApiHelper api;
    private String server = "http://192.168.1.107";
    private int currentVersionCode = 1;
    private int applicationId = 2;

    @Before
    public void setUp() throws Exception{
        Config.setServerAddress(server);
        Config.setApplicationId(applicationId);
    }

    @Before
    public void setUpApi() throws Exception{
        api = new ApiHelper();
        api.setPackageName(Robolectric.getShadowApplication().getPackageName());
        api.setCurrentVersionCode(currentVersionCode);
    }

    @Before
    public void setUpResponse() throws Exception{
        String parseString = "{\"message\":\"has_update\",\"information\":{\"application_id\":1,\"version_code\":2,\"version_name\":\"1.1\",\"description\":\"version 2\",\"apk_updated_at\":1380823151,\"apk_patch_size\":2206}}";
        Robolectric.setDefaultHttpResponse(1, parseString);
    }

    @Test
    public void testGetUpdateInformation() throws Exception{
        UpdateInformation information = getUpdateInformation();
        isRequestUrlRight(getRequestUrl());
        checkParamsRightWithAllParams(information);
    }

    @Test
    public void testGetDownloadUrl() throws Exception{
        String url = api.getDownloadUrl();
        isRequestUrlRight(url);
    }

    private UpdateInformation getUpdateInformation(){
        return api.getUpdateInformation();
    }

    private void isRequestUrlRight(String url){
        String wantUrl = "/" + applicationId + "/" + getBase64PackageName() + "/" + currentVersionCode;
        assertTrue(url.contains(server));
        assertTrue(url.contains(wantUrl));
    }

    private String getPackageName(){
        return Robolectric.getShadowApplication().getPackageName();
    }

    private String getBase64PackageName(){
        return Base64.encodeToString(getPackageName().getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
    }

    private String getRequestUrl(){
        HttpRequest lastRequest = Robolectric.getLatestSentHttpRequest();
        return lastRequest.getRequestLine().getUri();
    }

    private void checkParamsRightWithAllParams(UpdateInformation information){
        PatchInformation patchInfor = information.getPatchInformation();
        assertTrue(information.isHasUpdate());
        assertEquals(patchInfor.getDescription(),"version 2");
    }

}
