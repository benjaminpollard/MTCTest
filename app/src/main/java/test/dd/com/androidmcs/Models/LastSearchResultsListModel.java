package test.dd.com.androidmcs.Models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class LastSearchResultsListModel extends RealmObject {
    public RealmList<PlayerModel> players;
    public RealmList<TeamModel> teams ;
    private String id = "";

}
