package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SwipesAfterSplash extends AppCompatActivity {

    private Integer[] imagesForSwiping={R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4,R.drawable.s5};
    private ImageView imv;

    private float startXvalue=1;
    private int n=0;

    private Button btnS;

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
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        int permission= ContextCompat.checkSelfPermission(SwipesAfterSplash.this,Manifest.permission.CAMERA);
        int permission1= ContextCompat.checkSelfPermission(SwipesAfterSplash.this,Manifest.permission.ACCESS_COARSE_LOCATION);
        int permission2= ContextCompat.checkSelfPermission(SwipesAfterSplash.this,Manifest.permission.ACCESS_FINE_LOCATION);
        int permission3= ContextCompat.checkSelfPermission(SwipesAfterSplash.this,Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission4= ContextCompat.checkSelfPermission(SwipesAfterSplash.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        if(permission1!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        if(permission2!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        if(permission3!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        if(permission4!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);

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
                    Toast.makeText(SwipesAfterSplash.this, "Permission granted to use your CAMERA", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SwipesAfterSplash.this, "Permission denied to use your CAMERA", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(SwipesAfterSplash.this, "Permission granted to use course LOCATION service", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SwipesAfterSplash.this, "Permission denied to use course LOCATION service", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 2: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(SwipesAfterSplash.this, "Permission granted to use fine LOCATION service", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SwipesAfterSplash.this, "Permission denied to use fine LOCATION service", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 3: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(SwipesAfterSplash.this, "Permission granted to read your External storage", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SwipesAfterSplash.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 4: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(SwipesAfterSplash.this, "Permission granted to write on your External storage", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SwipesAfterSplash.this, "Permission denied to write on your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
