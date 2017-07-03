package test.dd.com.androidmcs.Service.Interfaces;

import java.util.HashMap;

public interface IBaseService {
    void Post(String Url, HashMap<String,String> params , String jsonBody , IBaseServiceCallback host);
    void Get(String Url, HashMap<String,String> params , IBaseServiceCallback host );
}
