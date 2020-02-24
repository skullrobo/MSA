package one.mstudio.qrbar.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;

import java.util.ArrayList;

import one.mstudio.qrbar.R;
import one.mstudio.qrbar.data.constant.Constants;
import one.mstudio.qrbar.data.preference.AppPreference;
import one.mstudio.qrbar.data.preference.PrefKey;
import one.mstudio.qrbar.utility.AdManager;
import one.mstudio.qrbar.utility.AppUtils;


public class ResultActivity extends AppCompatActivity {

    private Activity mActivity;
    private Context mContext;

    private TextView result, actionText;
    private ImageView actionIcon;

    private LinearLayout buttonCopy, buttonSearch, buttonShare, buttonAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVars();
        initViews();
        initFunctionality();
        initListeners();
    }

    private void initVars() {
        mActivity = ResultActivity.this;
        mContext = mActivity.getApplicationContext();
    }

    private void initViews() {
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        result = (TextView) findViewById(R.id.result);
        actionText = (TextView) findViewById(R.id.actionText);
        actionIcon = (ImageView) findViewById(R.id.actionIcon);

        buttonCopy = (LinearLayout) findViewById(R.id.buttonCopy);
        buttonSearch = (LinearLayout) findViewById(R.id.buttonSearch);
        buttonShare = (LinearLayout) findViewById(R.id.buttonShare);
        buttonAction = (LinearLayout) findViewById(R.id.buttonAction);
    }

    private void initFunctionality() {

        getSupportActionBar().setTitle(getString(R.string.result));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayList<String> arrayList = AppPreference.getInstance(mContext).getStringArray(PrefKey.RESULT_LIST);
        String lastResult = arrayList.get(arrayList.size() - 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result.setText(Html.fromHtml(lastResult, Html.FROM_HTML_MODE_LEGACY));
        } else {
            result.setText(Html.fromHtml(lastResult));
        }
        result.setMovementMethod(LinkMovementMethod.getInstance());

        // TODO Sample fullscreen Ad implementation
        // fullscreen ad
       // AdManager.getInstance(mContext).loadFullScreenAd(mActivity);
       // AdManager.getInstance(mContext).getInterstitialAd().setAdListener(new AdListener() {
         //   @Override
       //     public void onAdLoaded() {
       //         super.onAdLoaded();
        //        AdManager.getInstance(mContext).showFullScreenAd();
        //    }
      //  });

        int type = AppUtils.getResourceType(lastResult);
        if (type == Constants.TYPE_TEXT) {
            buttonAction.setVisibility(View.GONE);
        } else if (type == Constants.TYPE_WEB) {
            actionIcon.setImageResource(R.drawable.ic_web);
            actionText.setText(getString(R.string.action_visit));
        } else if (type == Constants.TYPE_YOUTUBE) {
            actionIcon.setImageResource(R.drawable.ic_video);
            actionText.setText(getString(R.string.action_youtube));
        } else if (type == Constants.TYPE_PHONE) {
            actionIcon.setImageResource(R.drawable.ic_call);
            actionText.setText(getString(R.string.action_call));
        } else if (type == Constants.TYPE_EMAIL) {
            actionIcon.setImageResource(R.drawable.ic_email);
            actionText.setText(getString(R.string.action_email));
        }
    }

    private void initListeners() {
        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.copyToClipboard(mContext, result.getText().toString());
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.searchInWeb(mActivity, result.getText().toString());
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.share(mActivity, result.getText().toString());
            }
        });

        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.executeAction(mActivity, result.getText().toString(), AppUtils.getResourceType(result.getText().toString()));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == Constants.PERMISSION_REQ) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", result.getText().toString(), null)));
            }
        } else {
            AppUtils.showToast(mContext, getString(R.string.permission_not_granted));
        }
    }

}

