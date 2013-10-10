package model;

import com.special.AndroidSmartUpdates.model.PatchInformation;
import com.special.AndroidSmartUpdates.model.UpdateInformation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * User: special
 * Date: 13-9-16
 * Time: 下午3:27
 * Mail: specialcyci@gmail.com
 */
@RunWith(RobolectricTestRunner.class)
public class TestUpdateInformationModel {

    @Test
    public void testModelParseWithHasUpdate() throws Exception{
        String parseString = "{\"message\":\"has_update\",\"information\":{\"application_id\":1,\"version_code\":2,\"version_name\":\"1.1\",\"description\":\"version 2\",\"apk_updated_at\":1380823151,\"apk_patch_size\":2206}}";
        UpdateInformation information = UpdateInformation.parseFromJsonString(parseString);
        isUpdateInformationRight(information);
    }

    private void isUpdateInformationRight(UpdateInformation information){
        PatchInformation patchInfor = information.getPatchInformation();
        assertEquals(information.getMessage(),"has_update");
        assertEquals(patchInfor.getApplicationId(),1);
        assertEquals(patchInfor.getVersionCode(),2);
        assertEquals(patchInfor.getVersionName(),"1.1");
        assertEquals(patchInfor.getDescription(),"version 2");
        assertEquals(patchInfor.getApkUpdatedAt(),1380823151);
        assertEquals(patchInfor.getApkPatchSize(),2206);
    }

    @Test
    public void testModelParseWithNonePatchInformation() throws Exception{
        String parseString = "{\"message\":\"no_update\",\"information\":null}";
        UpdateInformation information = UpdateInformation.parseFromJsonString(parseString);
        assertEquals(information.getMessage(),"no_update");
        assertNull(information.getPatchInformation());
    }

    @Test
    public void testModelParseWithErrorMessage() throws Exception{
        String parseString = "{\"message1\":\"no_update\",\"information2\":null}";
        testParseError(parseString);
    }

    @Test
    public void testModelParseWithErrorFormat() throws Exception{
        String parseString = "123123";
        testParseError(parseString);
    }

    @Test
    public void testModelParseWithEmptyString() throws Exception{
        String parseString = "";
        testParseError(parseString);
    }


    private void testParseError(String parseString){
        UpdateInformation information = UpdateInformation.parseFromJsonString(parseString);
        assertEquals(information.getMessage(),"parse_error");
        assertNull(information.getPatchInformation());
    }
}
