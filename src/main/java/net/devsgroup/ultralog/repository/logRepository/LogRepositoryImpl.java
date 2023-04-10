package net.devsgroup.ultralog.repository.logRepository;

import static net.devsgroup.ultralog.constants.Constants.TAG;
import static net.devsgroup.ultralog.constants.Constants.d;
import static net.devsgroup.ultralog.constants.Constants.e;
import static net.devsgroup.ultralog.constants.Constants.i;
import static net.devsgroup.ultralog.constants.Constants.v;
import static net.devsgroup.ultralog.constants.Constants.w;
import static net.devsgroup.ultralog.constants.Constants.wtf;

import android.content.Context;
import android.util.Log;

import net.devsgroup.ultralog.repository.UltralogRepository;

import java.io.File;

public class LogRepositoryImpl implements UltralogRepository {
    @Override
    public void log(String log, int level) {
        switch (level) {
            case v:
                Log.v(TAG, log);
                break;
            case i:
                Log.i(TAG, log);
                break;
            case w:
                Log.w(TAG, log);
                break;
            case e:
                Log.e(TAG, log);
                break;
            case wtf:
                Log.wtf(TAG, log);
                break;
            case d:
            default:
                Log.d(TAG, log);
        }
    }

    @Override
    public void err(Throwable e) {
        Log.e(TAG, e.getLocalizedMessage(), e);
    }

    @Override
    public void clear() {

    }

    @Override
    public File getLog(Context context) {
        return null;
    }
}
