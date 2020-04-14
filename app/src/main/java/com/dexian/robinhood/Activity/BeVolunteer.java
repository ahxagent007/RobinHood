package com.dexian.robinhood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dexian.robinhood.R;

public class BeVolunteer extends AppCompatActivity {

//https://secretdevbd.com/static/home/img/robin_hood_terms.jpg

    Button btn_register;
    CheckBox CB_terms;
    TextView TV_terms;
    EditText ET_ref, ET_how, ET_pets, ET_why, ET_education, ET_NID, ET_mother_name, ET_father_name, ET_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_volunteer);

        TV_terms = findViewById(R.id.TV_terms);

        TV_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPicture("https://secretdevbd.com/static/home/img/robin_hood_terms.jpg");
            }
        });

    }

    private void ShowPicture(String URL){

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(BeVolunteer.this);
        View myView = getLayoutInflater().inflate(R.layout.dialog_single_image, null);

        ImageView TV_singleImage;

        TV_singleImage = myView.findViewById(R.id.TV_singleImage);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.main_logo)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(this).load(URL).apply(options).into(TV_singleImage);

        //Glide.with(getApplicationContext()).load(URL).dontAnimate().into(TV_singleImage); //transform(new CenterInside(),new RoundedCorners(10))

        myBuilder.setView(myView);
        final AlertDialog Dialog = myBuilder.create();

        Dialog.setCancelable(true);
        Dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 100), ((getHeight(getApplicationContext()) / 100) * 100));
        Dialog.getWindow().setGravity(Gravity.CENTER);

        Dialog.show();

    }

    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
