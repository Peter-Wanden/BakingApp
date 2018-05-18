package com.example.peter.bakingapp.ui.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peter.bakingapp.R;
import com.example.peter.bakingapp.model.Ingredient;
import com.example.peter.bakingapp.utils.IngredientFormat;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecipeDetailIngredientAdapter
        extends
        RecyclerView.Adapter<RecipeDetailIngredientAdapter.RecipeIngredientAdapterViewHolder> {

    private final Context mContext;
    private ArrayList<Ingredient> mIngredients;

    public RecipeDetailIngredientAdapter(Context context) {
        mContext = context;
        mIngredients = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecipeIngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        int layoutForThisItem = R.layout.list_item_ingredient;

        View view = LayoutInflater
                .from(mContext)
                .inflate(layoutForThisItem, viewGroup, false);

        return new RecipeIngredientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientAdapterViewHolder holder, int position) {

        /* Get the ingredient at the passed in position */
        Ingredient ingredients = mIngredients.get(position);

        /* Set the color of the TextViews, white if tablet, black if phone */
        if (mContext.getResources().getBoolean(R.bool.is_tablet)){
            holder.quantityTV.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.measureTV.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.ingredientTV.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        /* Get the fields we're interested in, format then display them */
        holder.quantityTV.setText(
                IngredientFormat.formatRawQuantity(ingredients.getQuantity()));

        holder.measureTV.setText(
                IngredientFormat.formatRawUnits(ingredients.getMeasure()));

        holder.ingredientTV.setText(
                IngredientFormat.formatRawIngredient(ingredients.getIngredient()));
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    // Swaps out the adapters data
    public void newIngredients(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    static class RecipeIngredientAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView quantityTV;
        final TextView measureTV;
        final TextView ingredientTV;

        public RecipeIngredientAdapterViewHolder (View itemView) {
            super(itemView);

            quantityTV = itemView.findViewById(R.id.ingredient_list_item_quantity);
            measureTV = itemView.findViewById(R.id.ingredient_list_item_measure);
            ingredientTV = itemView.findViewById(R.id.ingredient_list_item_ingredient);
        }
    }
}
