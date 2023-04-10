package net.devsgroup.ultralog.server;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SendLogApi {
    @Multipart
    @POST("androidLog")
    Single<Response<ResponseBody>> sendLog(
            @Part MultipartBody.Part file,
            @Part("govNum") RequestBody id
    );
}
