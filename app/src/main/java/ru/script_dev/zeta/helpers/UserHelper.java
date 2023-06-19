package ru.script_dev.zeta.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class UserHelper {
    private static final String Preferences = "UserData";

    public static void saveUser(Context context, String name, String mail, String pass) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Preferences, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.putString("Mail", mail);
        editor.putString("Pass", pass);
        editor.apply();
    }

    public static String getName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Preferences, Context.MODE_PRIVATE);
        return sharedPreferences.getString("Name", "");
    }

    public static String getMail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Preferences, Context.MODE_PRIVATE);
        return sharedPreferences.getString("Mail", "");
    }

    public static String getPass(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Preferences, Context.MODE_PRIVATE);
        return sharedPreferences.getString("Pass", "");
    }
}
