package net.devsgroup.ultralog.preferences;

import android.content.Context;
import android.net.Uri;

public class PreferenceRepository {
    private static final String sharedPreferenceName = "log.sp";
    private static final String prefLogUri = "PREF_LOG_URI";
    private static final String prefBaseUrl = "PREF_LOG_BASE_URL";

    public static String getFileUri(Context context) {
        return context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
                .getString(prefLogUri, "");
    }

    public static void putFileUri(Context context, Uri uri) {
        context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE).edit()
                .putString(prefLogUri, uri.toString()).apply();
    }

    public static String getLogUrl(Context context) {
        return context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
                .getString(prefBaseUrl, "http://");
    }

    public static void setLogUrl(Context context, String baseUrl) {
        context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE).edit()
                .putString(prefBaseUrl, baseUrl).apply();
    }
}
