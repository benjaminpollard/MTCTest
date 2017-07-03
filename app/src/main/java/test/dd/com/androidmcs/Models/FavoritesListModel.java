package test.dd.com.androidmcs.Models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class FavoritesListModel extends RealmObject {
    public RealmList<PlayerModel> favPlayers = new RealmList<>();
    public RealmList<TeamModel> favTeams = new RealmList<>();

}
