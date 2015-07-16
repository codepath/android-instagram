package com.codepath.instagram.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramImage;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramUser;

import java.util.ArrayList;
import java.util.List;

public class InstagramClientDatabase extends SQLiteOpenHelper {
    private static final String TAG = "InstagramClientDatabase";

    // Database info
    private static final String DATABASE_NAME = "instagramClientDatabase";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_POSTS = "posts";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_IMAGES = "images";
    private static final String TABLE_COMMENTS = "comments";
    private static final String TABLE_POST_COMMENTS = "postComments";

    // Constraints
    private static final String CONSTRAINT_POST_COMMENTS_PK = "postComments_pk";

    // Posts table columns
    private static final String KEY_POST_ID = "id";
    private static final String KEY_POST_MEDIA_ID = "mediaId";
    private static final String KEY_POST_USER_ID_FK = "userId";
    private static final String KEY_POST_IMAGE_ID_FK = "imageId";
    private static final String KEY_POST_CAPTION = "caption";
    private static final String KEY_POST_LIKES_COUNT = "likesCount";
    private static final String KEY_POST_COMMENTS_COUNT = "commentsCount";
    private static final String KEY_POST_CREATED_TIME = "createdTime";

    // Users table columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PROFILE_PICTURE_URL = "profilePictureUrl";

    // Images table columns
    private static final String KEY_IMAGE_ID = "id";
    private static final String KEY_IMAGE_URL = "imageUrl";
    private static final String KEY_IMAGE_HEIGHT = "imageHeight";
    private static final String KEY_IMAGE_WIDTH = "imageWidth";

    // Comments table columns
    private static final String KEY_COMMENT_ID = "id";
    private static final String KEY_COMMENT_USER_ID_FK = "userId";
    private static final String KEY_COMMENT_TEXT = "text";
    private static final String KEY_COMMENT_CREATED_TIME = "createdTime";

    // Post Comments table columns
    private static final String KEY_POST_COMMENT_POST_ID_FK = "postId";
    private static final String KEY_POST_COMMENT_COMMENT_ID_FK = "commentId";

