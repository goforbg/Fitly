package com.androar.fitly.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.androar.fitly.agora.Constants;

public class SPUtils {
    public static final int NO_USER = -1;

    public static void saveUserId(Context context, int id) {
        SharedPreferences pf = context.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        pf.edit().putInt(Constants.PREF_USER_ID, id).apply();
    }

    public static int getUserId(Context context) {
        SharedPreferences pf = context.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        return pf.getInt(Constants.PREF_USER_ID, NO_USER);
    }
}
