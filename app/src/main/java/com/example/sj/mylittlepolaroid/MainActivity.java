package com.example.sj.mylittlepolaroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity  extends Activity {
    RelativeLayout main;
    TextView click_message;
    Animation animFadein;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = (RelativeLayout)findViewById(R.id.touch);
        click_message=(TextView)findViewById(R.id.Clicktext);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_animation);
        click_message.startAnimation(animFadein);
        main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, menu.class);
                startActivity(intent);
            }
        });
    }




}
