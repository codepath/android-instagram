package com.codepath.instagram.helpers;

import android.content.Context;

import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String TAG = "Utils";
    private static final NumberFormat numberFormat;

    static {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);
    }

    public static String formatNumberForDisplay(int number) {
        return numberFormat.format(number);
    }

    public static JSONObject loadJsonFromAsset(Context context, String fileName) throws IOException, JSONException {
        InputStream inputStream = context.getResources().getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        String line;
        StringBuilder builder = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }

        JSONObject result = new JSONObject(builder.toString());

        inputStream.close();
        bufferedReader.close();

        return result;
    }

    public static List<InstagramPost> decodePostsFromJson(JSONObject jsonObject) throws JSONException {
        List<InstagramPost> posts = new ArrayList<>();
        JSONArray postsJson = jsonObject.optJSONArray("data");
        if (postsJson != null) {
            for (int i = 0; i < postsJson.length(); i++) {
                InstagramPost instagramPost = InstagramPost.fromJson(postsJson.getJSONObject(i));
                posts.add(instagramPost);
            }
        }
        return posts;
    }

    private static List<InstagramComment> decodeCommentsFromJson(JSONObject jsonObject) throws JSONException {
        List<InstagramComment> comments = new ArrayList<>();
        JSONArray commentsJson = jsonObject.optJSONArray("data");
        if (commentsJson != null) {
            for (int i = 0; i < commentsJson.length(); i++) {
                InstagramComment instagramComment = InstagramComment.fromJson(commentsJson.getJSONObject(i));
                comments.add(instagramComment);
            }
        }
        return comments;
    }
}
