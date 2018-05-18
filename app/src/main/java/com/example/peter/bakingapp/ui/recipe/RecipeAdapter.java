package com.example.peter.bakingapp.ui.recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.peter.bakingapp.GlideApp;
import com.example.peter.bakingapp.R;
import com.example.peter.bakingapp.model.Recipe;

import java.util.ArrayList;

public class RecipeAdapter
        extends
        RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private static final String LOG_TAG = RecipeAdapter.class.getSimpleName();

    /* Used to access utility methods, app resources and layout inflaters */
    private final Context mContext;

    /* Click interface */
    final private RecipeAdapterOnClickHandler mClickHandler;

    /* Recipes array */
    private ArrayList<Recipe> mRecipes;

    /* Constructor */
    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler listener) {
        mContext = context;
        mClickHandler = listener;
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        int layoutForThisItem = R.layout.list_item_recipe;

        View view = LayoutInflater
                .from(mContext)
                .inflate(layoutForThisItem, viewGroup, false);

        view.setFocusable(true);

        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {

        /* Get the Recipe object */
        Recipe currentRecipe = mRecipes.get(position);

        /* Get the fields we're interested in */
        String recipeTitle = currentRecipe.getTitle();
        String imageLink = currentRecipe.getImage();
        int servings = currentRecipe.getServings();

        /* Apply the fields to the views */
        holder.recipeTitleTv.setText(recipeTitle);

        if (imageLink != null && imageLink.length() > 2) {

            GlideApp
                    .with(holder.recipeThumbnailIv)
                    .load(currentRecipe.getImage())
                    .placeholder(R.drawable.menu_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.recipeThumbnailIv);
        } else {

            GlideApp
                    .with(holder.recipeThumbnailIv)
                    .load(R.drawable.menu_placeholder)
                    .into(holder.recipeThumbnailIv);
        }

        holder.recipeServingsTv.setText(String.valueOf(servings));
    }

    @Override
    public int getItemCount() {
        if (null == mRecipes) return 0;
        return mRecipes.size();
    }

    /* Swaps out the adapter data */
    public void swapRecipes(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    /* The interface that receives onClick messages */
    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe clickedRecipe);
    }

    class RecipeAdapterViewHolder
            extends
            RecyclerView.ViewHolder
            implements
            View.OnClickListener {

        final ImageView recipeThumbnailIv;
        final TextView recipeTitleTv;
        final TextView recipeServingsTv;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);

            recipeThumbnailIv = itemView.findViewById(R.id.recipe_list_item_thumbnail);
            recipeTitleTv = itemView.findViewById(R.id.recipe_list_item_title);
            recipeServingsTv = itemView.findViewById(R.id.recipe_list_item_servings);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe selectedRecipe = mRecipes.get(adapterPosition);
            mClickHandler.onClick(selectedRecipe);
        }
    }
}
