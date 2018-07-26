package com.juborajsarker.medicare.activity.more;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.medicare.R;

import java.util.Locale;

public class AmbulanceActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    TextView messageTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);


        init();


        String url = "https://www.google.com/maps/search/ambulance"; ///@23.8639095,90.3896284,15z/data=!3m1!4b1
        setTitle("Ambulance Service");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);



        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                super.onProgressChanged(view, newProgress);

                progressBar.setProgress(newProgress);

                if (newProgress == 100){

                    progressBar.setVisibility(View.GONE);
                }



            }
        });


        try {
            if (getUserCountry(AmbulanceActivity.this).equals("Bangladesh")){

                if (checkConnection()){

                    webView.setVisibility(View.VISIBLE);
                    messageTV.setVisibility(View.GONE);
                    webView.setWebViewClient(new MyWebViewClient());
                    webView.loadUrl(url);

                }else {

                    webView.setVisibility(View.GONE);
                    messageTV.setVisibility(View.VISIBLE);

                }

            }else {

                webView.setVisibility(View.GONE);
                messageTV.setVisibility(View.VISIBLE);
                messageTV.setText("You have tp buy Medicare Pro for this option !!! Tap here to buy Medicare Pro");
            }

        }catch (Exception e){

            Toast.makeText(AmbulanceActivity.this, "Error found", Toast.LENGTH_SHORT).show();
        }







    }


    private void init() {

        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        messageTV = (TextView) findViewById(R.id.messageTV);

        messageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAds();
            }
        });
    }


    private Intent removeAdsIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, "com.juborajsarker.medicarepro" )));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }


    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available

                Locale loc = new Locale("", simCountry);
                return loc.getDisplayCountry();

            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available

                    Locale loc = new Locale("", networkCountry);
                    return loc.getDisplayCountry();
                }
            }
        }
        catch (Exception e) {

            Log.d("error", e.getMessage());
        }
        return null;
    }

    public void removeAds() {
        try {
            Intent rateIntent = removeAdsIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = removeAdsIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                super.onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }


    }




    public class MyWebViewClient  extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean checkConnection(){

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfoWifi.isConnected();
        NetworkInfo networkInfoMobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfoMobile.isConnected();

        if (isWifiConn || isMobileConn){

            return true;

        }else {


            return false;
        }
    }

}
