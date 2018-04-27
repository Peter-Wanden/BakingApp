package com.example.peter.bakingapp.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    private int mRecipeId;

    @SerializedName(value = "quantity", alternate = "mQuantity")
    private double mQuantity;

    @SerializedName(value = "measure", alternate = "mMeasure")
    private String mMeasure;

    @SerializedName(value = "ingredient", alternate = "mIngredient")
    private String mIngredient;

    public Ingredient(int recipeId, double quantity, String measure, String ingredient){
        this.mRecipeId = recipeId;
        this.mQuantity = quantity;
        this.mMeasure = measure;
        this.mIngredient = ingredient;
    }
}
