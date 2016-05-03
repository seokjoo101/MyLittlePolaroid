package com.example.sj.mylittlepolaroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Selfie extends Activity implements Callback {

    Camera cam;
    SurfaceHolder hld;
    int temp;
    int temp2;
    private Camera.Parameters p;
    String path;
    String picture_time;
    double latitude;
    double longitude;
    private GpsInfo gps;
    private TextView txtLat;
    private TextView txtLon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cam);
        SurfaceView view = (SurfaceView) findViewById(R.id.view);
        hld = view.getHolder();
        hld.addCallback(this);
        ImageButton btn = (ImageButton) findViewById(R.id.button);
        btn.setOnClickListener(shootLstn);


        txtLat = (TextView) findViewById(R.id.Latitude);
        txtLon = (TextView) findViewById(R.id.Longitude);




        temp = 1;
        temp2 = 0;

    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        releaseCam();
        cam = Camera.open(temp);

        cam.setDisplayOrientation(90);

    }

    private void releaseCam() {

        if (cam != null) {
            cam.stopPreview();
            cam.release();
            cam = null;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCam();
    }

    View.OnClickListener shootLstn = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {

            if (cam != null)
                cam.takePicture(null, null, pictureCB);
        }
    };




    Camera.PictureCallback pictureCB = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //시간 받아오기
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy.MM.dd HH:mm  ", Locale.KOREA );
            Date currentTime = new Date();
            picture_time = mSimpleDateFormat.format(currentTime);
            //위치 받아오기
            gps = new GpsInfo(Selfie.this);
            if (gps.isGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();


// data[] 로 넘어온 데이터를 bitmap으로 변환
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
// 화면 회전을 위한 matrix객체 생성
                Matrix m = new Matrix();
// matrix객체에 회전될 정보 입력
                m.setRotate(270, (float) bmp.getWidth(), (float) bmp.getHeight());
// 기존에 저장했던 bmp를 Matrix를 적용하여 다시 생성
                Bitmap rotateBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, false);
// 기존에 생성했던 bmp 자원해제
                bmp.recycle();


                Log.i("hi", "cal-kak");
                String sd = Environment.getExternalStorageDirectory().getAbsolutePath();

                path = sd + "/DCIM/insect/" + System.currentTimeMillis() + ".jpg";

                Log.i("path ", path);
                File file = new File(path);
                try {
                    OutputStream outStream = new FileOutputStream(file);
                    rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // File file = new File("/storage/sdcard0/DCIM/test/heye.jpg");
//			try {
//				FileOutputStream fos = new FileOutputStream(file);
//				fos.write(data);
//				fos.flush();
//				fos.close();
//			}
//
//			catch (Exception e) {
//				Log.i("44", "44");
//			}
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.parse("file://" + path);
                intent.setData(uri);
                sendBroadcast(intent);

                startPreview();
                CustomDialogClass cdd = new CustomDialogClass(Selfie.this);
                cdd.show();
// on some system, it is not required
            } else {
                // GPS 를 사용할수 없으므로
                gps.showSettingsAlert();
            }
        }
    };

    private void startPreview() {
        if (cam == null || hld == null)
            return;
        try {
            cam.setPreviewDisplay(hld);
        } catch (IOException e) {
        }
        cam.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (cam != null)
            cam.stopPreview();
    }


    public class CustomDialogClass  extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;

        public CustomDialogClass(Activity a) {
            super(a, R.style.My_Dialog);
            // TODO Auto-generated constructor stub
            this.c = a;


        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.cam_insert);
            yes = (Button) findViewById(R.id.yes);
            no = (Button) findViewById(R.id.no);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.yes:
                    Intent intent = new Intent(Selfie.this, Insert.class);
                    Bundle params = new Bundle();
                    params.putDouble("Key_lat", latitude);
                    params.putDouble("Key_lon", longitude);
                    intent.putExtras(params);
                    intent.putExtra("p",path);
                    intent.putExtra("t",picture_time);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.no:
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

}