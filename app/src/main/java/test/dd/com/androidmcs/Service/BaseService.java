package test.dd.com.androidmcs.Service;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import test.dd.com.androidmcs.Service.Interfaces.IBaseService;
import test.dd.com.androidmcs.Service.Interfaces.IBaseServiceCallback;

class BaseService implements IBaseService {

    private OkHttpClient client;
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    BaseService()
    {
        client = new OkHttpClient();
    }
    public void Post(String Url, HashMap<String,String> params ,String jsonBody ,IBaseServiceCallback callback )
    {
        RequestBody body = RequestBody.create(JSON, jsonBody);

        Request request = new Request.Builder()
                .header("Authorization", "token abcd")
                .url(SetUpBaseRequest(Url,params))
                .post(body)
                .build();

        SetUpCallbacks(request,callback );
    }
    public void Get(String Url, HashMap<String,String> params ,IBaseServiceCallback host )
    {
        Request request = new Request.Builder()
                .header("Authorization", "token abcd")
                .url(SetUpBaseRequest(Url,params))
                .build();

        SetUpCallbacks(request,host);
    }
    private String SetUpBaseRequest(String Url, HashMap<String,String> params)
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Url).newBuilder();

        for (Object o : params.entrySet()) {
            Map.Entry mEntry = (Map.Entry) o;
            urlBuilder.addQueryParameter(mEntry.getKey().toString(), mEntry.getValue().toString());
        }

        return urlBuilder.build().toString();

    }
    private void SetUpCallbacks(Request request  , final IBaseServiceCallback host)
    {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, final IOException e) {

                e.printStackTrace();

                Handler uiHandler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        host.callBack(false,e.getMessage());
                    }
                };

                uiHandler.post(runnable);
            }

            @Override
            public void onResponse(@NonNull Call call, final Response response) throws IOException {

                final String json = response.body().string();
                final Boolean failed = !response.isSuccessful();
                final String message = response.message();


                Handler uiHandler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                if (failed || json == null) {
                    host.callBack(false , message);
                }
                host.callBack(true,json );

                    }
                };

                uiHandler.post(runnable);

            }
        });
    }

}
