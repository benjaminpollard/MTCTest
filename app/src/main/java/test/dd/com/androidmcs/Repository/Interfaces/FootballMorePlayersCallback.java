package test.dd.com.androidmcs.Repository.Interfaces;

import test.dd.com.androidmcs.Models.MorePlayersFromApiModel;

public abstract class FootballMorePlayersCallback {

    public abstract void OnFail();
    public abstract void OnSuccess(MorePlayersFromApiModel result);

}
