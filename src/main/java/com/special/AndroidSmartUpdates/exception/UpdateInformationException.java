package com.special.AndroidSmartUpdates.exception;

import android.content.Context;
import com.special.AndroidSmartUpdates.Constant;
import com.special.AndroidSmartUpdates.R;
import com.special.AndroidSmartUpdates.model.UpdateInformation;

/**
 * User: special
 * Date: 13-10-4
 * Time: 下午4:49
 * Mail: specialcyci@gmail.com
 */
public class UpdateInformationException extends Exception {

    protected final Context context;

    public UpdateInformationException(Context context) {
        super();
        this.context = context;
    }

    public static void isValidInformation(Context context, UpdateInformation information) throws UpdateInformationException {
        if (information.getMessage().equals(Constant.FLAG_PARSE_ERROR))
            throw new ParseErrorException(context);
        else if (information.getMessage().equals(Constant.FLAG_APPLICATION_NOT_EXISTED))
            throw new ApplicationNotExistedException(context);
        else if (information.getMessage().equals(Constant.FLAG_CURRENT_VERSION_NOT_EXISTED))
            throw new CurrentVersionNotExistedException(context);
        else if (information.getMessage().equals(Constant.FLAG_PACKAGENAME_NOT_MATCH))
            throw new PackageNameNotMatchException(context);
    }

    public static class ParseErrorException extends UpdateInformationException {

        public ParseErrorException(Context context) {
            super(context);
        }

        @Override
        public String getMessage() {
            return context.getString(R.string.toast_parse_error);
        }
    }

    public static class ApplicationNotExistedException extends UpdateInformationException {

        public ApplicationNotExistedException(Context context) {
            super(context);
        }

        @Override
        public String getMessage() {
            return context.getString(R.string.toast_application_not_existed);
        }
    }

    public static class CurrentVersionNotExistedException extends UpdateInformationException {

        public CurrentVersionNotExistedException(Context context) {
            super(context);
        }

        @Override
        public String getMessage() {
            return context.getString(R.string.toast_current_version_not_existed);
        }
    }

    public static class PackageNameNotMatchException extends UpdateInformationException {

        public PackageNameNotMatchException(Context context) {
            super(context);
        }

        @Override
        public String getMessage() {
            return context.getString(R.string.toast_package_name_not_match);
        }
    }
}
