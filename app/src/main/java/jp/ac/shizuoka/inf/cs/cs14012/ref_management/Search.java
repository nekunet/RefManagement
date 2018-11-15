package jp.ac.shizuoka.inf.cs.cs14012.ref_management;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Search extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Intent intent = getIntent();
        String searchKeyword = intent.getStringExtra("search_data");
        String url;
        if (searchKeyword != null) {
            url = "http://cookpad.com/search/" + searchKeyword;
        }else{
            url = "http://cookpad.com/";
        }

        WebView myWebView = (WebView)findViewById(R.id.webView1);

        //リンクをタップしたときに標準ブラウザを起動させない
        myWebView.setWebViewClient(new WebViewClient());

        //最初にcookpadのページを表示する。
        myWebView.loadUrl(url);

        //javascriptを許可する
        myWebView.getSettings().setJavaScriptEnabled(true);


    }

    //戻るキーでブラウザの一つ前の状態に戻る
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView myWebView2 = (WebView)findViewById(R.id.webView1); //ブラウザバック用にここで呼び出す
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView2.canGoBack()) {
            myWebView2.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
