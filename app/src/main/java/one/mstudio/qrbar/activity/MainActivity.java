package one.mstudio.qrbar.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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

public class MainActivity extends AppCompatActivity {

    private Activity mActivity;
    private Context mContext;

    private ViewPager mViewPager;
    private ArrayList<String> mFragmentItems;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVars();
        initViews();
        initFunctionality();
        initListeners();

    }

    private void initVars() {
        mActivity = MainActivity.this;
        mContext = mActivity.getApplicationContext();
        mFragmentItems = new ArrayList<>();
    }

    private void initViews() {
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

    }

    private void initFunctionality() {
        if ((ContextCompat.checkSelfPermission( mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission( mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                    mActivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSION_REQ);
        } else {
            setUpViewPager();
        }

        // TODO Sample banner Ad implementation
        AdManager.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adView));

    }

    private void initListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_scan:
                        mViewPager.setCurrentItem(0, true);
                        break;
                    case R.id.nav_generate:
                        mViewPager.setCurrentItem(1, true);
                        break;
                    case R.id.nav_history:
                        mViewPager.setCurrentItem(2, true);
                        break;
                }
                return true;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_scan);
                } else if(position == 1) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_generate);
                } else if(position == 2) {
                    bottomNavigationView.setSelectedItemId(R.id.nav_history);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpViewPager() {

        mFragmentItems.add(getString(R.string.menu_scan));
        mFragmentItems.add(getString(R.string.menu_generate));
        mFragmentItems.add(getString(R.string.menu_history));

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragmentItems);
        mViewPager.setAdapter(mainPagerAdapter);
        mainPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Constants.PERMISSION_REQ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpViewPager();
            } else {
                AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
            }

        }
    }



    @Override
    public void onBackPressed() {
        AppUtils.tapToExit(mActivity);
    }
}
