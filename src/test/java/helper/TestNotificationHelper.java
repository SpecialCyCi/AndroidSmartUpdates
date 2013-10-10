package helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import com.special.AndroidSmartUpdates.Constant;
import com.special.AndroidSmartUpdates.R;
import com.special.AndroidSmartUpdates.helper.NotificationHelper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.junit.Test;
import org.robolectric.shadows.ShadowNotificationManager;

import static org.junit.Assert.*;

/**
 * User: special
 * Date: 13-9-17
 * Time: 下午6:53
 * Mail: specialcyci@gmail.com
 */
@RunWith(RobolectricTestRunner.class)
public class TestNotificationHelper {

    private NotificationHelper notificationHelper;

    @Before
    public void setUp(){
        ShadowActivity shadowActivity = new ShadowActivity();
        shadowActivity.onCreate(null);
        notificationHelper = new NotificationHelper(shadowActivity.getApplicationContext());
    }

    @Test
    public void testShowNotification(){
        notificationHelper.showNotification();
        assertTrue(isNotificationShowing());
    }

    @Test
    public void testIsRightLayout(){
        testShowNotification();
        int viewId = getNotification().contentView.getLayoutId();
        assertEquals(viewId, R.layout.notification_);
    }

    @Test
    public void testUpdateNotification(){
        testShowNotification();
        for (int progress = 0; progress <= 100 ; progress++){
            notificationHelper.updateProgress(progress);
            assertTrue(isNotificationShowing());
        }
    }

    @Test
    public void testDismissNotification(){
        notificationHelper.dismissNotification();
        assertFalse(isNotificationShowing());
    }

    private boolean isNotificationShowing(){
        Notification notification = getNotification();
        return notification != null;
    }

    private Notification getNotification(){
        NotificationManager notificationManager = (NotificationManager)
                Robolectric.application.getSystemService(Context.NOTIFICATION_SERVICE);
        ShadowNotificationManager shadow = Robolectric.shadowOf(notificationManager);
        return shadow.getNotification(null, Constant.NOTIFICATION_ID);
    }

}
