package com.watar.player;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.view.*;
import android.content.*;
import android.net.Uri;

public class MainActivity extends Activity {
    WebView web;
    ValueCallback<Uri[]> fileCallback;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(0xFF07070D);
        web = new WebView(this);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setBuiltInZoomControls(false);
        web.setWebChromeClient(new WebChromeClient(){
            @Override public boolean onShowFileChooser(WebView v, ValueCallback<Uri[]> cb, FileChooserParams p){
                if(fileCallback!=null) fileCallback.onReceiveValue(null);
                fileCallback=cb;
                Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE); i.setType("audio/*"); i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(i,42); return true;
            }
        });
        web.setWebViewClient(new WebViewClient());
        web.setOverScrollMode(View.OVER_SCROLL_NEVER);
        web.loadUrl("file:///android_asset/index.html");
        setContentView(web);
    }
    @Override protected void onActivityResult(int r,int c,Intent d){
        super.onActivityResult(r,c,d); if(r==42 && fileCallback!=null){
            Uri[] u=null; if(c==RESULT_OK && d!=null){
                if(d.getClipData()!=null){ int n=d.getClipData().getItemCount(); u=new Uri[n]; for(int x=0;x<n;x++)u[x]=d.getClipData().getItemAt(x).getUri(); }
                else if(d.getData()!=null) u=new Uri[]{d.getData()};
            } fileCallback.onReceiveValue(u); fileCallback=null;
        }
    }
    @Override public void onBackPressed(){ if(web.canGoBack()) web.goBack(); else super.onBackPressed(); }
}
