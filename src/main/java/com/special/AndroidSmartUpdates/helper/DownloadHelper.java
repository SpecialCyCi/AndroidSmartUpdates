package com.special.AndroidSmartUpdates.helper;

import android.content.Context;
import android.os.Environment;
import com.special.AndroidSmartUpdates.Constant;
import com.special.AndroidSmartUpdates.exception.DownloadException;
import com.special.AndroidSmartUpdates.exception.SDCardNotExistedException;
import com.special.AndroidSmartUpdates.impl.DownloadListener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: special
 * Date: 13-9-16
 * Time: 下午6:30
 * Mail: specialcyci@gmail.com
 */
public class DownloadHelper {

    private final Context context;
    private int fileSize;
    private File saveFile;
    private InputStream inputStream;
    private FileOutputStream fileOutputStream;
    private DownloadListener downloadListener;
    private int downFileSize;
    private int lastProgress;

    public DownloadHelper(Context context) {
        this.context = context;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void startDownload(String url) throws SDCardNotExistedException, DownloadException {
        if (!isMountedSdCard()) throw new SDCardNotExistedException();

        try {
            downloadProcessing(url);
        } catch (MalformedURLException e) {
            throw new DownloadException(e.getMessage());
        } catch (IOException e) {
            throw new DownloadException(e.getMessage());
        }
    }

    private void downloadProcessing(String url) throws IOException {
        createDownloadRelative(url);
        writeAndDownloadFile();
        closeDownloadRelative();
    }

    private void createDownloadRelative(String url) throws IOException {
        saveFile = buildSaveFile();
        HttpURLConnection conn = buildHttpUrlConnection(url);
        if (!isConnectSuccess(conn)) throw new IOException();
        fileSize = conn.getContentLength();
        inputStream = conn.getInputStream();
        fileOutputStream = new FileOutputStream(saveFile);
    }

    private void writeAndDownloadFile() throws IOException {
        byte[] buffer = new byte[1024];
        int i = 0;
        lastProgress = -1;
        downFileSize = 0;
        while ((i = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, i);
            downFileSize = downFileSize + i;
            int progress = calculateProgress(downFileSize);
            transferProgressToListener(progress);
        }
    }

    private void transferProgressToListener(int progress){
        if(downloadListener == null)  return;
        if (isDownLoadCompleteProgess(progress)) {
            downloadListener.onDownloadComplete(saveFile.getPath());
        }else if (isProgressChange(progress)) {
            downloadListener.onDownloading(progress);
        }
    }

    private boolean isProgressChange(int progress)  {
        if (lastProgress != progress){
            lastProgress = progress;
            return true;
        }
        return false;
    }

    private boolean isDownLoadCompleteProgess(int progress)  {
        return progress == 100;
    }

    private int calculateProgress(int downFileSize){
        return (int) (downFileSize * 100.0 / fileSize);
    }

    private void closeDownloadRelative() throws IOException {
        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();
    }

    private File buildSaveFolder(){
        File folder = new File(Environment.getExternalStorageDirectory()
                + File.separator + Constant.SAVE_FOLDER );
        if (!folder.exists()) folder.mkdir();
        return folder;
    }

    private File buildSaveFile(){
        File saveFileFolder = buildSaveFolder();
        return new File(saveFileFolder, Constant.SAVE_FILENAME);
    }

    private HttpURLConnection buildHttpUrlConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        return (HttpURLConnection) url.openConnection();
    }

    private boolean isConnectSuccess(HttpURLConnection conn) throws IOException {
        return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
    }

    private boolean isMountedSdCard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


}
