package com.fandean.photogallery;

import android.net.Uri;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan on 17-7-11.
 */

public class FlickrFetchr {
    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = "7c2b35fd017fdfc481f2159ae99c4f62";


    /**
     * API文档页： https://www.flickr.com/services/api/
     * REST 请求格式（request formats）
     * REST 是要使用的最簡單的要求格式 - 它是簡單的 HTTP GET 或 POST 動作。
     * The REST Endpoint URL is https://api.flickr.com/services/rest/
     */
    public List<GalleryItem> fetchItems(){
        List<GalleryItem> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                .buildUpon()
                .appendQueryParameter("method","flickr.photos.getRecent")
                .appendQueryParameter("api_key",API_KEY)
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                //如有小图片，也一并返回其URL
                .appendQueryParameter("extras","url_s")
                .build().toString();
            String jsonString = getUrlString(url);
            Logger.d("url: "+ url + "\n JsonSize: "+ jsonString.length());
//            Logger.json(jsonString);

            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items,jsonBody);
        } catch (IOException e) {
            Logger.e("Failed to fetch items",e);
        } catch (JSONException e) {
            Logger.e("Failed to parse JSON", e);
        }
        return items;
    }


    private void parseItems(List<GalleryItem> list, JSONObject jsonObject) throws JSONException {
        JSONObject photosJsonObject = jsonObject.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
        for (int i = 0; i < photoJsonArray.length(); i++){
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setCaption(photoJsonObject.getString("title"));
            item.setId(photoJsonObject.getString("id"));

            if (!photoJsonObject.has("url_s")){//不包含
                continue;//执行i++，跳出循环
            }
            item.setUrl(photoJsonObject.getString("url_s"));
            list.add(item);
        }
    }


    public byte[] getUrlBytes(String urlSpec) throws IOException {
        //下面两条语句都会抛出IO异常
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            //定义输入输出流
            InputStream in = connection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }

            int bytesRead = 0;
            byte[] buffere = new byte[1024];
            while ((bytesRead = in.read(buffere)) > 0) {
                out.write(buffere, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
}
