package com.example.e610.popularmoviesstage2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.e610.popularmoviesstage2.Models.Movie;
import com.example.e610.popularmoviesstage2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>  {
    ArrayList<Movie> movies;
    Context context;
    int  LastPosition=-1;
    RecyclerViewClickListener ClickListener ;
    public MovieAdapter(){}
    public MovieAdapter(ArrayList<Movie> movies, Context context){
        this.movies=new ArrayList<>();
        this.movies=movies;
        this.context=context;
    }

    public void setClickListener(RecyclerViewClickListener clickListener){
       this.ClickListener= clickListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_movie,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Picasso.with(context).load(holder.imgString+movies.get(position).getPoster_ImageUrl()).placeholder(R.drawable.loadingicon)
                .error(R.drawable.error).into(holder.PosterImg);
        setAnimation(holder.PosterContainer,position);
                //holder.studentImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.female));

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        final public String imgString= "http://image.tmdb.org/t/p/w185/";
        ImageView PosterImg;
        RelativeLayout PosterContainer;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            PosterImg=(ImageView)itemView.findViewById(R.id.Poster_Image2);
            PosterContainer=(RelativeLayout)itemView.findViewById(R.id.PosterContainer);
        }

        @Override
        public void onClick(View view) {
            if(ClickListener!=null)
            ClickListener.ItemClicked(view ,getAdapterPosition());
        }

        public void clearAnimation()
        {
            PosterContainer.clearAnimation();
        }
    }

    public interface RecyclerViewClickListener
    {

        public void ItemClicked(View v, int position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {

        if (position > LastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            LastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
             holder.clearAnimation();
    }



}

