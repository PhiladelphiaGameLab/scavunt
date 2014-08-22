package org.philadelphiagamelab.scavunt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViewStringMap =
            Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;

    public ImageLoader(Context context) {
        fileCache=new FileCache(context);
        executorService= Executors.newFixedThreadPool(5);
    }

    public void DisplayImage(String url, ImageView imageView)
    {
        imageViewStringMap.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null) {
            imageView.setImageBitmap(bitmap);
        }
        else
        {
            queuePhoto(url, imageView);
        }
    }

    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad photoToLoad=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(photoToLoad));
    }

    private Bitmap getBitmap(String url)
    {
        File file = fileCache.getFile(url);

        //from SD cache
        Bitmap sdBitmap = decodeFile(file);
        if(sdBitmap!=null) {
            return sdBitmap;
        }

        //from web, then copy to file
        try {
            Bitmap webBitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)imageUrl.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setInstanceFollowRedirects(true);
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = new FileOutputStream(file);
            LoadingUtilities.CopyStream(inputStream, outputStream);
            outputStream.close();
            webBitmap = decodeFile(file);
            return webBitmap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File file){
        try {
            //decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, options);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=256;
            int widthTemp = options.outWidth;
            int heightTemp = options.outHeight;
            int scale=1;
            while(true){
                if(((widthTemp / 2 ) < REQUIRED_SIZE) || ((heightTemp / 2 ) < REQUIRED_SIZE))
                    break;
                widthTemp /= 2;
                heightTemp /= 2;
                scale *= 2;
            }

            //decode with inSampleSize
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(file), null, options2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String urlIn, ImageView imageViewIn){
            url = urlIn;
            imageView = imageViewIn;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if(imageViewReused(photoToLoad)) {
                return;
            }
            Bitmap bitmap = getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bitmap);
            if(imageViewReused(photoToLoad)) {
                return;
            }
            BitmapDisplayer bitmapDisplayer = new BitmapDisplayer(bitmap, photoToLoad);
            Activity activity = (Activity)photoToLoad.imageView.getContext();
            activity.runOnUiThread(bitmapDisplayer);
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag = imageViewStringMap.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad)) {
                return;
            }
            if(bitmap!=null) {
                photoToLoad.imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
}
