package com.example.peter.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Recipe {

    /* The recipes Id from JSON */
    @SerializedName(value = "id", alternate = "mId")
    private int mId;

    /* Recipes name / title */
    @SerializedName(value = "name", alternate = "mName")
    private String mName;

    /* Number of portions this recipe makes */
    @SerializedName(value = "servings", alternate = "mServings")
    private int mServings;

    /* A URL that points to an image for the recipe */
    @SerializedName(value = "image", alternate = "mImage")
    private String mImage;

    // TODO - Add the following ArrayLists to Parcelable
    /* The list of ingredients to make the recipe */
    private ArrayList<Ingredient> mIngredients;

    /* A list of instructions that describe how to make the recipe */
    private ArrayList<Steps> mSteps;

    /* Constructor */
    public Recipe(
            int id,
            String name,
            int servings,
            String image,
            ArrayList<Ingredient> ingredients,
            ArrayList<Steps> steps){

        this.mId = id;
        this.mName = name;
        this.mServings = servings;
        this.mImage = image;
        this.mIngredients = ingredients;
        this.mSteps = steps;
    }

    /* Getters and Setters */
    public int getId() {return mId;}
    public String getTitle() {return mName;}
    public int getServings() {return mServings;}
    public String getImage() {return mImage;}
    public ArrayList<Ingredient> getIngredients() {return mIngredients;}
    public ArrayList<Steps> getmSteps() {return mSteps;}
}
