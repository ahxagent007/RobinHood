package com.dexian.robinhood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dexian.robinhood.DB.RescueDB;
import com.dexian.robinhood.DB.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class AddRescue extends AppCompatActivity {

    String TAG = "XIAN";

    EditText ET_name, ET_details, ET_phone;
    Spinner SP_area;
    ImageView IV_uploadImage;
    Button btn_uploadImage, btn_rescueDone;
    ProgressBar PB_uploading;

    private static final int PICK_IMAGE_REQUEST = 111;
    private Uri mImageUri;

    RescueDB rescueDB;

    double longitude;
    double latitude;
    String IP = "***.***.***.***";

    //FIREBASE
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rescue);

        ET_name = findViewById(R.id.ET_name);
        ET_details = findViewById(R.id.ET_details);
        ET_phone = findViewById(R.id.ET_phone);
        SP_area = findViewById(R.id.SP_area);
        IV_uploadImage = findViewById(R.id.IV_uploadImage);
        btn_uploadImage = findViewById(R.id.btn_uploadImage);
        btn_rescueDone = findViewById(R.id.btn_rescueDone);
        PB_uploading = findViewById(R.id.PB_uploading);

        mStorageRef = FirebaseStorage.getInstance().getReference("RESCUE_PICTURE");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RESCUE");

        generateLocation();
        simpleRequestGetIP();

        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btn_rescueDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rescueDB = new RescueDB();

                if(ET_name.getText().toString().length()>0 && ET_details.getText().toString().length()>0 && ET_phone.getText().toString().length()>0){
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
                    Date date = new Date();
                    String dddd = dateFormat.format(date);

                    rescueDB.setName(ET_name.getText().toString());
                    rescueDB.setPhone(ET_phone.getText().toString());
                    rescueDB.setDetails(ET_details.getText().toString());
                    rescueDB.setArea(SP_area.getSelectedItem().toString());
                    rescueDB.setLocation(latitude+","+longitude);
                    rescueDB.setID(System.currentTimeMillis());
                    rescueDB.setIP(IP);
                    rescueDB.setStatus("PENDING");
                    rescueDB.setTime(dddd);

                    uploadFileToFirebase();
                }

            }
        });


        // Write a message to the database
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.i(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/
    }

    private void simpleRequestGetIP() {
        // ...

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        final Gson g = new Gson();

        String url = "https://api.ipify.org/?format=json";
        Log.i(TAG, "URL : " + url);

        // Request a string response from the provided URL.
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.i(TAG, response);



                        Status status = g.fromJson(response, Status.class);

                        IP = status.getIp();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.i(TAG, "simpleRequestGetIP : "+error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return null;
            }
        };


        // Add the request to the RequestQueue.
        //queue.add(stringRequest);
        queue.add(postRequest);
    }

    private void generateLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationManager lm = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, locationListener);
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }


    private String getFileExtention(Uri uri){

        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();

            //Picasso.get().load(mImageUri).into(IV_AddProdPic);
        }
    }


    private void uploadFileToFirebase(){
        if(mImageUri != null){

            Toast.makeText(getApplicationContext(),"Uploading Image",Toast.LENGTH_SHORT).show();
           /* try {
                Bitmap b = decodeUri(getApplicationContext(),mImageUri,100);
                mImageUri = getImageUri(getApplicationContext(),b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            final StorageReference fileRef = mStorageRef.child(rescueDB.getID()+"."+getFileExtention(mImageUri));

            fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PB_uploading.setProgress(0);
                        }
                    },5000);

                    Toast.makeText(getApplicationContext(),"SUCCESSFULLY ADDED",Toast.LENGTH_LONG).show();

                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String propicURL = uri.toString();
                            Log.i(TAG,"DOWNLOAD URL = "+propicURL);
                            rescueDB.setPictureName(propicURL);
                            addToFirebaseDatabase();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"UPLOAD FAILED",Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    PB_uploading.setProgress((int)progress);
                }
            });

        }else{
            Toast.makeText(getApplicationContext(),"NO IMAGE SELECTED!",Toast.LENGTH_SHORT).show();
        }

    }

    private void addToFirebaseDatabase(){

        mDatabaseRef.child(""+rescueDB.getID()).setValue(rescueDB);

        //ResetAll();

    }
}
