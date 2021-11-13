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

import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {


   public Button register_btn;
    public static EditText email_edit_text;
    private ProgressDialog loadingbar;
    public static String email_name;
    public TextView testing;
    private CheckBox chckbx_rememberMe;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register_btn=  findViewById(R.id.Register_btn);
        email_edit_text =  findViewById(R.id.Email_Edit_text);
        loadingbar= new ProgressDialog(this);

        Paper.init(this);






//action to be perfomed when register button is clicked

    register_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            String reg_email = email_edit_text.getText().toString();
      Paper.book().write(Configuration.FirstEmailKey,reg_email);
  //check if email is entered
      if (TextUtils.isEmpty(reg_email))
      {
          Toast.makeText(MainActivity.this,"email is empty",Toast.LENGTH_LONG).show();

      }
      else
          {
              Toast.makeText(MainActivity.this, "Email "+reg_email+" registered", Toast.LENGTH_SHORT).show();
              Intent intent = new Intent(MainActivity.this,LoggedInActivity.class);
              startActivity(intent);


          }

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
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }


        return true;
            case R.id.feed_back:
                Toast.makeText(this, "give feed back", Toast.LENGTH_SHORT).show();
                return true;



            case R.id.about:

                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.show();

                //setting animation of dialog
                dialog.getWindow().setWindowAnimations(R.style.AnimationsForDialog);


                return true;

            case R.id.hide_icon:
                Toast.makeText(this, "hide application", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.exit:

                finish();

            default:


        return super.onOptionsItemSelected(item);
      }
   }

}