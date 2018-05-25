package com.example.peter.bakingapp.utils;

import com.example.peter.bakingapp.app.Constants;
import com.example.peter.bakingapp.model.Ingredient;
import com.example.peter.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GsonUtils {

    public static ArrayList<Recipe> getRecipes() {

        /* Create the URL object */
        URL url = null;

        try {
            url = new URL(Constants.recipeUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /* Query the server and return a JSON response */
        String jsonResponse = null;
        try {
            jsonResponse = NetworkUtils.getResponseFromHttpUrl(Objects.requireNonNull(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* JSON starts with an Array so we have to use a TypeToken */
        Type recipesType = new TypeToken<ArrayList<Recipe>>(){}.getType();

        return new Gson().fromJson(jsonResponse, recipesType);
    }

    /* Turns a list of ingredient objects into a JSON string */
    public static String ingredientsToJson(List<Ingredient> ingredients) {
        return new Gson().toJson(ingredients);
    }

    /* Turns a JSON string into a list of JSON objects */
    public static List<Ingredient> ingredientsJsonToList (String ingredientsJson){

        /* JSON starts with an Array so we have to use a TypeToken */
        Type ingredientsType = new TypeToken<List<Ingredient>>(){}.getType();

        return new Gson().fromJson(ingredientsJson, ingredientsType);
    }
}
