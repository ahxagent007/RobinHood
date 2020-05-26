package com.dexian.robinhood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dexian.robinhood.Activity.Admin.AdminAddBkash;
import com.dexian.robinhood.Activity.Admin.AdminAddNews;
import com.dexian.robinhood.Activity.Admin.AdminAddPicture;
import com.dexian.robinhood.DB.Post;
import com.dexian.robinhood.DB.bKash;
import com.dexian.robinhood.R;
import com.dexian.robinhood.SharedPreffClass;
import com.dexian.robinhood.TestActivity;
import com.dexian.robinhood.TouchyWebView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String TAG = "XIAN";

    //TouchyWebView WV_fbPage;

    Button btn_adminLogin, btn_rescue, btn_donate, btn_rescueNews, btn_beVolunteer, btn_callUs, btn_pictures;
    TextView TV_news1, TV_news2, TV_news3;
    LinearLayout LL_admin;
    //Button btn_adminLogout, btn_adminAddbKash, btn_adminAddPost, btn_adminAddMember, btn_adminAddEmergency, btn_adminAddVet, btn_website, btn_emergency_call;
    Button btn_website, btn_emergency_call;
    Button btn_addBkashDonate, btn_addRescueNews, btn_addPicture, btn_adminLogout;

    ImageView IV_FB, IV_YT;

    DatabaseReference mDatabaseRefPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //WV_fbPage = findViewById(R.id.WV_fbPage);
        btn_adminLogin = findViewById(R.id.btn_adminLogin);
        btn_rescue = findViewById(R.id.btn_rescue);
        //btn_vet = findViewById(R.id.btn_vet);
        //btn_emergency = findViewById(R.id.btn_emergency);
        //btn_rescueList = findViewById(R.id.btn_rescueList);
        //btn_members = findViewById(R.id.btn_members);
        btn_donate = findViewById(R.id.btn_donate);
        TV_news1 = findViewById(R.id.TV_news1);
        TV_news2 = findViewById(R.id.TV_news2);
        TV_news3 = findViewById(R.id.TV_news3);
        btn_rescueNews = findViewById(R.id.btn_rescueNews);
        btn_beVolunteer = findViewById(R.id.btn_beVolunteer);
        btn_callUs = findViewById(R.id.btn_callUs);
        btn_pictures = findViewById(R.id.btn_pictures);

        LL_admin = findViewById(R.id.LL_admin);
        /*btn_adminLogout = findViewById(R.id.btn_adminLogout);
        btn_adminAddbKash = findViewById(R.id.btn_adminAddbKash);
        btn_adminAddPost = findViewById(R.id.btn_adminAddPost);
        btn_adminAddMember = findViewById(R.id.btn_adminAddMember);
        btn_adminAddEmergency = findViewById(R.id.btn_adminAddEmergency);
        btn_adminAddVet = findViewById(R.id.btn_adminAddVet);*/

        btn_addBkashDonate = findViewById(R.id.btn_addBkashDonate);
        btn_addRescueNews = findViewById(R.id.btn_addRescueNews);
        btn_addPicture = findViewById(R.id.btn_addPicture);
        btn_adminLogout = findViewById(R.id.btn_adminLogout);

        btn_website = findViewById(R.id.btn_website);

        IV_FB = findViewById(R.id.IV_FB);
        IV_YT = findViewById(R.id.IV_YT);

        btn_emergency_call = findViewById(R.id.btn_emergency_call);

        /*WV_fbPage.getSettings().setJavaScriptEnabled(true);
        //WV_fbPage.getSettings().setLoadWithOverviewMode(true);
        //WV_fbPage.getSettings().setUseWideViewPort(true);
        // WV_fbPage.getSettings().setSupportZoom(true);
        WV_fbPage.setVerticalScrollBarEnabled(true);
        WV_fbPage.getSettings().setBuiltInZoomControls(true);*/

        if (!new SharedPreffClass(getApplicationContext()).getUserID().equalsIgnoreCase("-1")) {
            LL_admin.setVisibility(View.VISIBLE);
            btn_adminLogin.setVisibility(View.GONE);

            btn_adminLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SharedPreffClass(getApplicationContext()).setUserID("-1");
                    startActivity(getIntent());
                    finish();
                }
            });
        } else {
            LL_admin.setVisibility(View.GONE);
        }
        //http://animalcaretrustbangladesh.org/
        //https://m.facebook.com/junglesdad/
        //WV_fbPage.loadUrl("http://animalcaretrustbangladesh.org/");

        btn_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TestActivity.class));
            }
        });
        btn_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WebsiteView.class));
            }
        });

        btn_adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        btn_rescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Rescue.class));

            }
        });

        btn_emergency_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall999();
            }
        });

        btn_addBkashDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminAddBkash.class));
            }
        });

        btn_addRescueNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminAddNews.class));
            }
        });

        btn_addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminAddPicture.class));
            }
        });



        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Donate.class));
            }
        });

        btn_callUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall("+8801970737283");
            }
        });

        IV_YT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCJ2uFoQNNSHRKFq04mLWcdg"));
                startActivity(browserIntent);
            }
        });
        IV_FB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/junglesdad/"));
                startActivity(browserIntent);
            }
        });

        btn_beVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BeVolunteer.class));
            }
        });

        btn_rescueNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RescueNewsActivity.class));
            }
        });

        final ArrayList<Post> posts = new ArrayList<Post>();

        mDatabaseRefPost = FirebaseDatabase.getInstance().getReference("POST");

        mDatabaseRefPost.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Post p = ds.getValue(Post.class);
                    posts.add(p);
                }

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (posts.size() > 2) {
                            TV_news1.setText("# " + posts.get(posts.size() - 1).getAdminName() + " : " + posts.get(posts.size() - 1).getPost());
                            TV_news2.setText("# " + posts.get(posts.size() - 2).getAdminName() + " : " + posts.get(posts.size() - 2).getPost());
                            TV_news3.setText("# " + posts.get(posts.size() - 3).getAdminName() + " : " + posts.get(posts.size() - 3).getPost());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void makeCall(final String number) {

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Add the buttons
        builder.setPositiveButton("Directly", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + number));

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                /*
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

                }else{

                }*/
                //startActivity(intent);

            }
        });
        builder.setNegativeButton("999", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:999"));

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
         // Set other dialog properties
        builder.setMessage("Talk to us through 999 or Directly ?");
        builder.setTitle("Make call");

         // Create the AlertDialog
        dialog = builder.create();
        dialog.show();



    }

    private void makeCall999() {

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Add the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + "999"));

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                /*
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

                }else{

                }*/
                //startActivity(intent);

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        // Set other dialog properties
        builder.setMessage("Do you want to call 999 ?");
        builder.setTitle("Make call");

        // Create the AlertDialog
        dialog = builder.create();
        dialog.show();



    }
}
