package one.mstudio.qrbar.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import one.mstudio.qrbar.R;
import one.mstudio.qrbar.data.constant.Constants;
import one.mstudio.qrbar.utility.AppUtils;
import one.mstudio.qrbar.utility.CodeGenerator;
import one.mstudio.qrbar.utility.SaveImage;

public class GenerateFragment extends Fragment {

    private Activity mActivity;
    private Context mContext;

    private EditText inputText;
    private ImageView outputBitmap;
    private ImageButton switcher;
    private FloatingActionButton save, share;

    private static final int TYPE_QR = 0, TYPE_BAR = 1;
    private static int TYPE = TYPE_QR;

    private Bitmap output;
    private String inputStr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_generate, container, false);


        initView(rootView);
        initFunctionality();
        initListener();

        return rootView;
    }

    private void initVar() {
        mActivity = getActivity();
        mContext = mActivity.getApplicationContext();


    }

    private void initView(View rootView) {
        inputText = (EditText) rootView.findViewById(R.id.inputText);
        outputBitmap = (ImageView) rootView.findViewById(R.id.outputBitmap);
        switcher = (ImageButton) rootView.findViewById(R.id.switcher);
        save = (FloatingActionButton) rootView.findViewById(R.id.save);
        share = (FloatingActionButton) rootView.findViewById(R.id.share);

        save.hide();
        share.hide();
    }

    private void initFunctionality() {


    }

    private void initListener() {
        inputText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    generateCode(s.toString());
                } else {
                    save.hide();
                    share.hide();
                    if(TYPE == TYPE_QR) {
                        outputBitmap.setImageResource(R.drawable.ic_qr_placeholder);
                    } else {
                        outputBitmap.setImageResource(R.drawable.ic_bar_placeholder);
                    }
                }
            }
        });

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText("");
                save.hide();
                share.hide();
                if(TYPE == TYPE_QR) {
                    TYPE = TYPE_BAR;
                    switcher.setImageResource(R.drawable.ic_qr_button);
                    inputText.setHint(R.string.type_here_bar);
                    outputBitmap.setImageResource(R.drawable.ic_bar_placeholder);
                } else {
                    TYPE = TYPE_QR;
                    switcher.setImageResource(R.drawable.ic_barcode_button);
                    inputText.setHint(R.string.type_here_qr);
                    outputBitmap.setImageResource(R.drawable.ic_qr_placeholder);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndShare(false, inputStr, output);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndShare(true, inputStr, output);
            }
        });

    }

    private void generateCode(final String input) {
        CodeGenerator codeGenerator = new CodeGenerator();
        if(TYPE == TYPE_BAR) {
            codeGenerator.generateBarFor(input);
        } else {
            codeGenerator.generateQRFor(input);
        }
        codeGenerator.setResultListener(new CodeGenerator.ResultListener() {
            @Override
            public void onResult(Bitmap bitmap) {
                //((BitmapDrawable)outputBitmap.getDrawable()).getBitmap().recycle();
                output = bitmap;
                inputStr = input;
                outputBitmap.setImageBitmap(bitmap);

                if(!save.isShown()) {
                    save.show();
                    share.show();
                }
            }
        });
        codeGenerator.execute();
    }

    private void saveAndShare(final boolean shouldShare, String name, Bitmap bitmap) {

        if(shouldShare) {
            AppUtils.showToast(mContext, getString(R.string.preparing));
        } else {
            AppUtils.showToast(mContext, getString(R.string.saving));
        }
        SaveImage saveImage = new SaveImage(name, bitmap);
        saveImage.setSaveListener(new SaveImage.SaveListener() {
            @Override
            public void onSaved(String savedTo) {

                if (shouldShare) {
                    AppUtils.shareImage(mActivity, savedTo);
                } else {
                    AppUtils.showToast(mContext, getString(R.string.saved_to) +"'"+ Constants.SAVE_TO +"' "+getString(R.string.directory_in));
                }
            }
        });
        saveImage.execute();
    }


}
