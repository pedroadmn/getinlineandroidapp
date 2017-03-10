package com.androidapp.getinline.util;

import android.content.Context;
import android.widget.Toast;

import com.androidapp.getinline.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static final String EMPTY = "";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    public static final String GOOGLE_USER_CONTENT_KEY = "617151987057-es670jj5qfiuphb7u64gvrup3c8kdvum.apps.googleusercontent.com";
    public static final String FACEBOOK = "facebook";
    public static final String GOOGLE = "google";
    public static final String NAME_KEY = "name";
    public static final String EMAIL_KEY = "email";
    public static final String USERS_KEY_DB = "users";
    public static final String PROFILE_PERMISSION = "public_profile";
    public static final String FRIENDS_PERMISSION = "user_friends";
    public static final String EMAIL_PERMISSION = "email";

    public static boolean validateEmail(Context context, String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher;
        boolean result = true;
        matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            Toast.makeText(context, context.getResources().getString(R.string.msg_email_wrong_format), Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }

    public static boolean validateEqualPassword(Context context, String pass1Str, String pass2Str) {
        boolean result = true;
        if (!pass1Str.equals(pass2Str)) {
            Toast.makeText(context, context.getResources().getString(R.string.msg_dif_password), Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }


}
