package com.codepath.instagram.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class InstagramPost implements Serializable {
    public String mediaId;
    public InstagramUser user;
    public InstagramImage image;
    public ArrayList<InstagramComment> comments;
    public String caption;
    public int likesCount;
    public int commentsCount;
    public long createdTime;

    public void appendComment(InstagramComment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

    public static InstagramPost fromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        InstagramPost post = new InstagramPost();

        try {
            post.mediaId = jsonObject.getString("id");
            post.user = InstagramUser.fromJson(jsonObject.getJSONObject("user"));

            JSONObject imagesJson = jsonObject.optJSONObject("images");
            if (imagesJson != null) {
                post.image = InstagramImage.fromJson(imagesJson.optJSONObject("standard_resolution"));
            }

            post.caption = jsonObject.optJSONObject("caption") != null ?
                    jsonObject.getJSONObject("caption").getString("text") : null;

            JSONObject likesJson = jsonObject.optJSONObject("likes");
            post.likesCount = likesJson != null ? likesJson.optInt("count") : 0;

            JSONObject commentsJson = jsonObject.optJSONObject("comments");
            post.commentsCount = commentsJson != null ? commentsJson.optInt("count") : 0;
            post.comments = commentsJson != null ? InstagramComment.fromJson(commentsJson.optJSONArray("data")) : null;

            post.createdTime = jsonObject.getLong("created_time");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return post;
    }
}
