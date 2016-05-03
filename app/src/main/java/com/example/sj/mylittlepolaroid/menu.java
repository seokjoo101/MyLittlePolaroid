package com.example.sj.mylittlepolaroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by SJ on 2015-11-27.
 */
public class menu extends Activity {

    ImageButton album;
    ImageButton cam;
    ImageButton location;
    ImageButton selfie;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        album = (ImageButton) findViewById(R.id.album);
        cam = (ImageButton) findViewById(R.id.cam);
        location = (ImageButton) findViewById(R.id.location);
        selfie = (ImageButton) findViewById(R.id.selfie);
        album.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        cam.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        location.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        selfie.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


        album.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, Album.class);
                startActivity(intent);
            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, Cam.class);
                startActivity(intent);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, Map.class);
                startActivity(intent);
            }
        });

        selfie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, Selfie.class);
                startActivity(intent);
            }
        });
    }

}
