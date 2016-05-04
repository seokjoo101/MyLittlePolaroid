package com.example.sj.mylittlepolaroid;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SJ on 2015-11-27.
 */

//Git test
public class Album extends Activity {
    private int branch_create;

    private int gitCommit_Album;
    private DbOpenHelper mDbOpenHelper;
    private SQLiteDatabase db;
    private Cursor mCursor;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArray;
    private static final String TAG = "SEOK";
    GridView gridview;
    int[] album_id;
    Spinner spinner;
    ImageAdapter adapter;
    Button select;
    EditText search_keyword;
    RadioGroup rg;
    int temp;
    int search_flag;

    String arr[] = {"없음","최신 순 정렬","오래된 순 정렬","장소 순 정렬"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);

        rg = (RadioGroup)findViewById(R.id.radio_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                          @Override
                                          public void onCheckedChanged(RadioGroup group, int checkedId) {
                                              if(checkedId == R.id.location_radio)
                                                  search_flag =1;
                                              else if(checkedId == R.id.memo_radio)
                                                  search_flag =0;
                                          }
             });
        album_id = new int[30];
        for(int i = 0; i<30; i++)
            album_id[i] = i+1;
        temp=0;

        select=(Button)findViewById(R.id.select);
        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter2 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arr);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);

        select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                temp = 4;
                adapter = new ImageAdapter(Album.this);
                gridview.setAdapter(adapter);
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                //최신 순
                if (position == 1) {
                    temp = 1;
                    adapter = new ImageAdapter(Album.this);
                    gridview.setAdapter(adapter);
                }
                //오래된 순
                else if (position == 2) {
                    temp = 2;
                    adapter = new ImageAdapter(Album.this);
                    gridview.setAdapter(adapter);

                }
                //장소 순
                else if (position == 3) {
                    temp = 3;
                    adapter = new ImageAdapter(Album.this);
                    gridview.setAdapter(adapter);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();


        mInfoArray = new ArrayList<InfoClass>();

//        mDbOpenHelper.insertColumn("/storage/emulated/0/DCIM/insect/a1.jpg", "2015.02.01 16:44", "미국", "브라질 친구들이랑 교환학생 시절", 41.834398, -87.627554);
//        mDbOpenHelper.insertColumn("/storage/emulated/0/DCIM/insect/a2.jpg", "2015.03.03 15:44", "대한민국 서울특별시 중구 을지로 281", "DDP 처음 가서 신난 나 ㅋㅋ", 37.566836, 127.009431);
//        mDbOpenHelper.insertColumn("/storage/emulated/0/DCIM/insect/a3.jpg", "2014.05.01 12:44", "프랑스", "친구들과 배낭여행 중~", 48.858610, 2.294353);
//        mDbOpenHelper.insertColumn("/storage/emulated/0/DCIM/insect/a4.jpg", "2013.12.07 14:44", "대한민국 부산광역시 해운대구 우2동 1502", "동준이랑 1박2일 기차 여행", 35.168137, 129.132068);
//        mDbOpenHelper.insertColumn("/storage/emulated/0/DCIM/insect/a5.jpg", "2015.02.01 12:44", "일본", "도톤보리 글리코상 앞에서 ><", 34.666129, 135.500476);
//        mDbOpenHelper.insertColumn("/storage/emulated/0/DCIM/insect/a6.jpg", "2015.11.01 20:44", "대한민국 강원도 강릉시 강동면 정동역길 13", "정동진 새벽기차 타고 당일치기 여행!", 37.691722, 129.032683);
//        mDbOpenHelper.insertColumn("/storage/emulated/0/DCIM/insect/a7.jpg", "2014.07.03 15:44", "대한민국 전라남도 해남군 송지면 송호리 산", "해남 땅끝에서 한컷", 34.302307, 126.527587);
//        mDbOpenHelper.insertColumn("/storage/emulated/0/DCIM/insect/a8.jpg", "2014.05.02 12:41", "대한민국 강원도 인제군 북면 용대리", "설악산 등산 ㅎㅎ", 38.127083, 128.467272);
//        mDbOpenHelper.insertColumn("/storage/emulated/0/DCIM/insect/a9.jpg", "2013.12.07 15:42", "대한민국 경상북도 경주시 진현동 16", "가족들과 경주 불국사 방문!!", 35.790376, 129.332135);
//        mDbOpenHelper.insertColumn("/storage/emulated/0/DCIM/insect/a10.jpg", "2015.02.08 16:43", "대한민국 경상북도 안동시 풍천면 하회리 268", "정감 깊은 하회마을에서", 36.551407, 128.526869);
//        mDbOpenHelper.insertColumn("/storage/emulated/0/DCIM/insect/a11.jpg", "2015.02.02 16:43", "중국", "상하이 호텔방에서", 31.218759, 121.528708);




        doWhileCursorToArray();


        gridview= (GridView)findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setBackgroundResource(R.drawable.album_background_back);
        for (InfoClass i : mInfoArray) {
            Log.i(TAG, "ID = " + i._id);
            Log.i(TAG, "Image = " + i.image);
            Log.i(TAG, "date = " + i.date);
            Log.i(TAG, "address = " + i.address);
            Log.i(TAG, "explain = " + i.explain);
            Log.i(TAG, "x = " + i.x);
            Log.i(TAG, "y = " + i.y);
        }
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent intent = new Intent(Album.this, Polaroid.class);

                intent.putExtra("p", album_id[position]);

                Log.i(TAG, "position" + position);
                Log.i(TAG, "id" + id);
                Log.i(TAG, "view" + v);
                startActivity(intent);
            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {

                CustomDialogClass cdd = new CustomDialogClass(Album.this, album_id[position]);
                cdd.show();
                return true;
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


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        Cursor aCursor;
        private ArrayList<Bitmap> images = new ArrayList<Bitmap>();
        public ImageAdapter(Context c) {

            mDbOpenHelper = new DbOpenHelper(Album.this);
            mDbOpenHelper.open();
            aCursor = null;
            if(temp==0) {
                aCursor = mDbOpenHelper.getAllColumns();
                int i =0;
                while (aCursor.moveToNext()) {
                    album_id[i] =aCursor.getInt(aCursor.getColumnIndex("_id"));
                    String p = aCursor.getString(aCursor.getColumnIndex("image"));
                    Bitmap cc = DecodeBitmapFile(p);
                    images.add(cc);
                    i++;

                }
            }
            else if(temp==1) {
                aCursor = mDbOpenHelper.mDB.rawQuery("SELECT * FROM polaroid ORDER BY date desc", null);
                int i = 0;
                while (aCursor.moveToNext()) {
                    album_id[i] =aCursor.getInt(aCursor.getColumnIndex("_id"));
                    String p = aCursor.getString(aCursor.getColumnIndex("image"));

                    Bitmap cc = DecodeBitmapFile(p);

//                    String w = aCursor.getString(aCursor.getColumnIndex("date"));
//                    Log.i(TAG,w);
                    images.add(cc);
                    i++;
                }

            }
            else if(temp==2) {
                aCursor = mDbOpenHelper.mDB.rawQuery("SELECT * FROM polaroid ORDER BY date asc", null);
               int i = 0;
                while (aCursor.moveToNext()) {
                    album_id[i] =aCursor.getInt(aCursor.getColumnIndex("_id"));
                    String p = aCursor.getString(aCursor.getColumnIndex("image"));
                    Bitmap cc = DecodeBitmapFile(p);

                    String w = aCursor.getString(aCursor.getColumnIndex("date"));
                    Log.i(TAG,w);
                    images.add(cc);
                    i++;
                }

            }
            else if(temp==3) {
                aCursor = mDbOpenHelper.mDB.rawQuery("SELECT * FROM polaroid ORDER BY address asc", null);
                int i = 0;
                while (aCursor.moveToNext()) {
                    album_id[i] =aCursor.getInt(aCursor.getColumnIndex("_id"));
                    String p = aCursor.getString(aCursor.getColumnIndex("image"));
                    Bitmap cc = DecodeBitmapFile(p);
                    String w = aCursor.getString(aCursor.getColumnIndex("address"));
                    Log.i(TAG,p+"\n");
                    images.add(cc);
                    i++;
                }

            }
            else if(temp==4) {
                search_keyword = (EditText)findViewById(R.id.search_text);
                String keyword = search_keyword.getText().toString();
                //search_flag 1이면 location 0이면 memo
                if(search_flag == 1)
                    aCursor = mDbOpenHelper.getMatchAddress(keyword);
                else if(search_flag == 0)
                    aCursor = mDbOpenHelper.getMatchMemo(keyword);

                int i = 0;
                while (aCursor.moveToNext()) {
                    album_id[i] =aCursor.getInt(aCursor.getColumnIndex("_id"));
                    String p = aCursor.getString(aCursor.getColumnIndex("image"));
                    Bitmap cc = DecodeBitmapFile(p);
                    images.add(cc);
                    i++;
                }
            }
            aCursor.close();

            mContext = c;



        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
               // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(350, 450));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(images.get(position));
            imageView.setPadding(30, 30, 30, 30);
            Random r = new Random();
            int rn = r.nextInt(5);
            imageView.setBackgroundResource(R.drawable.album_polaroid_background4);
           // if(rn == 0)
           //     imageView.setBackgroundResource(R.drawable.album_polaroid_background1);
           // else if(rn==1)
           //     imageView.setBackgroundResource(R.drawable.album_polaroid_background2);
           // else if(rn==2)
           //     imageView.setBackgroundResource(R.drawable.album_polaroid_background3);
           // else if(rn==3)
           //     imageView.setBackgroundResource(R.drawable.album_polaroid_background4);
           // else if(rn==4)
           //     imageView.setBackgroundResource(R.drawable.album_polaroid_background);
            return imageView;
        }

    }


    public class CustomDialogClass  extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        public int b;

        public CustomDialogClass(Activity a,int b) {
            super(a, R.style.My_Dialog);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.b=b;


        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.deletedialog);
            yes = (Button) findViewById(R.id.yes);
            no = (Button) findViewById(R.id.no);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.yes:
                    mDbOpenHelper.deleteColumn(b);
                    adapter = new ImageAdapter(Album.this);
                    gridview.setAdapter(adapter);
                    break;
                case R.id.no:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    // 파일의 절대 경로로 비트맵 만들어줌
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
