package net.devsgroup.ultralog.repository;

import android.content.Context;

import java.io.File;

public interface UltralogRepository {
    void log(String log, int level);

    void err(Throwable e);

    void clear();

    File getLog(Context context);
}
