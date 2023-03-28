package com.example.fruitley.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fruitley.R;
import com.example.fruitley.model.Food;

import java.util.List;

public class PlaceYourOrderAdapter extends RecyclerView.Adapter<PlaceYourOrderAdapter.MyViewHolder> {

    private List<Food> menuList;

    public PlaceYourOrderAdapter(List<Food> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    public void updateData(List<Food> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_order_recycler_row, parent, false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceYourOrderAdapter.MyViewHolder holder, int position) {
        holder.thumbImage.setImageResource(menuList.get(position).getPicId());
        holder.menuName.setText(menuList.get(position).getName());
        holder.menuPrice.setText("Giá: "+String.format("%.2f", menuList.get(position).getPrice()*menuList.get(position).getTotalInCart())+"đ");
        holder.menuQty.setText("Số lượng: " + menuList.get(position).getTotalInCart());


    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  menuName;
        TextView  menuPrice;
        TextView  menuQty;
        ImageView thumbImage;

        public MyViewHolder(View view) {
            super(view);
            menuName = view.findViewById(R.id.menuName);
            menuPrice = view.findViewById(R.id.menuPrice);
            menuQty = view.findViewById(R.id.menuQty);
            thumbImage = view.findViewById(R.id.thumbImage);
        }
    }
}
