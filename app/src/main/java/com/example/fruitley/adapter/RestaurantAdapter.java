package com.example.fruitley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitley.R;
import com.example.fruitley.model.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>{
    private Context mContext;
    private List<Restaurant> RestaurantList;
    private RestaurantClickListener clickListener;
    public RestaurantAdapter(List<Restaurant> list,RestaurantClickListener clickListener){


        this.RestaurantList = list;
        this.clickListener=clickListener;
    }
    public void setData(List<Restaurant> lst){
        this.RestaurantList=lst;
        notifyDataSetChanged();//bind and load data in adapter
    }
    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant,parent,false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant rest=RestaurantList.get(position);
        if( rest==null){
            return;
        }
        holder.imgRestaurant.setImageResource(rest.getRestaurantIdPic());
        holder.restName.setText(rest.getName());
        holder.restAddress.setText(rest.getAddress());
        holder.hotline.setText(rest.getHotline());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                clickListener.onItemClick(RestaurantList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(RestaurantList!=null)return RestaurantList.size();
        return 0;
    }

    static class RestaurantViewHolder extends RecyclerView.ViewHolder{
        ImageView imgRestaurant;
        TextView restName,restAddress,hotline;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRestaurant=itemView.findViewById(R.id.thumbImageRest);
            restName=itemView.findViewById(R.id.restaurantName);
            restAddress=itemView.findViewById(R.id.restaurantAddress);
            hotline=itemView.findViewById(R.id.hotline);
        }


    }
    public interface RestaurantClickListener{
        public void onItemClick(Restaurant restaurant);
    }
}
