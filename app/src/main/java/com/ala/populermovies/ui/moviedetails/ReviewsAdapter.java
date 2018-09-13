package com.ala.populermovies.ui.moviedetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ala.populermovies.R;
import com.ala.populermovies.models.MovieReview;
import com.ala.populermovies.ui.base.BaseViewHolder;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private List<MovieReview> items;

    public ReviewsAdapter(List<MovieReview> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        switch (viewType) {
            case VIEW_TYPE_EMPTY:
                view = inflater.inflate(R.layout.movie_review_empty_row, viewGroup, false);
                return new EmptyHolder(view);
            case VIEW_TYPE_NORMAL:
            default:
                view = inflater.inflate(R.layout.movie_review_row, viewGroup, false);
                return new ReviewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        baseViewHolder.onBind(i);
    }
    @Override
    public int getItemViewType(int position) {
        if (items != null && !items.isEmpty()) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }
    @Override
    public int getItemCount() {
        if (items != null && items.size() > 0) {
            return items.size();
        } else {
            return 1;
        }
    }

    class ReviewHolder extends BaseViewHolder{
        TextView reviewAuthorTv;
        TextView reviewContentTv;
        public ReviewHolder(View itemView) {
            super(itemView);
            reviewAuthorTv = itemView.findViewById(R.id.review_author_tv);
            reviewContentTv = itemView.findViewById(R.id.review_content_tv);
        }

        @Override
        public void onBind(int position) {
            reviewAuthorTv.setText(items.get(position).getAuthor());
            reviewContentTv.setText(items.get(position).getContent());
        }
    }
    class EmptyHolder extends BaseViewHolder{

        public EmptyHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(int position) {
        }
    }
}
