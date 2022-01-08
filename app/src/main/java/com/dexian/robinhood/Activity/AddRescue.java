package com.dexian.robinhood.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dexian.robinhood.DB.Area;
import com.dexian.robinhood.DB.RescueDB;
import com.dexian.robinhood.DB.Status;
import com.dexian.robinhood.R;
import com.dexian.robinhood.SharedPreffClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AddRescue extends AppCompatActivity {

    String TAG = "XIAN";

    EditText ET_name, ET_details, ET_phone;
    Spinner SP_area;
    ImageView IV_uploadImage;
    Button btn_uploadImage, btn_rescueDone;
    ProgressBar PB_uploading, PB_loading;

    private Uri mImageUri;

    RescueDB rescueDB;

    double longitude;
    double latitude;
    String IP = "***.***.***.***";
    Long ID_firebase = (long)0;
    //FIREBASE1
    private DatabaseReference mDatabaseRef, mDatabaseRefArea;

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
        PB_loading = findViewById(R.id.PB_loading);

        PB_loading.setVisibility(View.INVISIBLE);

        mStorageRef = FirebaseStorage.getInstance().getReference("RESCUE_PICTURE");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RESCUE");
        mDatabaseRefArea = FirebaseDatabase.getInstance().getReference("AREA");

        //generateLocation();
        simpleRequestGetIP();

        final ArrayList<String> areasList = new ArrayList<String>();

        mDatabaseRefArea.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                areasList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Area a = ds.getValue(Area.class);
                    areasList.add(a.getName());
                    //Log.i(TAG,ds.toString()+" "+ds.getKey());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, areasList);
                SP_area.setAdapter(spinnerArrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*mDatabaseRef.orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ID_firebase = Long.parseLong(ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        ID_firebase = System.currentTimeMillis();

        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermissions()){
                    openFileChooser();
                }

            }
        });

        btn_rescueDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rescueDB = new RescueDB();

                if(ET_name.getText().toString().length()>0 && ET_details.getText().toString().length()>0 && ET_phone.getText().toString().length()>0){
                    PB_loading.setVisibility(View.VISIBLE);
                    btn_uploadImage.setEnabled(false);
                    btn_rescueDone.setEnabled(false);

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    String dddd = dateFormat.format(date);

                    rescueDB.setName(ET_name.getText().toString());
                    rescueDB.setPhone(ET_phone.getText().toString());
                    rescueDB.setDetails(ET_details.getText().toString());
                    rescueDB.setArea(SP_area.getSelectedItem().toString());
                    //rescueDB.setLocation(latitude+","+longitude);
                    rescueDB.setID(ID_firebase);
                    rescueDB.setIP(IP);
                    rescueDB.setStatus("PENDING");
                    rescueDB.setTime(dddd);

                    uploadFileToFirebase();
                }else{
                    Toast.makeText(getApplicationContext(),"Fill up Correctly", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
            Log.i(TAG, "LOCATION : "+latitude+","+longitude);
        }

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 10, locationListener);
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.i(TAG, "LOCATION : "+latitude+","+longitude);
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
/*
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

                    //Toast.makeText(getApplicationContext(),"SUCCESSFULLY Uploaded",Toast.LENGTH_LONG).show();

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
                    Log.i(TAG, "UPLOAD FAILED : "+e);

                    btn_uploadImage.setEnabled(true);
                    btn_rescueDone.setEnabled(true);
                    PB_loading.setVisibility(View.INVISIBLE);

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
            btn_uploadImage.setEnabled(true);
            btn_rescueDone.setEnabled(true);
        }

    }*/

    private static final int PICK_IMAGE_REQUEST = 11;
    private Uri mImageUri1, mImageUri2, mImageUri3;
    private StorageReference mStorageRef;

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri1 = data.getData();
            try {
                Long curTime = System.currentTimeMillis();
                Bitmap resizedBitmap = decodeUri(getApplication(), mImageUri1, 400);
                String path = saveBitmapToDevice(resizedBitmap, curTime+"_1");
                File f = new File(path);
                mImageUri1 = Uri.fromFile(f);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Glide.with(getApplicationContext()).load(mImageUri1).transform(new CenterInside(), new RoundedCorners(15)).dontAnimate().into(IV_uploadImage);

        }
    }

    String fname1, fname2, fname3;

    private String saveBitmapToDevice(Bitmap bitmap, String name){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/ROBINHOOD");
        myDir.mkdirs();
        String directory = root + "/ROBINHOOD/";
        //Random generator = new Random();
        //int n = 10000;
        //n = generator.nextInt(n);

        File file = null;
        String fname = null;

        fname1 = "RobinHood_"+name + ".jpg";
        file = new File(myDir, fname1);
        fname = fname1;

        //File file = new File(myDir, fname);
        Log.i(TAG, "" + file);
        if (file.exists()){
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.i(TAG, "saveBitmapToDevice : "+e);
            e.printStackTrace();
            return null;
        }
        Log.i(TAG, directory+fname);
        return directory+fname;
    }
    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    String[] appPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    final int PERMISSION_REQUES_CODE = 101;
    public boolean checkAndRequestPermissions(){

        List<String> listPermissinsNeeded = new ArrayList<>();

        for(String perm : appPermissions){

            if (Build.VERSION.SDK_INT >= 23) {
                if(getApplicationContext().checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED){
                    listPermissinsNeeded.add(perm);
                }
            }else{
                if(PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED){
                    listPermissinsNeeded.add(perm);
                }
            }


        }

        if(!listPermissinsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(AddRescue.this,
                    listPermissinsNeeded.toArray(new String[listPermissinsNeeded.size()]),
                    PERMISSION_REQUES_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUES_CODE:{
                Log.i(TAG,"PERMISSION RESULT" );

            }


        }
    }


    private void uploadFileToFirebase(){
        if(mImageUri1 != null){

            Toast.makeText(getApplicationContext(),"Uploading Main Picture",Toast.LENGTH_SHORT).show();

            //final StorageReference fileRef = mStorageRef.child(RingNo+"."+getFileExtention(mImageUri));fname
            final StorageReference fileRef = mStorageRef.child(rescueDB.getID()+"_"+fname1);

            fileRef.putFile(mImageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    //Toast.makeText(getApplicationContext(),"SUCCESSFULLY ADDED",Toast.LENGTH_LONG).show();

                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String propicURL = uri.toString();
                            Log.i(TAG,"DOWNLOAD URL = "+propicURL);
                            //addToFirebaseDatabase(propicURL);
                            rescueDB.setPictureName(propicURL);

                            mDatabaseRef.child(""+rescueDB.getID()).setValue(rescueDB);

                            Log.i(TAG,"PIC URL = "+propicURL);


                            //resetAll();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"UPLOAD FAILED",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "IMG 1 UPLOAD FAILED : "+e);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    PB_loading.setProgress((int)progress);
                }
            });

        }

    }

    private void addToFirebaseDatabase(){

        mDatabaseRef.child(""+rescueDB.getID()).setValue(rescueDB);
        Toast.makeText(getApplicationContext(),"SUCCESSFULLY ADDED",Toast.LENGTH_LONG).show();

        ResetAll();

    }


    private void ResetAll() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                PB_uploading.setProgress(0);
                ET_name.setText("");
                ET_details.setText("");
                ET_phone.setText("");
                ET_name.setHint("Name");
                ET_details.setHint("Details");
                ET_phone.setHint("Phone");

                btn_uploadImage.setEnabled(true);
                btn_rescueDone.setEnabled(true);
                PB_loading.setVisibility(View.INVISIBLE);
            }
        });

    }
}
