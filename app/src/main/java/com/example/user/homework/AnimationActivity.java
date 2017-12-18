package com.example.user.homework;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class AnimationActivity extends AppCompatActivity {

    final String TAG = "AnimationTest";
    ImageView res;
    int mScreenWidth;
    ImageView bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        res = (ImageView)findViewById(R.id.rest);
        bike = (ImageView)findViewById(R.id.bike);

    }

    @Override
    protected void onStart() {
        super.onStart();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenWidth = displaymetrics.widthPixels;

        startcarObjectPropertyAnimation();
    }

    private void startcarObjectPropertyAnimation(){

        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(bike, "translationX",
                0, -mScreenWidth*2);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(bike,"scaleX",1,1);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(bike,"scaleY",1,1);

        ObjectAnimator positionAnimator1 = ObjectAnimator.ofFloat(bike, "translationX",
                mScreenWidth*2,-mScreenWidth*1/2);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(positionAnimator,scaleXAnimator,scaleYAnimator);

        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.play(animatorSet).before(positionAnimator1);

        animatorSet1.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet1.setStartDelay(200);
        animatorSet1.setDuration(1800);

        ObjectAnimator res_scaleXAnimator = ObjectAnimator.ofFloat(res,"scaleX",0,1);
        ObjectAnimator res_scaleYAnimator = ObjectAnimator.ofFloat(res,"scaleY",0,1);
        ObjectAnimator res_alphaAnimator = ObjectAnimator.ofFloat(res,"alpha",0,1);


        AnimatorSet animatorSet_res = new AnimatorSet();
        animatorSet_res.playTogether(res_scaleXAnimator,res_scaleYAnimator);
        // animatorSet_res.setStartDelay(500);
        animatorSet_res.setDuration(4000);
        animatorSet_res.addListener(animatorListener);

        animatorSet1.start();
        animatorSet_res.start();
    }

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            Log.i(TAG, "onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            Log.i(TAG, "onAnimationEnd");
            finish();
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            Log.i(TAG, "onAnimationCancel");
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            Log.i(TAG, "onAnimationRepeat");
        }
    };
}
