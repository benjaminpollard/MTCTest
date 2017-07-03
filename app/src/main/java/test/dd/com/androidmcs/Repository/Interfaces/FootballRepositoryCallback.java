package test.dd.com.androidmcs.Repository.Interfaces;

import test.dd.com.androidmcs.Models.SearchResultFromApiModel;

public abstract class FootballRepositoryCallback {

    public abstract void OnFail();
    public abstract void OnSuccess(SearchResultFromApiModel result);

}
