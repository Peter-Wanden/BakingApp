package com.example.peter.bakingapp.utils;

import com.example.peter.bakingapp.app.Constants;
import com.example.peter.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class GsonUtils {

    private static final String LOG_TAG = GsonUtils.class.getSimpleName();

    public static ArrayList<Recipe> getRecipes() {

        // Create the URL object
        URL url = null;

        try {
            url = new URL(Constants.recipeUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Query the server and return a JSON response
        String jsonResponse = null;
        try {
            jsonResponse = NetworkUtils.getResponseFromHttpUrl(Objects.requireNonNull(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Json starts with an Array so we have to use a TypeToken
        Type recipesType = new TypeToken<ArrayList<Recipe>>(){}.getType();

        return new Gson().fromJson(jsonResponse, recipesType);
    }
}
