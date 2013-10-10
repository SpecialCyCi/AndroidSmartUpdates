import com.special.AndroidSmartUpdates.Config;
import com.special.AndroidSmartUpdates.SmartUpdates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import static org.junit.Assert.*;

/**
 * User: special
 * Date: 13-9-16
 * Time: 上午10:14
 * Mail: specialcyci@gmail.com
 */
@RunWith(RobolectricTestRunner.class)
public class TestConfig {

    @Test
    public void testSetServerAddressWithRightEndding() throws Exception{
        String rightEnddingServerAddress = "http://192.168.1.107/";
        testIsRightAddress(rightEnddingServerAddress);
        assertEquals(Config.getServerAddress(),rightEnddingServerAddress);
    }

    @Test
    public void testSetServerAddressWithWrongEndding() throws Exception{
        String wrongEnddingServerAddress = "http://192.168.1.107/update";
        testIsRightAddress(wrongEnddingServerAddress);
        assertEquals(Config.getServerAddress(),wrongEnddingServerAddress + "/");
    }

    private void testIsRightAddress(String serverAdress){
        Config.setServerAddress(serverAdress);
        assertTrue(Config.getServerAddress().toString().endsWith("/"));
    }

}
