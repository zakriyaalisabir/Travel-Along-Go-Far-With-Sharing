package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    private EditText etN,etE,etC,etP,etCity;
    private Button btnR;

    private String name,email,cnic,city,password,accountType,carName,carModel,carNumber;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseUser user;


    private RadioGroup rg;
    private RadioButton rb;
    private int radioSelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etN=(EditText)findViewById(R.id.etName);
        etE=(EditText)findViewById(R.id.etEmail);
        etC=(EditText)findViewById(R.id.etCNIC);
        etP=(EditText)findViewById(R.id.etPassword);
        etCity=(EditText)findViewById(R.id.etCity);
        rg=(RadioGroup)findViewById(R.id.radioGroup);


        btnR=(Button)findViewById(R.id.btnRegisterConfirm);

        mAuth=FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference();


        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog=new ProgressDialog(Register.this);
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Registering A New User");
                progressDialog.setMessage("Please wait ...");
                progressDialog.show();

                name=etN.getText().toString().toUpperCase();
                email=etE.getText().toString().toUpperCase();
                cnic=etC.getText().toString().toUpperCase();
                password=etP.getText().toString().toUpperCase();
                city=etCity.getText().toString().toUpperCase();

                if(name.isEmpty() || email.isEmpty() || cnic.isEmpty() || city.isEmpty() ||password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Incomplete info",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }

                radioSelId=rg.getCheckedRadioButtonId();
                rb=(RadioButton)findViewById(radioSelId);
                accountType=rb.getText().toString();

                final UserInfo userInfo=new UserInfo(name,email,"",cnic,city,password,accountType,"","","");

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"User Successfully Registered",Toast.LENGTH_LONG).show();
                            user=mAuth.getCurrentUser();
                            mRef.child("users").child(user.getUid()).setValue(userInfo);

                            if (accountType.equals("Ride Provider")){
                                startActivity(new Intent(getApplicationContext(),RegisterStep2.class));
                                finish();
                            }else {
                                startActivity(new Intent(getApplicationContext(),FirstTimeLoginForMobileNumberVerification.class));
                                finish();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });


            }
        });

    }
}
