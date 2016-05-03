package com.example.sj.mylittlepolaroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by SJ on 2015-11-27.
 */
public class Polaroid extends Activity {

    private static final String TAG = "SEOK";

    ImageView w1;
    TextView w2;
    TextView w3;
    TextView w4;
    Intent intent=null;
    int position;

    String p;

    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polaroid);

        intent = getIntent();
        position =intent.getIntExtra("p", -1);

        Log.i(TAG,"p�� "+position);

        w1=(ImageView)findViewById(R.id.r1);
        w2=(TextView)findViewById(R.id.r2);
        w3=(TextView)findViewById(R.id.r3);
        w4=(TextView)findViewById(R.id.r4);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        mInfoArray = new ArrayList<InfoClass>();

        mCursor = mDbOpenHelper.getColumn(position);
        p = mCursor.getString(mCursor.getColumnIndex("image"));
        Bitmap cc = DecodeBitmapFile(p);
        w1.setImageBitmap(cc);
        w2.setText(mCursor.getString(mCursor.getColumnIndex("date")));
        w3.setText( mCursor.getString(mCursor.getColumnIndex("address")));
        w4.setText( mCursor.getString(mCursor.getColumnIndex("explain")));

        doWhileCursorToArray();
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

    // ������ ���� ��η� ��Ʈ�� �������
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




}
