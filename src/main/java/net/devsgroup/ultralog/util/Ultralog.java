package net.devsgroup.ultralog.util;

import static net.devsgroup.ultralog.constants.Constants.d;
import static net.devsgroup.ultralog.constants.Constants.e;
import static net.devsgroup.ultralog.constants.Constants.i;
import static net.devsgroup.ultralog.constants.Constants.v;
import static net.devsgroup.ultralog.constants.Constants.w;
import static net.devsgroup.ultralog.constants.Constants.wtf;

import android.content.Context;
import android.os.Build;

import net.devsgroup.ultralog.constants.Constants;
import net.devsgroup.ultralog.model.OnComplete;
import net.devsgroup.ultralog.preferences.PreferenceRepository;
import net.devsgroup.ultralog.repository.UltralogRepository;
import net.devsgroup.ultralog.repository.fileRepository.FileRepositoryImpl;
import net.devsgroup.ultralog.repository.logRepository.LogRepositoryImpl;
import net.devsgroup.ultralog.repository.serverRepository.ServerRepository;
import net.devsgroup.ultralog.repository.serverRepository.ServerRepositoryImpl;

import java.io.File;
import java.util.function.Consumer;

import retrofit2.HttpException;

public class Ultralog {
    private static UltralogRepository fileRepository;
    private static UltralogRepository logRepository;
    private static ServerRepository serverRepository;

    public static class Builder {
        private String baseUrl;

        public void build(Context context) {
            Ultralog.setupBaseUrl(context, baseUrl);
            if (fileRepository != null || logRepository != null) {
                return;
            }
            Ultralog.withContext(context);
        }

        public Builder fileName(String fileName) {
            Constants.setFileName(fileName);
            return this;
        }

        public Builder tag(String tag) {
            Constants.TAG = tag;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }
    }

    private static void withContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            fileRepository = FileRepositoryImpl.create(context);
        } else {
            fileRepository = FileRepositoryImpl.create();
        }
        logRepository = new LogRepositoryImpl();
        serverRepository = new ServerRepositoryImpl();
    }

    private static void setupBaseUrl(Context context, String baseUrl) {
        PreferenceRepository.setLogUrl(context, baseUrl);
    }

    public static void v(String message) {
        logRepository.log(message, v);
        fileRepository.log(message, v);
    }

    public static void d(String message) {
        logRepository.log(message, d);
        fileRepository.log(message, d);
    }

    public static void i(String message) {
        logRepository.log(message, i);
        fileRepository.log(message, i);
    }

    public static void w(String message) {
        logRepository.log(message, w);
        fileRepository.log(message, w);
    }

    public static void e(String message) {
        logRepository.log(message, e);
        fileRepository.log(message, e);
    }

    public static void e(Throwable throwable) {
        logRepository.err(throwable);
        fileRepository.err(throwable);
    }

    public static void wtf(String message) {
        logRepository.log(message, wtf);
        fileRepository.log(message, wtf);
    }

    public static void wtf(Throwable throwable) {
        logRepository.err(throwable);
        fileRepository.err(throwable);
    }

    public static void clear() {
        fileRepository.clear();
        logRepository.clear();
        fileRepository = null;
        logRepository = null;
    }

    public static void sendLog(Context context, String id, OnComplete onComplete, Consumer<Throwable> doOnError) {
        File log = fileRepository.getLog(context);
        serverRepository.sendFile(context, log, id, onSuccess -> {
            if (!onSuccess.isSuccessful()) {
                Ultralog.e("Log didn't sent");
                throw new HttpException(onSuccess);
            }
            onComplete.complete();
        }, doOnError::accept);
    }
}
