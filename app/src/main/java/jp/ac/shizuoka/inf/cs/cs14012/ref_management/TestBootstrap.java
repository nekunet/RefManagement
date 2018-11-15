package jp.ac.shizuoka.inf.cs.cs14012.ref_management;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class TestBootstrap extends Application {
    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}