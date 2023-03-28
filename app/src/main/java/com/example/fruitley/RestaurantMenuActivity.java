package com.example.fruitley;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitley.adapter.MenuListAdapter;
import com.example.fruitley.model.Food;

import com.example.fruitley.model.Restaurant;
import com.example.fruitley.model.User;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenuActivity extends AppCompatActivity implements MenuListAdapter.MenuListClickListener{
        private List<Food> menuList=null;
        private MenuListAdapter menuListAdapter;
        private Toolbar toolbar;
        private List<Food> itemsInCartList;
        private int totalItemInCart = 0;
        private TextView buttonCheckout;
        private Button back;
        private ImageView bg;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_restaurant_menu);
                toolbar = findViewById(R.id.toolbar);

                setSupportActionBar(toolbar);
                Restaurant restaurant = getIntent().getParcelableExtra("rest");
                toolbar.setTitle(restaurant.getName());
                toolbar.setSubtitle(restaurant.getAddress());
                bg=findViewById(R.id.restaurant_bg);
                bg.setImageResource(restaurant.getRestaurantBg());
                User user = (User) getIntent().getExtras().get("object_user");
                Bundle bundle=new Bundle();
                bundle.putSerializable("object_user",user);

                menuList = restaurant.getMenus();

                RecyclerView recyclerView =  findViewById(R.id.recycler_view_menu);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                menuListAdapter = new MenuListAdapter( this,menuList,this);
                menuListAdapter.setData(menuList);
                recyclerView.setAdapter(menuListAdapter);

                back=findViewById(R.id.back_to_home);
                back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v){
                                Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                                i.putExtras(bundle);
                                startActivity(i);
                        }
                });
                buttonCheckout = findViewById(R.id.view_ur_cart);
                buttonCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(itemsInCartList != null && itemsInCartList.size() <= 0) {
                                        Toast.makeText(RestaurantMenuActivity.this, "Please add some items in cart.", Toast.LENGTH_SHORT).show();
                                        return;
                                }
                                restaurant.setMenus(itemsInCartList);
                                Intent i = new Intent(RestaurantMenuActivity.this, Cart.class);
                                i.putExtra("rest", restaurant);
                                i.putExtras(bundle);
                                //noinspection deprecation
                                startActivityForResult(i, 1000);
                        }
                });
        }


        @Override
        public void onAddToCartClick(Food menu) {
                if(itemsInCartList == null) {
                        itemsInCartList = new ArrayList<>();
                }
                itemsInCartList.add(menu);
                totalItemInCart = 0;

                for(Food m : itemsInCartList) {
                        totalItemInCart = totalItemInCart + m.getTotalInCart();
                }
                buttonCheckout.setText("Checkout (" +totalItemInCart +") items");
        }

        @Override
        public void onUpdateCartClick(Food menu) {
                if(itemsInCartList.contains(menu)) {
                        int index = itemsInCartList.indexOf(menu);
                        itemsInCartList.remove(index);
                        itemsInCartList.add(index, menu);

                        totalItemInCart = 0;

                        for(Food m : itemsInCartList) {
                                totalItemInCart = totalItemInCart + m.getTotalInCart();
                        }
                        buttonCheckout.setText("Checkout (" +totalItemInCart +") items");
                }
        }

        @Override
        public void onRemoveFromCartClick(Food menu) {
                if(itemsInCartList.contains(menu)) {
                        itemsInCartList.remove(menu);
                        totalItemInCart = 0;

                        for(Food m : itemsInCartList) {
                                totalItemInCart = totalItemInCart + m.getTotalInCart();
                        }
                        buttonCheckout.setText("Checkout (" +totalItemInCart +") items");
                }
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                        case android.R.id.home :
                                finish();
                        default:
                                //do nothing
                }
                return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if(requestCode == 1000 && resultCode == Activity.RESULT_OK) {
                        // get data from intent
                        Restaurant restaurant=data.getParcelableExtra(Cart.KEY_TEST);

                }
        }
}