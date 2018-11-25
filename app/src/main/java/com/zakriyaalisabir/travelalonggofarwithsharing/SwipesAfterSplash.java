package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SwipesAfterSplash extends AppCompatActivity {

    private Integer[] imagesForSwiping={R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4,R.drawable.s5};
    private ImageView imv;

    private float startXvalue=1;
    private int n=0;

    private Button btnS;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef1,mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipes_after_splash);

        imv=(ImageView)findViewById(R.id.imgSwiper);

        imv.setImageResource(imagesForSwiping[0]);

        btnS=(Button)findViewById(R.id.btnSkipSwiper);

        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PreLogin.class));
                finish();
            }
        });

        int permission= ContextCompat.checkSelfPermission(SwipesAfterSplash.this,Manifest.permission.CAMERA);
        int permission1= ContextCompat.checkSelfPermission(SwipesAfterSplash.this,Manifest.permission.ACCESS_COARSE_LOCATION);
        int permission2= ContextCompat.checkSelfPermission(SwipesAfterSplash.this,Manifest.permission.ACCESS_FINE_LOCATION);
        int permission3= ContextCompat.checkSelfPermission(SwipesAfterSplash.this,Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission4= ContextCompat.checkSelfPermission(SwipesAfterSplash.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if(permission!=PackageManager.PERMISSION_GRANTED ||
                permission1!=PackageManager.PERMISSION_GRANTED||
                permission2!=PackageManager.PERMISSION_GRANTED||
                permission3!=PackageManager.PERMISSION_GRANTED||
                permission4!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.SEND_SMS

                    },
                    0);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float endXvalue=0;
        float x1=event.getAxisValue(MotionEvent.AXIS_X);
        int action= MotionEventCompat.getActionMasked(event);

        switch (action){
            case (MotionEvent.ACTION_DOWN):

                startXvalue = event.getAxisValue(MotionEvent.AXIS_X);
                return true;

            case (MotionEvent.ACTION_UP):

                endXvalue = event.getAxisValue(MotionEvent.AXIS_X);

//                if (endXvalue > startXvalue) {
//                    if (endXvalue - startXvalue > 100) {
////                        System.out.println("Left-Right");
//                        imv.setImageResource(imagesForSwiping[2]);
//                    }
//                    return true;
//                }

                if(startXvalue > endXvalue) {
                    if (startXvalue -endXvalue> 100) {
//                        System.out.println("Right-Left");
                        n++;
                        if(n<imagesForSwiping.length){
                            imv.setImageResource(imagesForSwiping[n]);
                        }
                        if(n==imagesForSwiping.length){
                            startActivity(new Intent(getApplicationContext(),PreLogin.class));
                            finish();
                        }
                    }
                    return true;
                }


            default:
                return super.onTouchEvent(event);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(SwipesAfterSplash.this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SwipesAfterSplash.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
