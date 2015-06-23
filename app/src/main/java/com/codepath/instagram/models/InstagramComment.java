package com.codepath.instagram.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InstagramComment implements Serializable {
    public InstagramUser user;
    public String text;
    public long createdTime;

    public static InstagramComment fromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        InstagramComment comment = new InstagramComment();

        try {
            comment.user = InstagramUser.fromJson(jsonObject.getJSONObject("from"));
            comment.text = jsonObject.getString("text");
            comment.createdTime = jsonObject.getLong("created_time");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return comment;
    }

    public static List<InstagramComment> fromJson(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        List<InstagramComment> comments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            InstagramComment comment = InstagramComment.fromJson(jsonObject);
            if (comment != null) {
                comments.add(comment);
            }
        }
        return comments;
    }
}