    // Singleton instance
    private static InstagramClientDatabase sInstance;

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private InstagramClientDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized InstagramClientDatabase getInstance(Context context) {
        if (sInstance == null) {
            // Use the application context, which will ensure that you
            // don't accidentally leak an Activity's context.
            // See this article for more information: http://bit.ly/6LRzfx
            sInstance = new InstagramClientDatabase(context.getApplicationContext());
        }

        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS +
                "(" +
                KEY_POST_ID + " INTEGER PRIMARY KEY," +
                KEY_POST_MEDIA_ID + " TEXT," +
                KEY_POST_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS + "," +
                KEY_POST_IMAGE_ID_FK + " INTEGER REFERENCES " + TABLE_IMAGES + "," +
                KEY_POST_CAPTION + " TEXT," +
                KEY_POST_LIKES_COUNT + " INTEGER," +
                KEY_POST_COMMENTS_COUNT + " INTEGER," +
                KEY_POST_CREATED_TIME + " INTEGER" +
                ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_NAME + " TEXT UNIQUE ON CONFLICT ROLLBACK," +
                KEY_USER_PROFILE_PICTURE_URL + " TEXT" +
                ")";

        String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES +
                "(" +
                KEY_IMAGE_ID + " INTEGER PRIMARY KEY," +
                KEY_IMAGE_URL + " TEXT," +
                KEY_IMAGE_HEIGHT + " INTEGER," +
                KEY_IMAGE_WIDTH + " INTEGER" +
                ")";

        String CREATE_COMMENTS_TABLE = "CREATE TABLE " + TABLE_COMMENTS +
                "(" +
                KEY_COMMENT_ID + " INTEGER PRIMARY KEY," +
                KEY_COMMENT_TEXT + " TEXT," +
                KEY_COMMENT_CREATED_TIME + " INTEGER," +
                KEY_COMMENT_USER_ID_FK + " INTEGER REFERENCES " + TABLE_USERS +
                ")";

        String CREATE_POST_COMMENTS_TABLE = "CREATE TABLE " + TABLE_POST_COMMENTS +
                "(" +
                KEY_POST_COMMENT_POST_ID_FK + " INTEGER REFERENCES " + TABLE_POSTS + "," +
                KEY_POST_COMMENT_COMMENT_ID_FK + " INTEGER REFERENCES " + TABLE_COMMENTS + "," +
                "constraint " + CONSTRAINT_POST_COMMENTS_PK +
                " PRIMARY KEY(" +
                KEY_POST_COMMENT_POST_ID_FK + "," +
                KEY_POST_COMMENT_COMMENT_ID_FK +
                ")" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_IMAGES_TABLE);
        db.execSQL(CREATE_COMMENTS_TABLE);
        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_POST_COMMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Implement this method
    }

    public void emptyAllTables() {
        // TODO: Implement this method to delete all rows from all tables
    }

    public void addInstagramPosts(List<InstagramPost> posts) {
        // TODO: Implement this method
        // Take a look at the helper methods addImage, addComment, etc as you implement this method
        // It's also a good idea to do this work in a transaction
    }

    // Poor man's "upsert".
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists, followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    private long addorUpdateUser(InstagramUser user) {
        if (user == null) {
            throw new IllegalArgumentException(String.format("Attemping to add a null user to %s", DATABASE_NAME));
        }

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.userName);
        values.put(KEY_USER_PROFILE_PICTURE_URL, user.profilePictureUrl);
        long userId = -1;

        // First try to update the user in case the user already exists in DB
        int rows = db.update(TABLE_USERS, values, KEY_USER_NAME + "= ?", new String[] {user.userName});

        // Check if update succeeded
        if (rows == 1) {
            // Get the primary key of the user we just updated
            String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                    KEY_USER_ID, TABLE_USERS, KEY_USER_NAME);

            Cursor usersCursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.userName)});
            try {
                if (usersCursor.moveToFirst()) {
                    userId = usersCursor.getInt(0);
                }
                // There should only be one user
                if (usersCursor.moveToNext()) {
                    Log.wtf(TAG, "Too many primary keys returned");
                }
            } catch (Exception e) {
                Log.wtf(TAG, "Error while trying to add or update user");
                e.printStackTrace();
            } finally {
                usersCursor.close();
            }
        } else {
            // user with this userName did not already exist, so insert new user
            userId = db.insert(TABLE_USERS, null ,values);
        }

        return userId;
    }

    private long addImage(InstagramImage image) {
        if (image == null) {
            throw new IllegalArgumentException(String.format("Attemping to add a null image to %s", DATABASE_NAME));
        }

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE_URL, image.imageUrl);
        values.put(KEY_IMAGE_HEIGHT, image.imageHeight);
        values.put(KEY_IMAGE_WIDTH, image.imageHeight);

        return db.insert(TABLE_IMAGES, null, values);
    }

    private long addComment(InstagramComment comment, long postId) {
        if (comment == null) {
            throw new IllegalArgumentException(String.format("Attemping to add a null comment to %s", DATABASE_NAME));
        }
        SQLiteDatabase db = getWritableDatabase();

        long commentUserId = addorUpdateUser(comment.user);

        ContentValues values = new ContentValues();
        values.put(KEY_COMMENT_TEXT, comment.text);
        values.put(KEY_COMMENT_USER_ID_FK, commentUserId);
        values.put(KEY_COMMENT_CREATED_TIME, comment.createdTime);

        long commentId = db.insert(TABLE_COMMENTS, null, values);
        addPostCommentMapping(postId, commentId);
        return commentId;
    }

    private long addPostCommentMapping(long postId, long commentId) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POST_COMMENT_COMMENT_ID_FK, commentId);
        values.put(KEY_POST_COMMENT_POST_ID_FK, postId);

        return db.insert(TABLE_POST_COMMENTS, null, values);
    }

    public List<InstagramPost> getAllInstagramPosts() {
        List<InstagramPost> posts = new ArrayList<>();

        String LEFT_OUTER_JOIN_FORMAT_STRING = "LEFT OUTER JOIN %s ON %s.%s = %s.%s";

        String userJoin = String.format(LEFT_OUTER_JOIN_FORMAT_STRING,
                TABLE_USERS, TABLE_POSTS, KEY_POST_USER_ID_FK, TABLE_USERS, KEY_USER_ID);

        String imageJoin = String.format(LEFT_OUTER_JOIN_FORMAT_STRING,
                TABLE_IMAGES, TABLE_POSTS, KEY_POST_IMAGE_ID_FK, TABLE_IMAGES, KEY_IMAGE_ID);

        String postsSelectQuery = "SELECT * FROM " + TABLE_POSTS + " " + userJoin + " " + imageJoin;

        String commentJoin = String.format(LEFT_OUTER_JOIN_FORMAT_STRING,
                TABLE_COMMENTS, TABLE_POST_COMMENTS, KEY_POST_COMMENT_COMMENT_ID_FK, TABLE_COMMENTS, KEY_COMMENT_ID);
        String commentUserJoin = String.format(LEFT_OUTER_JOIN_FORMAT_STRING,
                TABLE_USERS, TABLE_USERS, KEY_USER_ID, TABLE_COMMENTS, KEY_COMMENT_USER_ID_FK);

        String commentsSelectQuery = String.format("SELECT * FROM %s %s %s WHERE %s = ?",
                TABLE_POST_COMMENTS, commentJoin, commentUserJoin, KEY_POST_COMMENT_POST_ID_FK);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor postsCursor = db.rawQuery(postsSelectQuery, null);

        try {
            if (postsCursor.moveToFirst()) {
                do {
                    InstagramPost post = new InstagramPost();
                    post.mediaId = postsCursor.getString(postsCursor.getColumnIndexOrThrow(KEY_POST_MEDIA_ID));
                    post.caption = postsCursor.getString(postsCursor.getColumnIndexOrThrow(KEY_POST_CAPTION));
                    post.likesCount = postsCursor.getInt(postsCursor.getColumnIndexOrThrow(KEY_POST_LIKES_COUNT));
                    post.commentsCount = postsCursor.getInt(postsCursor.getColumnIndexOrThrow(KEY_POST_COMMENTS_COUNT));
                    post.createdTime = postsCursor.getLong(postsCursor.getColumnIndexOrThrow(KEY_POST_CREATED_TIME));

                    InstagramUser user = new InstagramUser();
                    user.userName = postsCursor.getString(postsCursor.getColumnIndexOrThrow(KEY_USER_NAME));
                    user.profilePictureUrl = postsCursor.getString(postsCursor.getColumnIndexOrThrow(KEY_USER_PROFILE_PICTURE_URL));
                    post.user = user;

                    InstagramImage image = new InstagramImage();
                    image.imageUrl = postsCursor.getString(postsCursor.getColumnIndexOrThrow(KEY_IMAGE_URL));
                    image.imageHeight = postsCursor.getInt(postsCursor.getColumnIndexOrThrow(KEY_IMAGE_HEIGHT));
                    image.imageWidth = postsCursor.getInt(postsCursor.getColumnIndexOrThrow(KEY_IMAGE_WIDTH));
                    post.image = image;

                    int key = postsCursor.getInt(0);

                    // Get all comments for this post
                    Cursor commentsCursor = db.rawQuery(commentsSelectQuery, new String[]{String.valueOf(key)});
                    try {
                        if (commentsCursor.moveToFirst()) {
                            do {
                                InstagramComment comment = new InstagramComment();
                                comment.text = commentsCursor.getString(commentsCursor.getColumnIndexOrThrow(KEY_COMMENT_TEXT));
                                comment.createdTime = commentsCursor.getLong(commentsCursor.getColumnIndexOrThrow(KEY_COMMENT_CREATED_TIME));

                                InstagramUser commentUser = new InstagramUser();
                                commentUser.userName = commentsCursor.getString(commentsCursor.getColumnIndexOrThrow(KEY_USER_NAME));
                                commentUser.profilePictureUrl = commentsCursor.getString(commentsCursor.getColumnIndexOrThrow(KEY_USER_PROFILE_PICTURE_URL));
                                comment.user = commentUser;

                                post.appendComment(comment);
                            } while (commentsCursor.moveToNext());
                        }
                    } catch (Exception e) {
                        Log.wtf(TAG, "Error while trying to get comments from database");
                        e.printStackTrace();
                    } finally {
                        commentsCursor.close();
                    }
                    posts.add(post);
                } while (postsCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.wtf(TAG, "Error while trying to get posts from database");
            e.printStackTrace();
        } finally {
            closeCursor(postsCursor);
        }

        return posts;
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
