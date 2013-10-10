package com.special.AndroidSmartUpdates.impl;

/**
 * User: special
 * Date: 13-9-17
 * Time: 下午3:42
 * Mail: specialcyci@gmail.com
 */
public interface DownloadListener {

    public void onDownloading(int progress);

    public void onDownloadComplete(String filePath);

}
