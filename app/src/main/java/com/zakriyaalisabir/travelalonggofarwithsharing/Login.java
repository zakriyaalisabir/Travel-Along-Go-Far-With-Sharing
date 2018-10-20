package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private Button btnL,btnR;
    private TextView tvFP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnL=(Button)findViewById(R.id.btnLogin);
        btnR=(Button)findViewById(R.id.btnRegister);

        tvFP=(TextView)findViewById(R.id.tvForgetPassword);

        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FirstTimeLoginForMobileNumberVerification.class));
                finish();
            }
        });

        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
            }
        });
    }
}
