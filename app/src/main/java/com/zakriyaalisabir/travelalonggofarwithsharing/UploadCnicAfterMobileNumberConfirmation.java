package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class UploadCnicAfterMobileNumberConfirmation extends AppCompatActivity {

    private ImageView iv;
    private Button btnU,btnC,btnG;

    private Uri filePath = null;

    private static final  int GALLERY_REQUEST =2;

    private String picturePath;

    private static final int CAMERA_REQUEST_CODE=1;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mRef;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_cnic_after_mobile_number_confirmation);

        iv=(ImageView)findViewById(R.id.ivCNIC);
        btnC=(Button)findViewById(R.id.btnCAmera);
        btnU=(Button)findViewById(R.id.btnUpload);
        btnG=(Button)findViewById(R.id.btnGallery);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        mRef= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent,CAMERA_REQUEST_CODE);
                }

            }
        });


        btnG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALLERY_REQUEST);

            }
        });

        btnU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(filePath==null){
                    Toast.makeText(getApplicationContext(),"please use camera to capture your cnic or select from gallery to upload",Toast.LENGTH_LONG).show();
                    return;
                }


                if(filePath != null)
                {
                    final ProgressDialog progressDialog = new ProgressDialog(UploadCnicAfterMobileNumberConfirmation.this);
                    progressDialog.setTitle("Uploading Image");
                    progressDialog.setMessage("Please wait ...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    StorageReference ref = storageReference.child("cnicImages/"+ user.getUid().toString());

                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(UploadCnicAfterMobileNumberConfirmation.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(UploadCnicAfterMobileNumberConfirmation.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");

                                    if(progress>=100){
//                                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                                        finish();
                                    }
                                }
                            });
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data!=null){

            Bitmap photo=(Bitmap)data.getExtras().get("data");
            iv.setImageBitmap(photo);

            ByteArrayOutputStream bytes=new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG,100,bytes);

            String path= MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),photo,""+user.getUid(),null);

            filePath=Uri.parse(path);

            Toast.makeText(getApplicationContext(),"camera filePath = "+filePath,Toast.LENGTH_LONG).show();
            return;

        }
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();

            filePath=data.getData();

            Toast.makeText(getApplicationContext(),"gallery filePath = "+filePath,Toast.LENGTH_LONG).show();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            iv.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }
}
