package helper;

import com.special.AndroidSmartUpdates.helper.SystemHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import static org.junit.Assert.*;

/**
 * User: special
 * Date: 13-9-18
 * Time: 上午8:31
 * Mail: specialcyci@gmail.com
 */
@RunWith(RobolectricTestRunner.class)
public class TestSystemHelper {

    @Test
    public void testGetApkFilePath(){
        ShadowActivity activity = new ShadowActivity();
        activity.onCreate(null);
        assertNotNull(SystemHelper.getApkFilePath(activity.getApplication()));
    }

}
