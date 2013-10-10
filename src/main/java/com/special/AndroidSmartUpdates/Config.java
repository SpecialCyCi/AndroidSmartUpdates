package com.special.AndroidSmartUpdates;

/**
 * User: special
 * Date: 13-9-16
 * Time: 下午2:21
 * Mail: specialcyci@gmail.com
 */
public class Config {

    private static String serverAddress;
    private static int    applicationId;

    public static void setServerAddress(String serverAddress) {
        if (isRightEnddingAddress(serverAddress)){
            Config.serverAddress = serverAddress;
        }else{
            Config.serverAddress = addAddressEndding(serverAddress);
        }
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    private static boolean isRightEnddingAddress(String serverAddress){
        return serverAddress.endsWith("/");
    }

    private static String addAddressEndding(String serverAddress){
        return serverAddress + "/";
    }

    public static int getApplicationId() {
        return applicationId;
    }

    public static void setApplicationId(int applicationId) {
        Config.applicationId = applicationId;
    }

}
