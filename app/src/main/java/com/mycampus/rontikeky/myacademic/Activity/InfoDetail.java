package com.mycampus.rontikeky.myacademic.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mycampus.rontikeky.myacademic.R;

public class InfoDetail extends AppCompatActivity {

    public String extrasSlug;
    public String extrasTag;
    public String extrasTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        Intent result= getIntent();
        Bundle extrasResult = result.getExtras();

        if(extrasResult!=null)
        {
            extrasSlug =(String) extrasResult.get("slug_url");
            extrasTitle =(String) extrasResult.get("judul");
            extrasTag =(String) extrasResult.get("html_tag");
        }

        Log.d("xxx",extrasTag);

        final WebView mWebview = (WebView) findViewById(R.id.webview);

        final ProgressDialog pd = ProgressDialog.show(InfoDetail.this, "", "Mohon tunggu sebentar..", false);


        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setBuiltInZoomControls(true);


        mWebview.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplication().getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pd.show();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();

                String webUrl = mWebview.getUrl();

            }
        });

        String jquery = "<script\n" +
                "  src=\"https://code.jquery.com/jquery-2.2.4.min.js\"\n" +
                "  integrity=\"sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=\"\n" +
                "  crossorigin=\"anonymous\"></script>";
        String bootStrap = "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">" +
                "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>";

        String html = ""+
                "<div class=\"row\">"+
                "<div class=\"col-sm-12\">"+
                "<h3>"+extrasTitle+"</h3>"
                +
                extrasTag
                + "</div>"
                + "</div>";

        String fullPage = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "\t<meta name=viewport content=\"width=device-width, initial-scale=1\">\n" +
                "\t<title>AnggitPrayogo.com | About\n" +
                "</title>\n" +
                "\t<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" +
                "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.2.1/assets/owl.carousel.min.css\">\n" +
                "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.2.1/assets/owl.theme.default.min.css\">\n" +
                "<link rel=\"stylesheet\" href=\"http://anggitprayogo.com/css/style.css\">\n" +
                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                "<script src=\"https://code.jquery.com/jquery-2.2.4.min.js\"\n" +
                "  integrity=\"sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=\"></script>\n" +
                "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.2.1/owl.carousel.min.js\"></script>\n" +
                "<script src=\"//cdnjs.cloudflare.com/ajax/libs/ScrollMagic/2.0.5/ScrollMagic.min.js\"></script>\n" +
                "<script src=\"//cdnjs.cloudflare.com/ajax/libs/ScrollMagic/2.0.5/plugins/debug.addIndicators.min.js\"></script>\t\n" +
                "</head>\n" +
                "<body>\n"+
                html
                +
                "</body>\n" +
                "</html>";

        mWebview.loadData(fullPage, "text/html; charset=utf-8", "UTF-8");
        //mWebview.loadUrl(extrasSlug);

        getSupportActionBar().setTitle(extrasTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
