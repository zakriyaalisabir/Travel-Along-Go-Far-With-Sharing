package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PreLogin extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef,mRef1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);
    }
    @Override
    public void onStart() {
        super.onStart();

        progressDialog=new ProgressDialog(PreLogin.this);
        progressDialog.setTitle("Checking Last Login State");

        progressDialog.setMessage("Please wait ....");
        progressDialog.setCancelable(false);
//        progressDialog.show();

        mRef= FirebaseDatabase.getInstance().getReference("confiredPhoneNumbers");
        mRef1= FirebaseDatabase.getInstance().getReference("users");
        mAuth=FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(currentUser==null){
            progressDialog.dismiss();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }

        else if(currentUser!=null){
            progressDialog.show();
            mRef1.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInfo userInfo=dataSnapshot.getValue(UserInfo.class);

                    String cellNo=userInfo.phone;

                    Log.e("cellNo", " = "+cellNo );

                    if(!cellNo.isEmpty()){
                        mRef.child(cellNo).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String confirmationStatus=dataSnapshot.getValue(String.class);
                                Log.e("confirmationStatus", " = "+confirmationStatus );

                                Toast.makeText(getApplicationContext(),"User successfully Loged in ",Toast.LENGTH_LONG).show();

                                if(confirmationStatus.equals("confirmed")){
                                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                                    finish();
                                }else {
                                    startActivity(new Intent(getApplicationContext(),FirstTimeLoginForMobileNumberVerification.class));
                                    finish();
                                }

                                progressDialog.dismiss();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
