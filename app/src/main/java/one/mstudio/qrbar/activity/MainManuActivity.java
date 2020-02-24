package one.mstudio.qrbar.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import one.mstudio.qrbar.R;
import one.mstudio.qrbar.adapter.MainPagerAdapter;
import one.mstudio.qrbar.data.constant.Constants;
import one.mstudio.qrbar.utility.AdManager;
import one.mstudio.qrbar.utility.AppUtils;

public class MainManuActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmanu);
    }

    public void  Scan_Manual(View view)
    {
        Intent intent = new Intent(MainManuActivity.this, MainActivity.class);
        startActivity(intent);
    }


}
