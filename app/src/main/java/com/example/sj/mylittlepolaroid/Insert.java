package com.example.sj.mylittlepolaroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by SJ on 2015-12-02.
 */
public class Insert extends Activity {
    private static final String TAG = "SEOK";
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArray;
    Context context;
    String fullPath;
    String picture_time;
    String picture_address;
    double picture_lat;
    double picture_lon;
    String path;


    final int REQ_CODE_SELECT_IMAGE=100;
    ImageView select;
    Button save;
    ImageButton insects;

    EditText y3;
    Bitmap selPhoto ;
    Bitmap cibal;
    Uri selPhotoUri;
    String c="A";
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert);

        select = (ImageView)findViewById(R.id.i1);
        save = (Button)findViewById(R.id.adding);
        y3 = (EditText)findViewById(R.id.t3);

        intent = getIntent();
        c = intent.getExtras().getString("p");
        Bundle bundle = getIntent().getExtras();
        picture_lat = bundle.getDouble("Key_lat");
        picture_lon = bundle.getDouble("Key_lon");
        picture_time = intent.getExtras().getString("t");
        picture_address = findAddress(picture_lat, picture_lon);
        TextView picture_address_textview = (TextView)findViewById(R.id.pic_add);
        TextView picture_time_textview = (TextView)findViewById(R.id.pic_time);

        picture_time_textview.setText(picture_time);
        picture_address_textview.setText(picture_address);


        fullPath = c;

        Bitmap b = DecodeBitmapFile(fullPath);
        ImageView image = (ImageView)findViewById(R.id.i1);
        image.setImageBitmap(b);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mInfoArray = new ArrayList<InfoClass>();
        doWhileCursorToArray();
        for (InfoClass i : mInfoArray) {
            Log.i(TAG, "ID = " + i._id);
            Log.i(TAG, "Image = " + i.image);
            Log.i(TAG, "date = " + i.date);
            Log.i(TAG, "address = " + i.address);
            Log.i(TAG, "explain = " + i.explain);
            Log.i(TAG, "x = " + i.x);
            Log.i(TAG, "x = " + i.y);
        }
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                mDbOpenHelper.insertColumn(fullPath, picture_time, picture_address, y3.getText().toString().trim(),picture_lat,picture_lon);



                Intent intent = new Intent(Insert.this, Album.class);
                startActivity(intent);
                finish();
            }
        });


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
            Toast.makeText(this, "주소취득 실패"   , Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return bf.toString();
    }
}
