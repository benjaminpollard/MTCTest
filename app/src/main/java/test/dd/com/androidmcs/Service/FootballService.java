package test.dd.com.androidmcs.Service;

import java.util.HashMap;
import test.dd.com.androidmcs.Service.Interfaces.IBaseServiceCallback;
import test.dd.com.androidmcs.Service.Interfaces.IFootballService;
import test.dd.com.androidmcs.Service.Interfaces.ServiceCallbacks;

public class FootballService extends BaseService implements IFootballService {
    private static final String searchRequestEndPoint = "http://trials.mtcmobile.co.uk/api/football/1.0/search";

    public void GetSearchRequest(String objectAsJson, final ServiceCallbacks host)
    {
        super.Post(searchRequestEndPoint, new HashMap<String, String>(), objectAsJson, new IBaseServiceCallback() {
            @Override
            public void callBack(Boolean success, String responce) {
                if(success)
                {
                    host.OnSuccess(responce);
                }else
                {
                    host.OnFail(responce);
                }
            }
        });
    }

}
