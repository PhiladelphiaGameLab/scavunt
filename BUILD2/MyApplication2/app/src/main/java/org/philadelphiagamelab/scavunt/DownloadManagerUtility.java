package org.philadelphiagamelab.scavunt;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/*
 * Utility for using DownloadManager - Downloads file from a given url and saves it within the
 * application's external files directory with the given name
 * Returns a unique long id  which can be compared to DownloadManager.ACTION_DOWNLOAD_COMPLETE
 * broadcasts made by the DownloadManager to confirm that needed files have completed downloading.
 * If file is already loaded on device no new download is made and null is returned
 */
public class DownloadManagerUtility {
    public static Long useDownloadManager(String url, String name, Context context) {

        File path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file= new File(path, name);

        if(file.exists()){
            Log.d("Exists: ", file.toString());
            return null;
        }
        else {
            DownloadManager downloadManager;
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri requestUri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(requestUri);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            //to be displayed in notifications (if enabled)
            request.setTitle(R.string.app_name + "Downloading: " + name);
            //to be displayed in notifications (if enabled)
            request.setDescription("In progress");
            //Local destination for the downloaded file to a path within the application's external files directory
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, name);

            //Enqueue a new download and return the referenceId
            long downloadReference = downloadManager.enqueue(request);
            return downloadReference;
        }
    }

    public static String getFilePath(String url, String name, Context context) {

        File path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, name);

        return file.getAbsolutePath();
    }
}
