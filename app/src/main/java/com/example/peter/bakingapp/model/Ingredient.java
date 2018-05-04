package com.example.peter.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    private int mRecipeId;

    @SerializedName(value = "quantity", alternate = "mQuantity")
    private double mQuantity;

    @SerializedName(value = "measure", alternate = "mMeasure")
    private String mMeasure;

    @SerializedName(value = "ingredient", alternate = "mIngredient")
    private String mIngredient;

    /**
     * Constructor for an Ingredient object.
     * @param recipeId      The id of the recipe to which this ingredient belongs.
     * @param quantity      Quantity of the ingredient.
     * @param measure       Unit of measure used to measure this ingredient.
     * @param ingredient    The given common name for this ingredient.
     */
    public Ingredient(int recipeId, double quantity, String measure, String ingredient){
        this.mRecipeId = recipeId;
        this.mQuantity = quantity;
        this.mMeasure = measure;
        this.mIngredient = ingredient;
    }

    /* Parcelable implementation */
    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    private Ingredient(Parcel in) {
        mRecipeId = in.readInt();
        mQuantity = in.readDouble();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mRecipeId);
        dest.writeDouble(mQuantity);
        dest.writeString(mMeasure);
        dest.writeString(mIngredient);
    }

    public double getQuantity() { return mQuantity; }
    public String getMeasure() { return mMeasure; }
    public String getIngredient() { return mIngredient; }
}
