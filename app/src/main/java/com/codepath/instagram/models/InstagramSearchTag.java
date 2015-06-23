package com.codepath.instagram.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public static List<InstagramSearchTag> fromJson(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        List<InstagramSearchTag> searchTags = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            InstagramSearchTag searchTag = InstagramSearchTag.fromJson(jsonObject);
            if (searchTag != null) {
                searchTags.add(searchTag);
            }
        }
        return searchTags;
    }
}
