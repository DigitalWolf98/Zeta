package ru.script_dev.zeta.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class DataHelper {

    private Context context;
    private String key;
    private String fileName;

    public DataHelper(Context context, String key) {
        this.context = context;
        this.key = key.trim();
        this.fileName = this.key.concat(".json");
    }

    public void setData(ProductHelper products) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        Gson gson = new Gson();
        String json = gson.toJson(products);

        editor.putString(this.key, json);
        editor.apply();
    }

    public ProductHelper getData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString(this.key, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ProductHelper>(){}.getType();
            return gson.fromJson(json, type);
        }

        return null;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString(this.key, null);

        if (json != null) {
            Gson gson = new Gson();
            ProductHelper products = gson.fromJson(json, ProductHelper.class);

            String jsonFile = gson.toJson(products);

            try {
                File file = new File(context.getExternalFilesDir(null), fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(jsonFile.getBytes());
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void extractData() {
        try {
            File file = new File(context.getExternalFilesDir(null), fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();

            String json = stringBuilder.toString();

            Gson gson = new Gson();
            ProductHelper products = gson.fromJson(json, ProductHelper.class);

            setData(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}