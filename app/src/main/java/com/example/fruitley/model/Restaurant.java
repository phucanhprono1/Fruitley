package com.example.fruitley.model;

import static android.os.UserHandle.readFromParcel;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Restaurant implements Parcelable {
    private String name,address,hotline;
    private int restaurantIdPic,restaurantBg;
    private List<Food> menus=new ArrayList<>();


    public Restaurant(int restaurantIdPic,int restaurantBg,String name, String address, String hotline , List<Food> menus) {
        this.name = name;
        this.address = address;
        this.hotline = hotline;
        this.restaurantIdPic = restaurantIdPic;
        this.restaurantBg = restaurantBg;
        this.menus = menus;
    }

    public int getRestaurantBg() {
        return restaurantBg;
    }

    public void setRestaurantBg(int restaurantBg) {
        this.restaurantBg = restaurantBg;
    }

    protected Restaurant(Parcel in) {
        menus=in.createTypedArrayList(Food.CREATOR);
        name = in.readString();
        address = in.readString();
        hotline = in.readString();
        restaurantIdPic = in.readInt();
        restaurantBg=in.readInt();

    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public int getRestaurantIdPic() {
        return restaurantIdPic;
    }

    public void setRestaurantIdPic(int restaurantIdPic) {
        this.restaurantIdPic = restaurantIdPic;
    }

    public List<Food> getMenus() {
        return menus;
    }

    public void setMenus(List<Food> menus) {
        this.menus = menus;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(menus);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(hotline);
        dest.writeInt(restaurantIdPic);
        dest.writeInt(restaurantBg);

    }
}
