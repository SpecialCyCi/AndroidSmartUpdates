package com.special.AndroidSmartUpdates.model;

import android.util.Log;
import com.special.AndroidSmartUpdates.Constant;
import com.special.AndroidSmartUpdates.helper.SystemHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * the model of updateInformation,to save the
 * information which get from the server
 *
 * User: special
 * Date: 13-9-16
 * Time: 下午3:22
 * Mail: specialcyci@gmail.com
 */
public class UpdateInformation {

    private String message;
    private PatchInformation patchInformation;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PatchInformation getPatchInformation() {
        return patchInformation;
    }

    public void setPatchInformation(PatchInformation patchInformation) {
        this.patchInformation = patchInformation;
    }

    public boolean isHasUpdate() {
        return this.message.equals(Constant.FLAG_HAS_UPDATE);
    }

    public static UpdateInformation parseFromJsonString(String jsonString){
        try {
            return parseProcessing(jsonString);
        } catch (Exception e) {
//            Log.e("parse error", jsonString);
            return getParseErrorInformation();
        }
    }

    private static UpdateInformation getParseErrorInformation(){
        UpdateInformation information = new UpdateInformation();
        information.setMessage(Constant.FLAG_PARSE_ERROR);
        return information;
    }

    private static UpdateInformation parseProcessing(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        UpdateInformation information = new UpdateInformation();
        information.message = jsonObject.getString("message");
        information.patchInformation = PatchInformation.parseFrom(jsonObject.getString("information"));
        return information;
    }

}
