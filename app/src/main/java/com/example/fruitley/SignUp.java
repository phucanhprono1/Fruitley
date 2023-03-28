package com.example.fruitley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    private EditText username,phoneNumber,password,repassword;
    private Button btnRegister,btnLogin;



    DatabaseReference reference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://fruitley-247f2-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        btnRegister=findViewById(R.id.btnRegisterNow);
        btnLogin = findViewById(R.id.btnLogIn);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                final String user=username.getText().toString();
                final String phone=phoneNumber.getText().toString();
                final String pass=password.getText().toString();
                final String repass=repassword.getText().toString();
                if(user.isEmpty() || phone.isEmpty() || pass.isEmpty() || repass.isEmpty()){
                    Toast.makeText(SignUp.this,"Please fill all these field",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(pass.equals(repass)){
                        reference.child("users").addListenerForSingleValueEvent(new ValueEventListener(){
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot){
                                if(snapshot.hasChild(phone)){
                                    Toast.makeText(SignUp.this,"Phone is already registered",Toast.LENGTH_SHORT).show();

                                }
                                else{
//                                    User us=new User(user,phone,pass);
                                    reference.child("users").child(phone).child("username").setValue(user);
                                    reference.child("users").child(phone).child("phone").setValue(phone);
                                    reference.child("users").child(phone).child("password").setValue(pass);
                                    Toast.makeText(SignUp.this,"Registered Successfully.",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                    startActivity(intent);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else{
                        Toast.makeText(SignUp.this,"Password not matching",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(),SignIn.class));
            }
        });
    }
}