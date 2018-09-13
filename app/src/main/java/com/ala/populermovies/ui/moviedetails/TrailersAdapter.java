package com.ala.populermovies.ui.moviedetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ala.populermovies.R;
import com.ala.populermovies.models.MovieTrailer;
import com.ala.populermovies.ui.base.BaseViewHolder;
import com.ala.populermovies.utilities.AppUtils;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private List<MovieTrailer> items;

    public TrailersAdapter(List<MovieTrailer> items) {
        this.items = items;
    }

    public String getFirstYoutubeUrl(Context context) {
        if (items == null || items.size() < 1)
            return null;
        return String.format(context.getString(R.string.youtube_url_web), items.get(0).getKey());
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        switch (viewType) {
            case VIEW_TYPE_EMPTY:
                view = inflater.inflate(R.layout.movie_trailer_empty_row, viewGroup, false);
                return new EmptyHolder(view);
            case VIEW_TYPE_NORMAL:
            default:
                view = inflater.inflate(R.layout.movie_trailer_row, viewGroup, false);
                return new TrailerHolder(view);
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

    class TrailerHolder extends BaseViewHolder implements View.OnClickListener {
        TextView trailerNameTv;

        public TrailerHolder(View itemView) {
            super(itemView);
            trailerNameTv = itemView.findViewById(R.id.trailer_name_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onBind(int position) {
            trailerNameTv.setText(items.get(position).getName());
        }

        @Override
        public void onClick(View view) {
            String key = items.get(getAdapterPosition()).getKey();
            AppUtils.watchYoutubeVideo(itemView.getContext(), key);
        }

    }

    class EmptyHolder extends BaseViewHolder {

        public EmptyHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(int position) {

        }
    }
}
