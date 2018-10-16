package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
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
}
