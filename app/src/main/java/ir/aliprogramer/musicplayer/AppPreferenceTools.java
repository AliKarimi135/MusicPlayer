package ir.aliprogramer.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferenceTools {
    private Context mContext;
    private SharedPreferences mPreferences;

    public AppPreferenceTools(Context context) {
        this.mContext = context;
        this.mPreferences = this.mContext.getSharedPreferences("app_preference",Context.MODE_PRIVATE);
    }

    public void setFirstSetup( Boolean status) {
        mPreferences.edit().putBoolean("firstSetup",status).apply();
    }

    public Boolean getFirstSetup() {
        return mPreferences.getBoolean("firstSetup",false);
    }
}
