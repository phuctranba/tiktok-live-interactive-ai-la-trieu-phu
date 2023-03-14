package haui.android;

import android.app.Application;
import android.content.Context;

import haui.android.manager.MusicManager;

public class App extends Application {
    private static Context context;
    private static MusicManager musicPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        musicPlayer = new MusicManager(this);
    }

    public static MusicManager getMusicPlayer(){
        return musicPlayer;
    }

    public static Context getContext() {
        return context;
    }
}