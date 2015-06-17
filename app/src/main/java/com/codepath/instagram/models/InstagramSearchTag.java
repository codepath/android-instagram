package com.codepath.instagram.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class InstagramSearchTag implements Serializable {
    public String tag;
    public int count;

    public static InstagramSearchTag fromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        InstagramSearchTag searchTag = new InstagramSearchTag();

        try {
            searchTag.tag = jsonObject.getString("name");
            searchTag.count = jsonObject.getInt("media_count");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return searchTag;
    }
}
