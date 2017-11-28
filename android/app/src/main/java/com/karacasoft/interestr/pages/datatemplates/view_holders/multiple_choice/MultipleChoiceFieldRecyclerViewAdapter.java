package com.karacasoft.interestr.pages.datatemplates.view_holders.multiple_choice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.karacasoft.interestr.R;

import java.util.ArrayList;

/**
 * Created by karacasoft on 28.11.2017.
 */

public class MultipleChoiceFieldRecyclerViewAdapter extends RecyclerView.Adapter<MultipleChoiceFieldRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ChoiceItem> choices;
    private OnChoiceClickedListener onChoiceClickedListener;
    private OnChoiceDeleteClickedListener onChoiceDeleteClickedListener;

    public MultipleChoiceFieldRecyclerViewAdapter(ArrayList<ChoiceItem> choices) {
        this.choices = choices;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_multiple_choice_choices, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChoiceItem item = choices.get(position);

        holder.textView.setText(item.getName());

        holder.value = item;
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ChoiceItem value;

        public TextView textView;
        public ImageView deleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.choice_name);
            deleteBtn = itemView.findViewById(R.id.imgDelete);

            textView.setOnClickListener((view) -> onChoiceClickedListener.onChoiceClicked(getAdapterPosition(), value));

            deleteBtn.setOnClickListener((view) ->
                onChoiceDeleteClickedListener.onChoiceDeleteClicked(getAdapterPosition(), value));
        }
    }

    public void setOnChoiceClickedListener(OnChoiceClickedListener onChoiceClickedListener) {
        this.onChoiceClickedListener = onChoiceClickedListener;
    }

    public void setOnChoiceDeleteClickedListener(OnChoiceDeleteClickedListener onChoiceDeleteClickedListener) {
        this.onChoiceDeleteClickedListener = onChoiceDeleteClickedListener;
    }

    public interface OnChoiceClickedListener {
        public void onChoiceClicked(int index, ChoiceItem item);
    }

    public interface OnChoiceDeleteClickedListener {
        public void onChoiceDeleteClicked(int index, ChoiceItem item);
    }
}
