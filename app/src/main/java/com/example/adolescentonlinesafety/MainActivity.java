package com.example.adolescentonlinesafety;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adolescentonlinesafety.Model.Users;
import com.example.adolescentonlinesafety.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {


    public Button login_btn;
    TextView create_account;
    public static EditText input_email,input_password;
    private ProgressDialog loadingbar;
    public static String email_name;
    private CheckBox checkbox_rememberMe;
    private ProgressDialog loadinbar;
    private String parentDbName = "Users";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_btn =  findViewById(R.id.Login_btn);
        input_email =  findViewById(R.id.login_email);
        input_password = findViewById(R.id.login_password);
        loadingbar= new ProgressDialog(this);

        create_account = findViewById(R.id.Create_account);
        loadinbar=new ProgressDialog(this);

        Paper.init(this);

        String UserEmailKey = Paper.book().read(Prevalent.UserEmailKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserEmailKey !="" && UserPasswordKey !="") {
            if (!TextUtils.isEmpty(UserEmailKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                AlloweAccess(UserEmailKey, UserPasswordKey);

                loadinbar.setTitle("");
                loadinbar.setMessage("Please wait....");
                loadinbar.setCanceledOnTouchOutside(false);
                loadinbar.show();

            }
        }
    }
    private void AlloweAccess(final String login_email, final String login_password)

    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(login_email).exists()) {
                    Users usersData = snapshot.child("Users").child(login_email).getValue(Users.class);
                    if (usersData.getEmail().equals(login_email)) {
                        if (usersData.getPassword().equals(login_password)) {
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            loadinbar.dismiss();
                            Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            loadinbar.dismiss();
                        }


                    }


                } else {
                    Toast.makeText(MainActivity.this, "No account registered", Toast.LENGTH_SHORT).show();
                    loadinbar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });





//action to be performed when login button is clicked

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //login method
                LoginUser();
            }
        });





    }





    // login method to be called
    private void LoginUser()
    {
        String login_email = input_email.getText().toString().replace('.',',') ;;
        String login_password = input_password.getText().toString().replace('a','x'); //manual login string covet


        if (TextUtils.isEmpty(login_email))
        {
            Toast.makeText(MainActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(login_password))
        {
            Toast.makeText(MainActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadinbar.setTitle("Login");
            loadinbar.setMessage("Logging into your account");
            loadinbar.setCanceledOnTouchOutside(false);
            loadinbar.show();

            AllowAccessToAccount(login_email,login_password);

        }

    }

    private void AllowAccessToAccount(String login_email, String login_password)
    {


        Paper.book().write(Prevalent.UserEmailKey,login_email);
        Paper.book().write(Prevalent.UserPasswordKey,login_password);


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child(parentDbName).child(login_email).exists())

                {
                    Users usersData= snapshot.child(parentDbName).child(login_email).getValue(Users.class);

                    if (usersData.getEmail().equals(login_email))
                    {
                        if (usersData.getPassword().equals(login_password))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(MainActivity.this,"Welcome Admin",Toast.LENGTH_SHORT).show();
                                loadinbar.dismiss();
                                Intent intent = new Intent(MainActivity.this,LoggedInActivity.class);
                                startActivity(intent);

                            }
                            else if (parentDbName.equals("Users"))
                            {
                                Intent intent = new Intent(MainActivity.this,LoggedInActivity.class);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                                loadinbar.dismiss();


                            }

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            loadinbar.dismiss();
                        }
                    }

                }
                else
                {
                    Toast.makeText(MainActivity.this,"Account "+login_email+" not registered",Toast.LENGTH_SHORT).show();
                    loadinbar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });












    }
    // inflating menu on the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    //on click menu item action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                try {

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Adolescent Online Safety app");
                    String shareMessage= "\n Let me recommend you this application \n https://drive.google.com/file/d/1iaDsnZ3GOTHx0DWy8wscdHrT_cBaJJhp/view?usp=sharing\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }


                return true;
            case R.id.feedback:
                Toast.makeText(this, "Give Feedback", Toast.LENGTH_SHORT).show();
                return true;



            case R.id.about:

                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.show();

                //setting animation of dialog
                dialog.getWindow().setWindowAnimations(R.style.AnimationsForDialog);


                return true;


            case R.id.exit:

                finish();

            default:


                return super.onOptionsItemSelected(item);
        }

    }



}