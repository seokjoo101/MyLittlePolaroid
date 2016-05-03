package com.example.sj.mylittlepolaroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Map extends FragmentActivity {

    private static final String TAG = "SEOK";

    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArray;


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private ImageView civ;
    private Context mContext;
    private Marker mk;

    Cursor aCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        setUpMapIfNeeded();
        mContext=Map.this;

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mInfoArray = new ArrayList<InfoClass>();
        doWhileCursorToArray();


        Log.i(TAG, findAddress(37.691722, 129.032683)); //정동진
        Log.i(TAG, findAddress(34.302307, 126.527587 )); //해남땅끝마을
        Log.i(TAG, findAddress(38.127083, 128.467272 )); //설악산
        Log.i(TAG, findAddress(35.790376, 129.332135 )); //경주
        Log.i(TAG, findAddress(36.551407, 128.526869 )); //안동화회마을

//        mDbOpenHelper.delete(mDbOpenHelper.mDB);

    }

    private void doWhileCursorToArray() {

        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();
        Log.i(TAG, "COUNT = " + mCursor.getCount());

        while (mCursor.moveToNext()) {

            mInfoClass = new InfoClass(
                    mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getString(mCursor.getColumnIndex("image")),
                    mCursor.getString(mCursor.getColumnIndex("date")),
                    mCursor.getString(mCursor.getColumnIndex("address")),
                    mCursor.getString(mCursor.getColumnIndex("explain")),
                    mCursor.getDouble(mCursor.getColumnIndex("x")),
                    mCursor.getDouble(mCursor.getColumnIndex("y"))
            );

            mInfoArray.add(mInfoClass);
        }

        mCursor.close();

    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {



        // 시작위치
        LatLng address = new LatLng(37,128);
        CameraPosition cp = new CameraPosition.Builder().target((address )).zoom(5).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));


        mDbOpenHelper = new DbOpenHelper(Map.this);
        mDbOpenHelper.open();
        aCursor = null;
        aCursor = mDbOpenHelper.getAllColumns();
        while (aCursor.moveToNext()) {

            String address1 = aCursor.getString(aCursor.getColumnIndex("address"));
            double x = aCursor.getDouble(aCursor.getColumnIndex("x"));
            double y = aCursor.getDouble(aCursor.getColumnIndex("y"));

            mMap.addMarker(new MarkerOptions().position(new LatLng(x, y)).title(address1));


        }
        aCursor.close();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String title = marker.getTitle();
                aCursor=null;
                aCursor = mDbOpenHelper.getAllColumns();

                while (aCursor.moveToNext()) {

                    String address1 = aCursor.getString(aCursor.getColumnIndex("address"));
                    if(title.equals(address1)){
                        int a = aCursor.getInt(mCursor.getColumnIndex("_id"));
                        Intent intent = new Intent(Map.this, Polaroid.class);
                        intent.putExtra("p",a);
                        Log.i("Map to Polaroid",""+a);
                        startActivity(intent);
                    }

                }

            }
        });

    }

    public Bitmap DecodeBitmapFile(String strFilePath) {

        final int IMAGE_MAX_SIZE = 1024;

        File file = new File(strFilePath);

        if (file.exists() == false) {
            return null;
        }

        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inJustDecodeBounds = true;


        BitmapFactory.decodeFile(strFilePath, bfo);

        if (bfo.outHeight * bfo.outWidth >= IMAGE_MAX_SIZE * IMAGE_MAX_SIZE) {
            bfo.inSampleSize = (int) Math.pow(2,
                    (int) Math.round(Math.log(IMAGE_MAX_SIZE
                            / (double) Math.max(bfo.outHeight, bfo.outWidth))
                            / Math.log(0.5)));
        }
        bfo.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(strFilePath, bfo);

        return bitmap;
    }
    private String findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();

                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress);
                }
            }

        } catch (IOException e) {
            Toast.makeText(this, "주소취득 실패", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return bf.toString();
    }
}