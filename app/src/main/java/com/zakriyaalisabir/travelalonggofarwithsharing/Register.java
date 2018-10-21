package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText etN,etE,etPh,etC,etP,etCN,etCM,etCNum,etCity;
    private Button btnR;

    private String name,email,phone,cnic,city,password,carName,carModel,carNumber;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etPh=(EditText)findViewById(R.id.etPhone);
        etN=(EditText)findViewById(R.id.etName);
        etE=(EditText)findViewById(R.id.etEmail);
        etC=(EditText)findViewById(R.id.etCNIC);
        etP=(EditText)findViewById(R.id.etPassword);
        etCN=(EditText)findViewById(R.id.etCarName);
        etCM=(EditText)findViewById(R.id.etCarModel);
        etCNum=(EditText)findViewById(R.id.etCarNumber);
        etCity=(EditText)findViewById(R.id.etCity);

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
                phone=etPh.getText().toString().toUpperCase();
                cnic=etC.getText().toString().toUpperCase();
                password=etP.getText().toString().toUpperCase();
                carName=etCN.getText().toString().toUpperCase();
                carModel=etCM.getText().toString().toUpperCase();
                carNumber=etCNum.getText().toString().toUpperCase();
                city=etCity.getText().toString().toUpperCase();

                if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || cnic.isEmpty() || city.isEmpty() ||password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Incomplete info",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }

                if(phone.length()<13){
                    Toast.makeText(getApplicationContext(),"Enter phone number with country code",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }

                final UserInfo userInfo=new UserInfo(name,email,phone,cnic,city,password,carName,carModel,carNumber);


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"User Successfully Registered",Toast.LENGTH_LONG).show();
                            user=mAuth.getCurrentUser();
                            mRef.child("users").child(user.getUid()).setValue(userInfo);
                            mRef.child("confiredPhoneNumbers").child(userInfo.phone).setValue("notConfirmed");
                            finish();
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
