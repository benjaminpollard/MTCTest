package test.dd.com.androidmcs.Controllers;

import io.realm.RealmList;
import test.dd.com.androidmcs.Helpers.DatabaseUpdate;
import test.dd.com.androidmcs.Models.FavoritesListModel;
import test.dd.com.androidmcs.Models.PlayerModel;
import test.dd.com.androidmcs.Models.TeamModel;
import test.dd.com.androidmcs.Repository.Interfaces.IDatabaseRepository;

public class FavouriteController {

    private IDatabaseRepository databaseRepository;

    FavouriteController(IDatabaseRepository databaseRepository)
    {
        this.databaseRepository = databaseRepository;
    }

    public RealmList<TeamModel> GetTeams()
    {
        FavoritesListModel result = databaseRepository.GetItem(FavoritesListModel.class);
        if(result == null || result.favTeams == null)
        {
            return new RealmList<>();
        }
        return result.favTeams;
    }
    public RealmList<PlayerModel> GetPlayers()
    {
        FavoritesListModel result = databaseRepository.GetItem(FavoritesListModel.class);
        if(result == null || result.favPlayers == null)
        {
            return new RealmList<>();
        }
        return result.favPlayers;
    }

    public void RemoveFromFavourites(double id, boolean isPlayer) {
        final FavoritesListModel item = databaseRepository.GetItem(FavoritesListModel.class);
        if (isPlayer) {
            for (final PlayerModel player : GetPlayers()) {
                if (player.playerID == id) {
                    databaseRepository.Update(new DatabaseUpdate() {
                        @Override
                        public void Update() {
                            item.favPlayers.remove(player);
                        }
                    });
                    return;
                }
            }
        } else {
            for (final TeamModel team : GetTeams()) {
                if (team.teamID == id) {
                    databaseRepository.Update(new DatabaseUpdate() {
                        @Override
                        public void Update() {
                            item.favTeams.remove(team);
                        }
                    });
                    return;

                }
            }
        }
    }

}
