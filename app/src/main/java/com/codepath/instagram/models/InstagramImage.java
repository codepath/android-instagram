package com.codepath.instagram.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

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
}
