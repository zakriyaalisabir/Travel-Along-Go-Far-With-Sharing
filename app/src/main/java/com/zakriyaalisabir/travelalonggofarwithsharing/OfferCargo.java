package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OfferCargo extends AppCompatActivity {

    private Button btnS;
    private EditText etT,etF,etFa,etB;

    private String to,from,fare,bagsKg;

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_cargo);

        mRef= FirebaseDatabase.getInstance().getReference("cargoOffered");
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        btnS=(Button)findViewById(R.id.btnSubmitCargoOffered);
        etF=(EditText)findViewById(R.id.etFrom);
        etFa=(EditText)findViewById(R.id.etFare);
        etT=(EditText)findViewById(R.id.etTo);
        etB=(EditText)findViewById(R.id.etBagsKg);

        progressDialog=new ProgressDialog(OfferCargo.this);
        progressDialog.setTitle("Fetching Data from server");
        progressDialog.setMessage("Please wait ....");
        progressDialog.setCancelable(false);

        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                to=etT.getText().toString();
                from=etF.getText().toString();
                fare=etFa.getText().toString();
                bagsKg=etB.getText().toString();

                if(to.isEmpty() || from.isEmpty() || fare.isEmpty() || bagsKg.isEmpty()){
                    Toast.makeText(getApplicationContext(),"invalid info",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }

                ClassForCargoOffered classForCargoOffered=new ClassForCargoOffered(to,from,fare,bagsKg);

                mRef.child(mUser.getUid()).child(from+"2"+to).setValue(classForCargoOffered);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Cargo Offer added to DB successfully",Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}
