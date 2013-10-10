package helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import com.special.AndroidSmartUpdates.Constant;
import com.special.AndroidSmartUpdates.exception.DownloadException;
import com.special.AndroidSmartUpdates.exception.SDCardNotExistedException;
import com.special.AndroidSmartUpdates.helper.DownloadHelper;
import com.special.AndroidSmartUpdates.impl.DownloadListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowEnvironment;

import java.io.File;

import static org.junit.Assert.*;

/**
 * User: special
 * Date: 13-9-16
 * Time: 下午6:31
 * Mail: specialcyci@gmail.com
 */
@RunWith(RobolectricTestRunner.class)
public class TestDownloadHelper {

    private DownloadHelper downloadHelper;
    private ShadowConnectivityManager shadowConnectivityManager;

    @Before
    public void setUp() throws Exception{
        downloadHelper = new DownloadHelper(null);
        ConnectivityManager connectivityManager = (ConnectivityManager)
                Robolectric.application.getSystemService(Context.CONNECTIVITY_SERVICE);
        shadowConnectivityManager =  Robolectric.shadowOf(connectivityManager);
    }

    @Test
    public void testStartDownloadWithNoSDCard(){
        setUpNoSDCardEnvironment();
        try {
            downloadHelper.startDownload("http://www.baidu.com/");
            fail("SDCard Expection has not catch");
        } catch (SDCardNotExistedException e) {
            e.printStackTrace();
        } catch (DownloadException e) {
            fail();
        }
    }

    private void setUpSDCardEnvironment(){
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED);
    }

    private void setUpNoSDCardEnvironment(){
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_UNMOUNTED);
    }

    //"http://dldir1.qq.com/qqfile/qq/QQ2013/QQ2013SP2/8180/QQ2013SP2.exe"
    @Test
    public void testStartDownloadWithSDCard() {
        setUpSDCardEnvironment();
        try {
            downloadHelper.startDownload("http://www.baidu.com/");
        } catch (SDCardNotExistedException e) {
            fail("SDCard has Existed.");
        } catch (DownloadException e) {
            fail();
        }
    }

    @Test
    public void testStartDownloadWithWrongUrl() throws SDCardNotExistedException {
        setUpSDCardEnvironment();
        try {
            downloadHelper.startDownload("http://255.255.255.255/");
            fail();
        } catch (DownloadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStartDownloadWithOneFile() throws SDCardNotExistedException, DownloadException {
        setUpSDCardEnvironment();
        downloadHelper.startDownload("http://www.baidu.com/");
        assertDownloadFilesExisted(1);
    }

    @Test
    public void testStartDownloadWithTwoFile() throws SDCardNotExistedException, DownloadException {
        setUpSDCardEnvironment();
        downloadHelper.startDownload("http://www.baidu.com/");
        downloadHelper.startDownload("http://www.baidu.com/");
        assertDownloadFilesExisted(1);
    }

    @Test
    public void testDownloadListenerAvailable() throws Exception{
        setUpSDCardEnvironment();
        downloadHelper.setDownloadListener(downloadListener);
        downloadHelper.startDownload("http://www.baidu.com/");
        assertDownloadFilesExisted(1);
    }

    private void assertDownloadFilesExisted(int fileLengthOfFolder){
        File folder = new File(ShadowEnvironment.getExternalStorageDirectory()
                + File.separator + Constant.SAVE_FOLDER );
        assertTrue(folder.exists());
        assertTrue(folder.isDirectory());
        assertEquals(folder.listFiles().length,fileLengthOfFolder);
    }

    private DownloadListener downloadListener = new DownloadListener() {

        @Override
        public void onDownloading(int progress) {
            System.out.println("progress: " + progress);
            assertNotSame(progress,0);
        }

        @Override
        public void onDownloadComplete(String filePath) {
            System.out.println("download successfully: " + filePath);
            assertNotNull(filePath);
        }
    };

}
