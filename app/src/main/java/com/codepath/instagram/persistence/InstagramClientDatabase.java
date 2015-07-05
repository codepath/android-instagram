package com.codepath.instagram.persistence;

public class InstagramClientDatabase {
    private static final String TAG = "InstagramClientDatabase";

    private static final String DATABASE_NAME = "instagramClientDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_POSTS = "posts";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_IMAGES = "images";
    private static final String TABLE_COMMENTS = "comments";
    private static final String TABLE_POST_COMMENTS = "postComments";

    private static final String CONSTRAINT_POST_COMMENTS_PK = "postComments_pk";

    private static final String KEY_ID = "_id";

    // Posts table columns
    private static final String KEY_POST_MEDIA_ID = "mediaId";
    private static final String KEY_POST_USER_ID_FK = "userId";
    private static final String KEY_POST_IMAGE_ID_FK = "imageId";
    private static final String KEY_POST_CAPTION = "caption";
    private static final String KEY_POST_LIKES_COUNT = "likesCount";
    private static final String KEY_POST_COMMENTS_COUNT = "commentsCount";
    private static final String KEY_POST_CREATED_TIME = "createdTime";

    // Users table columns
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PROFILE_PICTURE_URL = "profilePictureUrl";

    // Images table columns
    private static final String KEY_IMAGE_URL = "imageUrl";
    private static final String KEY_IMAGE_HEIGHT = "imageHeight";
    private static final String KEY_IMAGE_WIDTH = "imageWidth";

    // Comments table columns
    private static final String KEY_COMMENT_USER_ID_FK = "userId";
    private static final String KEY_COMMENT_TEXT = "text";
    private static final String KEY_COMMENT_CREATED_TIME = "createdTime";

    // Post Comments table columns
    private static final String KEY_POST_COMMENT_POST_ID_FK = "postId";
    private static final String KEY_POST_COMMENT_COMMENT_ID_FK = "commentId";


    // ... add code ...
}
