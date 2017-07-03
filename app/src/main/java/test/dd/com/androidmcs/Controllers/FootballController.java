package test.dd.com.androidmcs.Controllers;

import java.util.List;
import io.realm.Realm;
import io.realm.RealmList;
import test.dd.com.androidmcs.Helpers.DatabaseUpdate;
import test.dd.com.androidmcs.Models.FavoritesListModel;
import test.dd.com.androidmcs.Models.LastSearchResultsListModel;
import test.dd.com.androidmcs.Models.PlayerModel;
import test.dd.com.androidmcs.Models.TeamModel;
import test.dd.com.androidmcs.Repository.Interfaces.FootballMorePlayersCallback;
import test.dd.com.androidmcs.Repository.Interfaces.FootballMoreTeamsCallback;
import test.dd.com.androidmcs.Repository.Interfaces.IDatabaseRepository;
import test.dd.com.androidmcs.Repository.Interfaces.FootballRepositoryCallback;
import test.dd.com.androidmcs.Repository.Interfaces.IFootballRepository;

public class FootballController  {

    private IDatabaseRepository databaseRepository;
    private IFootballRepository footballRepository;

    FootballController(IDatabaseRepository databaseRepository, IFootballRepository footballRepository)
    {
        this.databaseRepository = databaseRepository;
        this.footballRepository = footballRepository;
    }

    public LastSearchResultsListModel GetLastResults()
    {
        LastSearchResultsListModel item = databaseRepository.GetItem(LastSearchResultsListModel.class);
        if(item == null || item.players == null)
        {
            item = new LastSearchResultsListModel();
            item.players = new RealmList<>();
            item.teams = new RealmList<>();
        }
        return item;
    }
    public void SaveLastResults(final List<PlayerModel> playerModelList , final List<TeamModel> teamModelsList)
    {

        if(playerModelList.size() == 0 && teamModelsList.size() == 0 )
        {
            return;
        }

        boolean managed = false;
        for (PlayerModel item : playerModelList) {
            if(item.isManaged())
            {
                managed = true;
            }
        }

        if(managed)
        {
            final List<PlayerModel> playerModels = Realm.getDefaultInstance().copyFromRealm(playerModelList);
            final List<TeamModel> teamModelss = Realm.getDefaultInstance().copyFromRealm(teamModelsList);

            databaseRepository.Update(new DatabaseUpdate() {
                @Override
                public void Update() {
                    LastSearchResultsListModel list = databaseRepository.GetItem(LastSearchResultsListModel.class);
                    if(list == null )
                    {
                        list = new LastSearchResultsListModel();
                    }

                    list.players = new RealmList<>();
                    list.teams = new RealmList<>();
                    list.teams.addAll(teamModelss);
                    list.players.addAll(playerModels);
                }
            });
        }else
        {

            databaseRepository.Update(new DatabaseUpdate() {
                @Override
                public void Update() {
                    LastSearchResultsListModel list = databaseRepository.GetItem(LastSearchResultsListModel.class);
                    if(list == null )
                    {
                        list = new LastSearchResultsListModel();
                    }

                    list.players = new RealmList<>();
                    list.teams = new RealmList<>();
                    list.teams.addAll(teamModelsList);
                    list.players.addAll(playerModelList);
                }
            });
        }

    }
    public void GetSearchQuery( String search, FootballRepositoryCallback callback)
    {
        footballRepository.SearchPlayersAndTeams(0,search,callback);
    }

    public void GetMorePlayers(String search, FootballMorePlayersCallback callback, int offset)
    {
        footballRepository.SearchPlayer(offset,search,callback);
    }
    public void GetMoreTeams(String search, FootballMoreTeamsCallback callback, int offset)
    {
        footballRepository.SearchTeams(offset,search,callback);
    }

    //Save item as Fav, check to make sure its not already added
    public boolean SetItemAsFavourite(double id, boolean isPlayer , List<PlayerModel> playerList , List<TeamModel> teamModelList)
    {
        FavoritesListModel item = databaseRepository.GetItem(FavoritesListModel.class);

        if(item == null)
        {
            item = new FavoritesListModel();
        }
        if(isPlayer)
        {
            for (final PlayerModel player: playerList) {
                if(player.playerID == id)
                {
                    if( item.favPlayers == null)
                    {
                        item.favPlayers = new RealmList<>();
                    }
                    boolean alreadyAdded = false;
                    for (PlayerModel locPlayer: item.favPlayers) {
                        if(locPlayer.playerID == id)
                        {
                            alreadyAdded = true;
                        }
                    }
                    if(!alreadyAdded)
                    {
                        final FavoritesListModel finalItem1 = item;
                        databaseRepository.Update(new DatabaseUpdate() {
                            @Override
                            public void Update() {
                                finalItem1.favPlayers.add(player);
                            }
                        });
                        databaseRepository.SaveItem(item);
                        return true;
                    }
                    return false;
                }
            }
        }
        else
        {
            for (final TeamModel team: teamModelList) {
                if(team.teamID == id)
                {
                    if( item.favTeams == null)
                    {
                        item.favTeams = new RealmList<>();
                    }
                    if(!item.favTeams.contains(team))
                    {
                        final FavoritesListModel finalItem = item;
                        databaseRepository.Update(new DatabaseUpdate() {
                            @Override
                            public void Update() {
                                finalItem.favTeams.add(team);
                            }
                        });
                        return true;
                    }
                    return false;
                }
            }
        }

    return false;
    }



}
