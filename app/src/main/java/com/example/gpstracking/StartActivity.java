package com.example.gpstracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StartActivity extends AppCompatActivity {
   private ImageView iconImage;

   private Button register;
   private Button login;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        iconImage = findViewById(R.id.icon_image);
        linearLayout = findViewById(R.id.linear_layout);
        register = findViewById(R.id.Register);
        login = findViewById(R.id.Login);
        linearLayout.animate().alpha(0f).setDuration(5);
linearLayout.setVisibility(View.INVISIBLE);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -100);
        animation.setDuration(1500);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());
        iconImage.setAnimation(animation);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(StartActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));


            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                startActivity(new Intent(StartActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


    }
    private class MyAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {


        }

        @Override
        public void onAnimationEnd(Animation animation) {
            iconImage.clearAnimation();
            iconImage.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.animate().alpha(1f).setDuration(1000);


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}