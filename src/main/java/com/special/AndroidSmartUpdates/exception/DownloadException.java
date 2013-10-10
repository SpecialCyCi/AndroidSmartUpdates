package com.special.AndroidSmartUpdates.exception;

/**
 * User: special
 * Date: 13-9-16
 * Time: 下午6:38
 * Mail: specialcyci@gmail.com
 */
public class DownloadException extends Exception {

    private final String message;

    public DownloadException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
