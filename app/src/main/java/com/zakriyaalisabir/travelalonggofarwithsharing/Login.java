package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private Button btnL,btnR;
    private TextView tvFP;
    private EditText etE,etP;

    private String e,p;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef,mRef1;
    private FirebaseUser user;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnL=(Button)findViewById(R.id.btnLogin);
        btnR=(Button)findViewById(R.id.btnRegister);
        tvFP=(TextView)findViewById(R.id.tvForgetPassword);
        etE=(EditText) findViewById(R.id.etEmail);
        etP=(EditText) findViewById(R.id.etPassword);

        mAuth=FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference("confiredPhoneNumbers");
        mRef1= FirebaseDatabase.getInstance().getReference("users");

        progressDialog=new ProgressDialog(Login.this);
        progressDialog.setTitle("Signing in ");
        progressDialog.setMessage("Please wait ....");
        progressDialog.setCancelable(false);
//        progressDialog.show();

        int permission= ContextCompat.checkSelfPermission(Login.this, Manifest.permission.CAMERA);
        int permission1= ContextCompat.checkSelfPermission(Login.this,Manifest.permission.ACCESS_COARSE_LOCATION);
        int permission2= ContextCompat.checkSelfPermission(Login.this,Manifest.permission.ACCESS_FINE_LOCATION);
        int permission3= ContextCompat.checkSelfPermission(Login.this,Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission4= ContextCompat.checkSelfPermission(Login.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if(permission!= PackageManager.PERMISSION_GRANTED ||
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


        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                e=etE.getText().toString();
                p=etP.getText().toString();

                if(e.isEmpty() || p.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Invalid Info",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }

                mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            user=mAuth.getCurrentUser();

                            mRef1.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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

                        }else {
                            Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });


            }
        });

        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        tvFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgetPassword.class));
            }
        });

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
                    Toast.makeText(Login.this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Login.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
