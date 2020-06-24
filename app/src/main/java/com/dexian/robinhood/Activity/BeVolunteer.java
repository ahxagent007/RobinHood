package com.dexian.robinhood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dexian.robinhood.DB.VolunteerDB;
import com.dexian.robinhood.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BeVolunteer extends AppCompatActivity {

//https://secretdevbd.com/static/home/img/robin_hood_terms.jpg
    String TAG= "XIAN";

    Button btn_register;
    CheckBox CB_terms;
    TextView TV_terms;
    EditText ET_ref, ET_how, ET_pets, ET_why, ET_education, ET_NID, ET_mother_name, ET_father_name, ET_name, ET_Phone;

    boolean checked = false;

    private DatabaseReference mDatabaseRef;

    long ID = System.currentTimeMillis();
    String name;
    String FatherName;
    String MotherName;
    String NID;
    String EDU;
    String WhyWork;
    String Pets;
    String Help;
    String Reff;
    String Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_volunteer);

        CB_terms = findViewById(R.id.CB_terms);
        btn_register = findViewById(R.id.btn_register);
        ET_ref = findViewById(R.id.ET_ref);
        ET_how = findViewById(R.id.ET_how);
        ET_pets = findViewById(R.id.ET_pets);
        ET_why = findViewById(R.id.ET_why);
        ET_education = findViewById(R.id.ET_education);
        ET_NID = findViewById(R.id.ET_NID);
        ET_mother_name = findViewById(R.id.ET_mother_name);
        ET_father_name = findViewById(R.id.ET_father_name);
        ET_name = findViewById(R.id.ET_name);
        TV_terms = findViewById(R.id.TV_terms);
        ET_Phone = findViewById(R.id.ET_Phone);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("VOLUNTEER");

        CB_terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checked = b;
                if(b){
                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(BeVolunteer.this);
                    View myView = getLayoutInflater().inflate(R.layout.rules_regulation, null);

                    myBuilder.setView(myView);
                    final AlertDialog Dialog = myBuilder.create();

                    Button btn_done = myView.findViewById(R.id.btn_done);

                    btn_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CB_terms.setChecked(true);
                        }
                    });

                    Dialog.setCancelable(true);
                    Dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 100), ((getHeight(getApplicationContext()) / 100) * 100));
                    Dialog.getWindow().setGravity(Gravity.CENTER);

                    btn_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CB_terms.setChecked(true);
                            Dialog.cancel();
                        }
                    });

                    Dialog.show();
                }
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID = System.currentTimeMillis();
                name = ET_name.getText().toString();
                FatherName = ET_father_name.getText().toString();
                MotherName = ET_mother_name.getText().toString();
                NID = ET_NID.getText().toString();
                EDU = ET_education.getText().toString();
                WhyWork = ET_why.getText().toString();
                Pets = ET_pets.getText().toString();
                Help = ET_how.getText().toString();
                Reff = ET_ref.getText().toString();
                Phone = ET_Phone.getText().toString();

                if(!(name.equalsIgnoreCase("") || FatherName.equalsIgnoreCase("") || MotherName.equalsIgnoreCase("") ||
                        NID.equalsIgnoreCase("") || EDU.equalsIgnoreCase("") || WhyWork.equalsIgnoreCase("") ||
                        Pets.equalsIgnoreCase("") || Help.equalsIgnoreCase("") || Phone.equalsIgnoreCase(""))){
                    if(checked){
                        addVolunteer();
                    }else{
                        Toast.makeText(getApplicationContext(),"Please agree to our terms and conditions", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Please fill-up correctly", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void addVolunteer(){

        VolunteerDB volunteerDB = new VolunteerDB(ID, name, FatherName, MotherName, NID, EDU, WhyWork, Pets, Help, Reff, Phone);

        mDatabaseRef.child(""+volunteerDB.getID()).setValue(volunteerDB);

        Toast.makeText(getApplicationContext(), "Your request successfully added, Please send us a mail to help us find your quickly.", Toast.LENGTH_LONG).show();
        sendEmail(volunteerDB.toString());
        resetAll();
    }

    private void resetAll(){
        CB_terms.setChecked(false);

        ET_ref.setText("");
        ET_how.setText("");
        ET_pets.setText("");
        ET_why.setText("");
        ET_education.setText("");
        ET_NID.setText("");
        ET_mother_name.setText("");
        ET_father_name.setText("");
        ET_name.setText("");
        ET_Phone.setText("");

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

    protected void sendEmail(String msg) {
        Log.i(TAG, "Send email");

        String[] TO = {"animalcaretrustbangladesh@gmail.com"};
        String[] CC = {"ahx.agent007@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Apply for RobinHood Volunteer");
        emailIntent.putExtra(Intent.EXTRA_TEXT, msg);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i(TAG, "Finished sending email...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(BeVolunteer.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
