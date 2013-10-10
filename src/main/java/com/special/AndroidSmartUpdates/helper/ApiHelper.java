package com.special.AndroidSmartUpdates.helper;

import android.util.Base64;
import com.special.AndroidSmartUpdates.Config;
import com.special.AndroidSmartUpdates.Constant;
import com.special.AndroidSmartUpdates.model.UpdateInformation;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * User: special
 * Date: 13-9-16
 * Time: 下午2:42
 * Mail: specialcyci@gmail.com
 */
public class ApiHelper {

    private String packageName;
    private int currentVersionCode;

    public void setPackageName(String packageName) {
        packageName = Base64.encodeToString(packageName.getBytes(),Base64.URL_SAFE | Base64.NO_WRAP);
        this.packageName = packageName;
    }

    public void setCurrentVersionCode(int currentVersionCode) {
        this.currentVersionCode = currentVersionCode;
    }

    /**
     * the api to check if there was any update;
     *
     * @return
     */
    public UpdateInformation getUpdateInformation(){
        String url = getUpdateUrl();
        String response = getUpdateInformationResponse(url);
        return UpdateInformation.parseFromJsonString(response);
    }

    /**
     * a helper method to get download url;
     *
     * @return
     */
    public String getDownloadUrl(){
        StringBuilder url = new StringBuilder(Config.getServerAddress());
        url.append(Constant.API_GET_DOWNLOAD);
        url.append(appendMessage());
        return url.toString();
    }

    private String getUpdateUrl(){
        StringBuilder url = new StringBuilder(Config.getServerAddress());
        url.append(Constant.API_GET_UPDATE);
        url.append(appendMessage());
        return url.toString();
    }


    private String appendMessage(){
        StringBuilder builder = new StringBuilder();
        builder.append(Config.getApplicationId() + "/");
        builder.append(packageName + "/");
        builder.append(currentVersionCode);
        return builder.toString();
    }

    public String getUpdateInformationResponse(String url) {
        try {
            return httpGetWithUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String httpGetWithUrl(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
        return EntityUtils.toString(httpResponse.getEntity(), Constant.DEFAULT_ENCODDING);
    }
}
