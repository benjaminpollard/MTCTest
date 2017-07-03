package test.dd.com.androidmcs.Repository.Interfaces;

import test.dd.com.androidmcs.Models.MoreTeamsFromApiModel;

public abstract class FootballMoreTeamsCallback {

    public abstract void OnFail();
    public abstract void OnSuccess(MoreTeamsFromApiModel result);

}
