package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterStep2 extends AppCompatActivity {

    private EditText etCN,etCM,etCNo;
    private Button btnR;

    private DatabaseReference mRef,mRef1;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private UserInfo userInfo;

    private ProgressDialog progressDialog;

    private String carName,carModel,carNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mRef= FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid());

        progressDialog=new ProgressDialog(RegisterStep2.this);
        progressDialog.setTitle("Fetching Data From Server");
        progressDialog.setMessage("Please wait .....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    userInfo=dataSnapshot.getValue(UserInfo.class);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        btnR=(Button)findViewById(R.id.btnRegister) ;
        etCN=(EditText)findViewById(R.id.etCarName);
        etCM=(EditText)findViewById(R.id.etCarModel);
        etCNo=(EditText)findViewById(R.id.etCarNo);

        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carName=etCN.getText().toString().toUpperCase();
                carModel=etCM.getText().toString().toUpperCase();
                carNumber=etCNo.getText().toString().toUpperCase();

                if(carModel.isEmpty() || carNumber.isEmpty() || carName.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Invalid Details",Toast.LENGTH_LONG).show();
                    return;
                }

                mRef.child("carModel").setValue(carModel);
                mRef.child("carName").setValue(carName);
                mRef.child("carNumber").setValue(carNumber);

                startActivity(new Intent(getApplicationContext(),FirstTimeLoginForMobileNumberVerification.class));
                finish();

            }
        });

    }
}
