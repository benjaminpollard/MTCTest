package test.dd.com.androidmcs.Repository.Interfaces;


public interface IFootballRepository {
    void SearchPlayersAndTeams(int currentNumberOfItem , String search , final FootballRepositoryCallback callback);
    void SearchPlayer(int currentNumberOfItem , String search , final FootballMorePlayersCallback callback);
    void SearchTeams(int currentNumberOfItem , String search , final FootballMoreTeamsCallback callback);
}
