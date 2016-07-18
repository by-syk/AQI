package com.by_syk.aqi;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.by_syk.aqi.util.Aqi;
import com.by_syk.aqi.util.AqiHelper;
import com.by_syk.aqi.util.C;
import com.by_syk.lib.storage.SP;
import com.by_syk.lib.toast.GlobalToast;

public class MainActivity extends Activity {
    private SP sp;

    private TextView tvAqiGrade;
    private TextView tvAdvice;

    private Aqi aqi = null;

    private AlertDialog alertDialog = null;

    private boolean is_window_visible = false;

    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        (new LoadAqiTask()).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();

        is_window_visible = true;
        Log.d("AQI", "true");
    }

    @Override
    protected void onStop() {
        super.onStop();

        is_window_visible = false;
        Log.d("AQI", "false");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    private void init() {
        sp = new SP(this, false);

        tvAqiGrade = (TextView) findViewById(R.id.tv_aqi_grade);
        tvAdvice = (TextView) findViewById(R.id.tv_advice);

        tvAqiGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalToast.showToast(MainActivity.this, AqiHelper.getAqiAndAqiRange(aqi));
            }
        });
    }

    private class LoadAqiTask extends AsyncTask<String, Integer, Aqi> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            findViewById(R.id.pb_loading).setVisibility(View.VISIBLE);
        }

        @Override
        protected Aqi doInBackground(String... params) {
            return AqiHelper.getAqiData(sp.getString("city"));
        }

        @Override
        protected void onPostExecute(Aqi aqi) {
            super.onPostExecute(aqi);

            findViewById(R.id.pb_loading).setVisibility(View.GONE);

            if (aqi == null) {
                return;
            }
            MainActivity.this.aqi = aqi;

            if (!is_window_visible) { // App不在前台，以消息提示
                GlobalToast.showToast(MainActivity.this,
                        getString(R.string.toast_air_quality, aqi.getQuality()));
                return;
            }

            setHead(aqi.getCity());
            if (aqi.getQuality() != null) {
                tvAqiGrade.setText(aqi.getQuality());
            }
            if (aqi.getColor() != Color.TRANSPARENT) {
                findViewById(R.id.rl_color).setBackgroundColor(aqi.getColor());
                /*colorBoard.startAnimation(AnimationUtils
                        .loadAnimation(MainActivity.this, R.anim.alpha_in));*/
            }
            if (aqi.getAdvice() != null) {
                tvAdvice.setText(aqi.getAdvice());
            }

            checkCity(aqi.getCity());
        }
    }

    @TargetApi(11)
    private void setHead(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        if (C.SDK >= 11) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle(text);
            }
        } else {
            setTitle(text);
        }
    }

    private void checkCity(String city) {
        if (!TextUtils.isEmpty(sp.getString("city"))) {
            return;
        }

        ViewGroup viewGroup = (ViewGroup) getLayoutInflater()
                .inflate(R.layout.dialog_set_city, null);
        final EditText ET_CITY = (EditText) viewGroup.findViewById(R.id.et_city);
        if (city != null) {
            ET_CITY.setText(city);
            ET_CITY.setSelection(ET_CITY.getText().length());
        }
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.dia_title_where)
                .setView(viewGroup)
                .setPositiveButton(R.string.dia_bt_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = ET_CITY.getText().toString();
                        sp.save("city", text);
                    }
                })
                .create();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog != null) {
                    alertDialog.show();
                }
            }
        }, 1200);
    }
}
