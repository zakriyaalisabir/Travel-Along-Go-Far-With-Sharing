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
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText etFPE;
    private Button btnFP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btnFP=(Button)findViewById(R.id.btnRequestNewPassword);
        etFPE=(EditText) findViewById(R.id.etEmailFG);

        final ProgressDialog progressDialog=new ProgressDialog(ForgetPassword.this);
        progressDialog.setTitle("Requesting New Password");
        progressDialog.setMessage("Please wait ...");
        progressDialog.setCancelable(false);

        btnFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                String email=etFPE.getText().toString();

                if(email.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Enter an email first",Toast.LENGTH_LONG).show();
                }
                else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Password request is successfully send please check your email ",Toast.LENGTH_LONG).show();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Unable to request new password",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
