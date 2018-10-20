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

import java.util.UUID;

public class UploadCnicAfterMobileNumberConfirmation extends AppCompatActivity {

    private ImageView iv;
    private Button btnU,btnC,btnG;

    private Uri mImageUri = null;

    private static final  int GALLERY_REQUEST =1;

    private String picturePath;

    private static final int CAMERA_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_cnic_after_mobile_number_confirmation);

        iv=(ImageView)findViewById(R.id.ivCNIC);
        btnC=(Button)findViewById(R.id.btnCAmera);
        btnU=(Button)findViewById(R.id.btnUpload);
        btnG=(Button)findViewById(R.id.btnGallery);

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


                if(picturePath != null)
                {
                    final ProgressDialog progressDialog = new ProgressDialog(UploadCnicAfterMobileNumberConfirmation.this);
                    progressDialog.setTitle("Uploading Image");
                    progressDialog.setMessage("Please wait ...");
                    progressDialog.show();

                    StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
//            mImageUri = data.getData();
            Bitmap photo=(Bitmap)data.getExtras().get("data");
            iv.setImageBitmap(photo);

        }
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
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
