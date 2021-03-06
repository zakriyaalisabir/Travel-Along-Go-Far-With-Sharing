package com.zakriyaalisabir.travelalonggofarwithsharing;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DirectedAcyclicGraph;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostARide extends AppCompatActivity /*implements OnMapReadyCallback*/ {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private boolean gpsEnabled;
    private Location gpsLoc;

    private ArrayList<LatLng> arrayList;

    private Button btnS;
    private EditText etT,etF,etFa,etS;

    private String to,from,fare,seats;

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_aride);

        mRef=FirebaseDatabase.getInstance().getReference("ridesPosted");
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        btnS=(Button)findViewById(R.id.btnSubmitRide);
        etF=(EditText)findViewById(R.id.etFrom);
        etFa=(EditText)findViewById(R.id.etFare);
        etT=(EditText)findViewById(R.id.etTo);
        etS=(EditText)findViewById(R.id.etSeatsAvailable);

        progressDialog=new ProgressDialog(PostARide.this);
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
                seats=etS.getText().toString();

                if(to.isEmpty() || from.isEmpty() || fare.isEmpty() || seats.isEmpty()){
                    Toast.makeText(getApplicationContext(),"invalid info",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }

                ClassForPostARide classForPostARide=new ClassForPostARide(to,from,fare,seats);

                mRef.child(mUser.getUid()).child(from+"2"+to).setValue(classForPostARide);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Ride added to DB successfully",Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        mMap = googleMap;
//
//        arrayList=new ArrayList<LatLng>();
//
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        if (gpsEnabled) {
//            if (ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            Toast.makeText(getApplicationContext(),"myLoc = "+gpsLoc,Toast.LENGTH_LONG).show();
//        }
//        mMap.setMyLocationEnabled(true);
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setRotateGesturesEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.setTrafficEnabled(true);
//
//        LatLng latLng=new LatLng(gpsLoc.getLatitude(),gpsLoc.getLongitude());
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                if (arrayList.size()>1){
//                    arrayList.clear();
//                    mMap.clear();
//                }
//                arrayList.add(latLng);
//
//                MarkerOptions markerOptions=new MarkerOptions();
//                markerOptions.position(latLng);
//
//                if (arrayList.size() == 1) {
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                }else {
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//                }
//                mMap.addMarker(markerOptions);
//
//                if (arrayList.size()>=2){
//                    String url=getDirectionstUrl(arrayList.get(0),arrayList.get(1));
//                    DownloadTask downloadTask=new DownloadTask();
//
//                    downloadTask.execute(url);
//
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(0)));
//                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//                }
//            }
//        });
//
//    }
//
//    private String getDirectionstUrl(LatLng origin, LatLng destination) {
//        String strOrigin="origin="+origin.latitude+","+origin.longitude;
//        String strDestination="destination="+destination.latitude+","+destination.longitude;
//        String sensor="sensor=false";
//        String mode="mode=driving";
//
//        String params=strOrigin+"&"+strDestination+"&"+sensor+"&"+mode;
//
//        String output="json";
//
//
//        String url="https://maps.googleapis.com/maps/api/directions/"+output+"?"+params;
//
//        return url;
//    }
//
//    private String requestDirection(String reqUrl) throws IOException {
//
//        String responseString="";
//        InputStream inputStream=null;
//        HttpURLConnection httpURLConnection=null;
//        try {
//            URL url=new URL(reqUrl);
//
//            httpURLConnection=(HttpURLConnection)url.openConnection();
//            httpURLConnection.connect();
//
//            inputStream=httpURLConnection.getInputStream();
//            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
//
//            StringBuffer stringBuffer=new StringBuffer();
//            String line="";
//            while ((line=bufferedReader.readLine())!=null){
//                stringBuffer.append(line);
//
//            }
//
//            responseString=stringBuffer.toString();
//            bufferedReader.close();
//            inputStreamReader.close();
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }finally {
//            if(inputStream!=null){
//                inputStream.close();
//            }
//            httpURLConnection.disconnect();
//        }
//
//        return responseString;
//    }
//
//    public class DownloadTask extends AsyncTask<String,Void,String>{
//
//        @Override
//        protected String doInBackground(String... url) {
//
//            String responseString="";
//            try{
//                responseString=requestDirection(url[0]);
//
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//            return responseString;
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            ParserTask parserTask=new ParserTask();
//            parserTask.execute(result);
//
//        }
//    }
//
//    private class ParserTask extends AsyncTask<String,Integer,List<List<HashMap<String,String>>>>{
//
//
//        @Override
//        protected List<List<HashMap<String,String>>> doInBackground(String... jsonData) {
//
//            JSONObject jsonObject;
//            List<List<HashMap<String,String>>> routes=null;
//
//            try{
//                jsonObject=new JSONObject(jsonData[0]);
//                DirectionsJSONParser parser=new DirectionsJSONParser();
//
//                routes=parser.parse(jsonObject);
//
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            return routes;
//        }
//
//        @Override
//        protected void onPostExecute(List<List<HashMap<String,String>>> result) {
//            ArrayList points = null;
//            PolylineOptions lineOptions = null;
//            MarkerOptions markerOptions = new MarkerOptions();
//
//            for (int i = 0; i < result.size(); i++) {
//                points = new ArrayList();
//                lineOptions = new PolylineOptions();
//
//                List<HashMap<String,String>> path = result.get(i);
//
//                for (int j = 0; j < path.size(); j++) {
//                    HashMap<String,String> point = path.get(j);
//
//                    double lat = Double.parseDouble(point.get("lat"));
//                    double lng = Double.parseDouble(point.get("lng"));
//                    LatLng position = new LatLng(lat, lng);
//
//                    points.add(position);
//                }
//
//                lineOptions.addAll(points);
//                lineOptions.width(12);
//                lineOptions.color(Color.RED);
//                lineOptions.geodesic(true);
//
//            }
//
//            // Drawing polyline in the Google Map for the i-th route
//            mMap.addPolyline(lineOptions);
//        }
//    }
}
