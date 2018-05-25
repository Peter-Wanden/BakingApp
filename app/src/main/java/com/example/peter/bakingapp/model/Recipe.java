package com.example.peter.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Recipe implements Parcelable {

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    /* The recipes Id from JSON */
    @SerializedName(value = "id", alternate = "mId")
    private final int mId;

    /* Recipes name / title */
    @SerializedName(value = "name", alternate = "mName")
    private final String mName;

    /* Number of portions this recipe makes */
    @SerializedName(value = "servings", alternate = "mServings")
    private final int mServings;

    /* A URL that points to an image for the recipe */
    @SerializedName(value = "image", alternate = "mImage")
    private final String mImage;

    /* The list of ingredients to make the recipe */
    @SerializedName(value = "ingredients", alternate = "mIngredients")
    private final ArrayList<Ingredient> mIngredients;

    /* A list of instructions that describe how to make the recipe */
    @SerializedName(value = "steps", alternate = "mSteps")
    private final ArrayList<Steps> mSteps;

    /**
     * Constructor for a recipe object
     * @param id            The id of the recipe.
     * @param name          The name of the recipe.
     * @param servings      The number of servinge the recipe provides.
     * @param image         A URL location for an image related to the recipe.
     * @param ingredients   An ArrayList of ingredients.
     * @param steps         An ArrayList of the steps needed to be take to create this recipe.
     */
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

    private Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mServings = in.readInt();
        mImage = in.readString();
        mIngredients = in.createTypedArrayList(Ingredient.CREATOR);
        mSteps = in.createTypedArrayList(Steps.CREATOR);
    }

    /* Getters and Setters */
    public int getId() {return mId;}
    public String getTitle() {return mName;}
    public int getServings() {return mServings;}
    public String getImage() {return mImage;}
    public ArrayList<Ingredient> getIngredients() {return mIngredients;}
    public ArrayList<Steps> getSteps() {return mSteps;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeInt(mServings);
        dest.writeString(mImage);
        dest.writeTypedList(mIngredients);
        dest.writeTypedList(mSteps);
    }
}
