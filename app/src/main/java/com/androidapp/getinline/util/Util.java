package com.androidapp.getinline.util;

import android.content.Context;
import android.widget.Toast;

import com.androidapp.getinline.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import za.co.riggaroo.materialhelptutorial.TutorialItem;

public class Util {
    /**
     * Constant EMPTY
     */
    public static final String EMPTY = "";

    /**
     * Constant EMAIL_PATTERN
     */
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

    /**
     * Constant GOOGLE_USER_CONTENT_KEY
     */
    public static final String GOOGLE_USER_CONTENT_KEY = "617151987057-es670jj5qfiuphb7u64gvrup3c8kdvum.apps.googleusercontent.com";

    /**
     * Constant FACEBOOK
     */
    public static final String FACEBOOK = "facebook";

    /**
     * Constant GOOGLE
     */
    public static final String GOOGLE = "google";

    /**
     * Constant NAME_KEY
     */
    public static final String NAME_KEY = "name";

    /**
     * Constant EMAIL_KEY
     */
    public static final String EMAIL_KEY = "email";

    /**
     * Constant USERS_KEY_DB
     */
    public static final String USERS_KEY_DB = "users";

    /**
     * Constant PROFILE_PERMISSION
     */
    public static final String PROFILE_PERMISSION = "public_profile";

    /**
     * Constant FRIENDS_PERMISSION
     */
    public static final String FRIENDS_PERMISSION = "user_friends";

    /**
     * Constant EMAIL_PERMISSION
     */
    public static final String EMAIL_PERMISSION = "email";

    /**
     * Constant KEY_USER
     */
    public static final String KEY_USER = "key_user";

    /**
     * Constant KEY_ESTABLISHMENT
     */
    public static final String KEY_ESTABLISHMENT = "key_establishment";

    /**
     * Constant BASE_URL. Base url end point
     */
    public static final String BASE_URL = "http://projeto1getinline.herokuapp.com/";

    /**
     * Method to validate user email address
     *
     * @param context Context
     * @param email   Email
     * @return True if the email has a validate format, False otherwise
     */
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

    /**
     * Method to validate if the passwords match
     *
     * @param context  Context
     * @param pass1Str Password
     * @param pass2Str Password
     * @return True if the passwords match, False otherwise
     */
    public static boolean validateEqualPassword(Context context, String pass1Str, String pass2Str) {
        boolean result = true;
        if (!pass1Str.equals(pass2Str)) {
            Toast.makeText(context, context.getResources().getString(R.string.msg_dif_password), Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }

    /**
     * Method to setup the start tutorial screens
     *
     * @param context Context
     * @return Tutorial Item List
     */
    public static ArrayList<TutorialItem> getTutorialItems(Context context) {
        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();

        TutorialItem tutorialItem1 = new TutorialItem(context.getString(R.string.tutorial_first_message_title), context.getString(R.string.tutorial_first_message_subtitle),
                R.color.colorPrimaryDark, R.drawable.social_network);

        TutorialItem tutorialItem2 = new TutorialItem(context.getString(R.string.tutorial_second_message_title), context.getString(R.string.tutorial_second_message_subtitle),
                R.color.colorAccent, R.drawable.social_network);

        TutorialItem tutorialItem3 = new TutorialItem(context.getString(R.string.tutorial_third_message_title), context.getString(R.string.tutorial_third_message_subtitle),
                R.color.coral, R.drawable.social_network);

        TutorialItem tutorialItem4 = new TutorialItem(context.getString(R.string.tutorial_fourth_message_title), context.getString(R.string.tutorial_fourth_message_subtitle),
                R.color.seaGreen, R.drawable.social_network);

        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

}
