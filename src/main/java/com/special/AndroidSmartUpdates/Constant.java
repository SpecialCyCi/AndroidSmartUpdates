package com.special.AndroidSmartUpdates;

import org.apache.http.protocol.HTTP;

/**
 * User: special
 * Date: 13-9-17
 * Time: 下午3:34
 * Mail: specialcyci@gmail.com
 */
public class Constant {

    public static final String FLAG_HAS_UPDATE = "has_update";

    public static final String FLAG_NO_UPDATE  = "no_update";

    public static final String FLAG_APPLICATION_NOT_EXISTED = "application_not_existed";

    public static final String FLAG_CURRENT_VERSION_NOT_EXISTED = "current_version_not_existed";

    public static final String FLAG_PACKAGENAME_NOT_MATCH = "package_name_not_match";

    public static final String FLAG_PARSE_ERROR = "parse_error";

    public static final String API_GET_UPDATE = "api/update/";

    public static final String API_GET_DOWNLOAD = "api/download/";

    public static final String DEFAULT_ENCODDING = HTTP.UTF_8;

    public static final String SAVE_FOLDER     = "SmartUpdates";

    public static final String SAVE_FILENAME   = "patcher.patch";

    public static final String NEWAPK_FILENAME = "patcher.apk";

    public static final int NOTIFICATION_ID = 8877;

}
