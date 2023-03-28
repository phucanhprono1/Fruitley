package com.example.fruitley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitley.adapter.PlaceYourOrderAdapter;
import com.example.fruitley.model.Food;

import com.example.fruitley.model.Restaurant;
import com.example.fruitley.model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Cart extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView phoneLabel,usernameLabel;
    private View header;
    private float delivery_charge;

    public static String KEY_TEST = "KEY_TEST";

    DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://fruitley-247f2-default-rtdb.firebaseio.com/");
    private RecyclerView cartItemsRecyclerView;
    private PlaceYourOrderAdapter placeYourOrderAdapter;
    private EditText inputAddress,City;
    private TextView tvSubtotalAmount,totalAmount,tvDeliveryChargeAmount,buttonOrder;
    private Bundle bundle;
    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        drawerlayout=findViewById(R.id.drawer_layout);
        navigationView =  findViewById(R.id.nav_view);
        header=navigationView.getHeaderView(0);
        
        phoneLabel = header.findViewById(R.id.phonenumberLabel);
        usernameLabel =  header.findViewById(R.id.usernameLabel);
        inputAddress = findViewById(R.id.inputAddress);
        City=findViewById(R.id.inputCity);
        tvSubtotalAmount=findViewById(R.id.tvSubtotalAmount);
        totalAmount=findViewById(R.id.tvTotalAmount);
        tvDeliveryChargeAmount=findViewById(R.id.tvDeliveryChargeAmount);




        //-------toolbar
        setSupportActionBar(toolbar);
        //---------Navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerlayout ,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        User user = (User) getIntent().getExtras().get("object_user");
        usernameLabel.setText(user.getUsername());
        phoneLabel.setText(user.getPhoneNumber());
        bundle=new Bundle();
        bundle.putSerializable("object_user",user);

        //---------main_menu(receiver_view)
        restaurant = getIntent().getParcelableExtra("rest");
        cartItemsRecyclerView=findViewById(R.id.cartItemsRecyclerView);
        initRecyclerView(restaurant);
        calculateTotalAmount(restaurant);
        //upload bill to firebase
        buttonOrder=findViewById(R.id.buttonPlaceYourOrder);
        buttonOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onPlaceOrderButtonClick(restaurant);
            }
        });





    }
    private void onPlaceOrderButtonClick(Restaurant restaurant) {
        if(TextUtils.isEmpty(inputAddress.getText().toString())) {
            inputAddress.setError("Please enter address ");
            return;
        }
        else if(TextUtils.isEmpty(City.getText().toString())){
            City.setError("Please enter city");
            return;
        }
        final String username=usernameLabel.getText().toString();
        final String phone=phoneLabel.getText().toString();
        final String address=inputAddress.getText().toString();
        final String city = City.getText().toString();
        final String total=totalAmount.getText().toString();
        reference.child("bill").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("bill").child(username).child(address).child("phone").setValue(phone);
                reference.child("bill").child(username).child(address).child("Tên quán").setValue(restaurant.getName());
                reference.child("bill").child(username).child(address).child("City").setValue(city);
                for( Food m:restaurant.getMenus()){
                    reference.child("bill").child(username).child(address).child("food_ordered").child(m.getName()).setValue(m.getTotalInCart());
                }
                reference.child("bill").child(username).child(address).child("Total Cost").setValue(total);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //start success activity..
        Intent i = new Intent(Cart.this, HomeActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }
    private void initRecyclerView(Restaurant restaurant) {
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeYourOrderAdapter = new PlaceYourOrderAdapter(restaurant.getMenus());
        cartItemsRecyclerView.setAdapter(placeYourOrderAdapter);
    }
    @Override
    public void onBackPressed() {
        if(drawerlayout.isDrawerOpen(GravityCompat.START)){
            drawerlayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();

            // push data to previous activity
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra(KEY_TEST, new ModelTest("NameTest", "12"));

            setResult(Activity.RESULT_OK);
            finish();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.home:
                Intent intent1=new Intent(getApplicationContext(),HomeActivity.class);
                intent1.putExtra("item_in_cart",restaurant);
                startActivity(intent1);
                break;
            case R.id.cart:
                break;
            case R.id.log_out:
                Intent intent2=new Intent(Cart.this,MainActivity.class);
                startActivity(intent2);
                break;

        }

        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1000) {
            setResult(Activity.RESULT_OK,data);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void calculateTotalAmount(Restaurant restaurant) {
        float subTotalAmount = 0f;
        delivery_charge=0f;

        for(Food m : restaurant.getMenus()) {
            subTotalAmount += m.getPrice() * m.getTotalInCart();
        }

        tvSubtotalAmount.setText(String.format("%.2f", subTotalAmount)+"đ");

        if(subTotalAmount<200000) {
            delivery_charge=20000.00f;
            tvDeliveryChargeAmount.setText(String.format("%.2f", delivery_charge)+"đ");
            subTotalAmount += delivery_charge;
        }else if(subTotalAmount<400000){
            delivery_charge=10000.00f;
            tvDeliveryChargeAmount.setText(String.format("%.2f", delivery_charge)+"đ");
            subTotalAmount += delivery_charge;
        }
        else{
            delivery_charge=0f;
            tvDeliveryChargeAmount.setText(String.format("%.2f", delivery_charge)+"đ");
            subTotalAmount += delivery_charge;
        }
        totalAmount.setText(String.format("%.2f", subTotalAmount)+"đ");
    }
}