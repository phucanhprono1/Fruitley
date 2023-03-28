
package com.example.fruitley;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fruitley.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private Button login;
    private TextView register;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://fruitley-247f2-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        final EditText phoneNumber = findViewById(R.id.phoneNumber1);
        final EditText password1 = findViewById(R.id.password1);
        login = findViewById(R.id.btnLogin);
        register=findViewById(R.id.register);

        login.setOnClickListener(v -> {
            final String phone = phoneNumber.getText().toString();
            final String pass1 = password1.getText().toString();
            if (phone.isEmpty() || pass1.isEmpty()) {
                Toast.makeText(SignIn.this, "Please fill all these field", Toast.LENGTH_SHORT).show();

            } else {
                reference.child("users").addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(phone)){
                        final String getPassword=snapshot.child(phone).child("password").getValue(String.class);
                        final String getUsername=snapshot.child(phone).child("username").getValue(String.class);
                        if(getPassword.equals(pass1)){
                            User us=new User(getUsername,phone,pass1);
                            Toast.makeText(SignIn.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("object_user", us);

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(SignIn.this,"Sai mật khẩu",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(SignIn.this,"Không tồn tại số điện thoại này",Toast.LENGTH_SHORT).show();
                    }
                }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });
            }
        });
        register.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),SignUp.class);
            startActivity(intent);
        });
    }
}