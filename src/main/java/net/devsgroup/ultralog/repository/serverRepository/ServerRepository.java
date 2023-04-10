package net.devsgroup.ultralog.repository.serverRepository;

import android.content.Context;

import java.io.File;
import java.util.function.Function;

import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Response;

public interface ServerRepository {
    void sendFile(Context context, File fileData, String id, Consumer<Response<ResponseBody>> onSuccess, Consumer<Throwable> onError);
}
