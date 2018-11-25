package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FirstTimeLoginForMobileNumberVerification extends AppCompatActivity {

    private Spinner sp;

    private List<String> countryCodesList;
    private ArrayAdapter<String> arrayAdapter;

    private Button btnP,btnC;
    private EditText etCn,etp;

    private String cellNo;
    private String pinAsMsg,enteredPin;

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_login_for_mobile_number_verification);

        sp=(Spinner)findViewById(R.id.spCC);
        btnC=(Button)findViewById(R.id.btnConfirm);
        btnP=(Button)findViewById(R.id.btnProceed);
        etCn=(EditText)findViewById(R.id.etCellNumber);
        etp=(EditText)findViewById(R.id.etPin);

        btnC.setVisibility(View.INVISIBLE);
        etp.setVisibility(View.INVISIBLE);

        mRef= FirebaseDatabase.getInstance().getReference("confiredPhoneNumbers");
        mAuth=FirebaseAuth.getInstance();

        countryCodesList=new ArrayList<String>();
        countryCodesList.add("+92");

        for(int i=1;i<=300;i++){
            if(i==92){
                continue;
            }
            countryCodesList.add("+"+i);
        }

        arrayAdapter=new ArrayAdapter<String>(this,R.layout.my_spinner_item,countryCodesList);
        arrayAdapter.notifyDataSetInvalidated();
        sp.setAdapter(arrayAdapter);

        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cc=sp.getSelectedItem().toString();
                String cn=etCn.getText().toString();

                if(cc.isEmpty() || cn.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Invalid Phone Number",Toast.LENGTH_LONG).show();
                    return;
                }

                cellNo=cc+cn;

                try{
                    SmsManager smsManager=SmsManager.getDefault();

                    Random random=new Random();
                    int a=random.nextInt(9);
                    int b=random.nextInt(9);
                    int c=random.nextInt(9);
                    int d=random.nextInt(9);

                    pinAsMsg=""+a+b+c+d;

                    smsManager.sendTextMessage(cellNo,null,pinAsMsg,null,null);

                    Toast.makeText(getApplicationContext(), "Pin Sent",Toast.LENGTH_LONG).show();

                    etp.setVisibility(View.VISIBLE);
                    btnC.setVisibility(View.VISIBLE);
                    btnP.setText("RESEND PIN");

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Failed to send pin",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        });

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enteredPin=etp.getText().toString();

                if(enteredPin.equals(pinAsMsg)){
                    mRef.child(cellNo).setValue("confirmed");
                    startActivity(new Intent(getApplicationContext(),UploadCnicAfterMobileNumberConfirmation.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Invalid Pin",Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}
