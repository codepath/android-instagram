package com.codepath.instagram.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InstagramImage implements Serializable {
    public String imageUrl;
    public int imageHeight;
    public int imageWidth;

    public static InstagramImage fromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        InstagramImage image = new InstagramImage();

        try {
            image.imageUrl = jsonObject.getString("url");
            image.imageHeight = jsonObject.getInt("height");
            image.imageWidth = jsonObject.getInt("width");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return image;
    }

    public static List<InstagramImage> fromJson(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        List<InstagramImage> images = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            InstagramImage image = InstagramImage.fromJson(jsonObject);
            if (image != null) {
                images.add(image);
            }
        }
        return images;
    }
}
