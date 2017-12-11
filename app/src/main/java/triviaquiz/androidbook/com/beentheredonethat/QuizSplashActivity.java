package triviaquiz.androidbook.com.beentheredonethat;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuizSplashActivity extends QuizActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        startAnimation();
//        startActivity(new Intent(QuizSplashActivity.this, QuizMenuActivity.class));
//        QuizSplashActivity.this.finish();

    }

    private void startAnimation() {
        // Header shade animation
        TextView header = (TextView) findViewById(R.id.header);
        Animation headerFade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        header.startAnimation(headerFade);

        // Left side spinin animation
        Animation leftSpinin = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        LinearLayout leftLinearLayout = (LinearLayout) findViewById(R.id.leftPics);
        for (int i = 0; i < leftLinearLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) leftLinearLayout.getChildAt(i);
            imageView.setAnimation(leftSpinin);
        }

        // Right side spinin animation
        Animation rightSpinin = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        LinearLayout rightLinearLayout = (LinearLayout) findViewById(R.id.rightPics);
        for (int i = 0; i < rightLinearLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) rightLinearLayout.getChildAt(i);
            imageView.setAnimation(rightSpinin);
        }

        // Footer shade animation
        TextView footer = (TextView) findViewById(R.id.footer);
        Animation footerFade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        footer.startAnimation(footerFade);

        // Set animation listner for main menu
        footerFade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(QuizSplashActivity.this, QuizMenuActivity.class));
                QuizSplashActivity.this.finish();
            }
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

    }
}
