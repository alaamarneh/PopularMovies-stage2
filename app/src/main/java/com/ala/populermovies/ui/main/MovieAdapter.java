package com.ala.populermovies.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ala.populermovies.R;
import com.ala.populermovies.models.Movie;
import com.ala.populermovies.ui.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    private List<Movie> movies;
    private MovieAdapterListener mListener;

    public MovieAdapter(MovieAdapterListener mListener) {
        this.mListener = mListener;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.movie_row, viewGroup, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int i) {
        holder.onBind(i);
    }

    @Override
    public int getItemCount() {
        if (movies != null && movies.size() > 0) {
            return movies.size();
        } else {
            return 0;
        }
    }

    class MovieHolder extends BaseViewHolder implements View.OnClickListener {
        ImageView movieImage;

        MovieHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movie_img);
            itemView.setOnClickListener(this);
        }

        public void onBind(int pos) {
            Picasso.with(itemView.getContext())
                    .load(movies.get(pos).getPosterImage())
                    .into(movieImage);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (mListener != null) {
                mListener.onMovieClicked(movies.get(pos));
            }
        }
    }


    public interface MovieAdapterListener {
        void onMovieClicked(Movie movie);
    }
}
