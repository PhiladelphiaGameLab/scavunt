package org.philadelphiagamelab.scavunt;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by aaronmsegal on 8/13/14.
 */
public class ServerInterface {

    private static InputStream inputStream = null;
    private static JSONObject jsonObject = null;
    private static String json = "";

    public static enum Method {
        POST, GET
    }

    //returns JSONObject from a HTTP POST or GET
    public JSONObject makeHttpRequest(String url, Method method, List<NameValuePair> parameters) {

        //Http Request
        try {
            switch (method) {
                case POST:
                    //Nothing yet
                    break;
                case GET:
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    String paramString = URLEncodedUtils.format(parameters, "utf-8");
                    url += "?" + paramString;

                    //Debug url
                    Log.d("URL:", url);

                    HttpGet httpGet = new HttpGet(url);

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    inputStream = httpEntity.getContent();
                    break;
                default:
                    Log.e("Invalid Method", "Invalid Method");
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get json string
        try {
            /*
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, "iso-8859-1"), 8);
            json = reader.readLine();
            Log.d ("JSON : ", json);
            */
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            json = sb.toString();
            Log.d ("JSON : ", json);

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try to parse the string to a JSON object
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data:" + e.toString());
        }

        return  jsonObject;
    }
}
