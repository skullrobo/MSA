package one.mstudio.qrbar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import one.mstudio.qrbar.R;
import one.mstudio.qrbar.utility.ActivityUtils;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.splashBody);

        relativeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.getInstance().invokeActivity(SplashActivity.this, MainManuActivity.class, true);
            }
        }, 2000);
    }
}

