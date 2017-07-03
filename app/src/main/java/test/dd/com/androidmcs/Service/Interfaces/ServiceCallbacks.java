package test.dd.com.androidmcs.Service.Interfaces;

public abstract class ServiceCallbacks {
     public abstract void OnFail(String reason);
     public abstract void OnSuccess(String jsonResult);
}
