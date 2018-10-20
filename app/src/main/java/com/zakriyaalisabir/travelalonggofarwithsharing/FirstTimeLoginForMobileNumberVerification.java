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

        countryCodesList=new ArrayList<String>();

        for(int i=1;i<=300;i++){
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
                    int a=random.nextInt();
                    int b=random.nextInt();
                    int c=random.nextInt();
                    int d=random.nextInt();

                    String msg=""+a+b+c+d;

                    smsManager.sendTextMessage(cellNo,null,msg,null,null);

                    Toast.makeText(getApplicationContext(), "Pin Sent",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Failed to send pin",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        });

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UploadCnicAfterMobileNumberConfirmation.class));
            }
        });
    }
}
