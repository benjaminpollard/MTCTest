package test.dd.com.androidmcs;

import android.app.Application;

import io.realm.Realm;

public class AppApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Realm.init(this);
    }
}
