package com.example.peter.bakingapp.ui.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.bakingapp.R;
import com.example.peter.bakingapp.model.Steps;

import java.util.ArrayList;

public class RecipeDetailStepsAdapter
        extends
        RecyclerView.Adapter<RecipeDetailStepsAdapter.RecipeStepsAdapterViewHolder> {

    public static final String LOG_TAG = RecipeDetailStepsAdapter.class.getSimpleName();

    /* Used to access utility methods, app resources and layout inflaters */
    private final Context mContext;

    /* Click interface */
    private final StepsAdapterOnCLickHandler mClickHandler;

    /* Recipes array */
    private ArrayList<Steps> mSteps;

    public RecipeDetailStepsAdapter(Context context, StepsAdapterOnCLickHandler listener){
        mContext = context;
        mClickHandler = listener;
    }

    /* Constructor */
    @NonNull
    @Override
    public RecipeStepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        int layoutForThisItem = R.layout.list_item_step;

        View view = LayoutInflater
                .from(mContext)
                .inflate(layoutForThisItem, viewGroup, false);

        view.setFocusable(true);

        return new RecipeStepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapterViewHolder holder, int position) {

        /* Get the current Step object */
        Steps currentStep = mSteps.get(position);

        /* Get the fields we're interested in */
        String shortDescription = currentStep.getShortDescription();

        /* Apply the fields to the views */
        holder.stepTv.setText(shortDescription);
    }

    @Override
    public int getItemCount() {
        if (null == mSteps) return 0;
        return mSteps.size();
    }

    public void swapSteps(ArrayList<Steps> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    /* The interface that receives onClick messages */
    public interface StepsAdapterOnCLickHandler {
        void onClick(int selectedStepId);
    }

    class RecipeStepsAdapterViewHolder
            extends
            RecyclerView.ViewHolder
            implements
            View.OnClickListener {

        final TextView stepTv;


        RecipeStepsAdapterViewHolder(View itemView) {
            super(itemView);

            stepTv = itemView.findViewById(R.id.list_item_step_stepsTv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Steps selectedStep = mSteps.get(adapterPosition);
            mClickHandler.onClick(selectedStep.getStepId());
        }
    }
}