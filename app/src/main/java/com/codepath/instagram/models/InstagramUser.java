package com.codepath.instagram.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class InstagramUser implements Serializable {
    public String userName;
    public String firstName;
    public String lastName;
    public String profilePictureUrl;
    public String userId;

    public static InstagramUser fromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        InstagramUser user = new InstagramUser();

        try {
            user.userId = jsonObject.getString("id");
            user.userName = jsonObject.getString("username");
            user.firstName = jsonObject.optString("first_name", "");
            user.lastName = jsonObject.optString("last_name", "");
            user.profilePictureUrl = jsonObject.getString("profile_picture");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }
}
