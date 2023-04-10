package net.devsgroup.ultralog.repository.serverRepository;

import android.annotation.SuppressLint;
import android.content.Context;

import net.devsgroup.ultralog.preferences.PreferenceRepository;
import net.devsgroup.ultralog.server.SendLogApi;
import net.devsgroup.ultralog.util.Ultralog;

import org.joda.time.DateTime;

import java.io.File;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ServerRepositoryImpl implements ServerRepository {
    private SendLogApi logApi;

    public ServerRepositoryImpl() {
    }

    @SuppressLint("CheckResult")
    @Override
    public void sendFile(Context context, File fileData, String id, Consumer<Response<ResponseBody>> onSuccess, Consumer<Throwable> onError) {
        if (logApi == null) {
            try {
                logApi = createApi(context);
            } catch (Exception e) {
                Ultralog.e("Log sender is not initialized");
                Ultralog.e(e);
                return;
            }
        }
        if (fileData == null) {
            Ultralog.e("File is null");
            return;
        }
        RequestBody requestFile = RequestBody.create(
                MediaType.parse("text/plain"),
                fileData
        );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData(
                        "androidLog", "logs" + "T" +
                                DateTime.now().toString("yyyyMMdd-HHmmss") + "ID" + id + ".txt",
                        requestFile);
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, id);
        logApi.sendLog(body, description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        onSuccess,
                        onError
                );
    }

    private SendLogApi createApi(Context context) throws IllegalArgumentException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(PreferenceRepository.getLogUrl(context))
                .client(client)
                .build()
                .create(SendLogApi.class);

    }
}
