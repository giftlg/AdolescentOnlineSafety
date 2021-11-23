package com.example.adolescentonlinesafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class RegisterActivity extends AppCompatActivity {

    Button reg_btn;
    EditText reg_email,reg_password;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_btn = (Button) findViewById(R.id.reg_button);
        reg_email = (EditText) findViewById(R.id.reg_email);
        reg_password = (EditText) findViewById(R.id.reg_password);
        loadingbar= new ProgressDialog(this);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Configuration.FirstEmailKey = reg_email.getText().toString();


                CreatAccount();

            }
        });




    }

    private void CreatAccount() {
        String email = reg_email.getText().toString().replace(".",","); //replacing character (.) to (,) because it is not allowed in firebase realtime database
        String password = reg_password.getText().toString().replace('a','x'); // coveting character for security reason in database

        Configuration.FirstEmailKey = reg_email.getText().toString();




        if (TextUtils.isEmpty(email))
        {
            //function
            Toast.makeText(this, "enter email", Toast.LENGTH_SHORT).show();

        }



        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();

        }
        else if (password.length()<6)
        {
            Toast.makeText(this, "password must contain 6 or more characters", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Create account");
            loadingbar.setMessage("Checking credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidateEmail(email,password);

        }
    }

    private void ValidateEmail(String email, String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(email).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("email", email);
                    userdataMap.put("password", password);

                    RootRef.child("Users").child(email).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Account created successfully ", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);


                                    } else {
                                        loadingbar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();

                                    }


                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "This" + email + "Already exist", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}