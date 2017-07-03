package test.dd.com.androidmcs.Repository;

import org.json.JSONException;

import java.io.IOException;

import test.dd.com.androidmcs.Models.MorePlayersFromApiModel;
import test.dd.com.androidmcs.Models.MoreTeamsFromApiModel;
import test.dd.com.androidmcs.Models.SearchRequestBodyModel;
import test.dd.com.androidmcs.Models.SearchResultFromApiModel;
import test.dd.com.androidmcs.Repository.Interfaces.FootballMorePlayersCallback;
import test.dd.com.androidmcs.Repository.Interfaces.FootballMoreTeamsCallback;
import test.dd.com.androidmcs.Repository.Interfaces.FootballRepositoryCallback;
import test.dd.com.androidmcs.Repository.Interfaces.IFootballRepository;
import test.dd.com.androidmcs.Repository.Interfaces.IJsonConverter;
import test.dd.com.androidmcs.Service.Interfaces.IFootballService;
import test.dd.com.androidmcs.Service.Interfaces.ServiceCallbacks;

public class FootballRepository implements IFootballRepository {

    private IFootballService service;
    private IJsonConverter jsonConverter;

    public FootballRepository(IFootballService service, IJsonConverter jsonConverter)
    {
        this.service = service;
        this.jsonConverter = jsonConverter;
    }

    private enum SearchType
    {
        BOTH , TEAM, PLAYER
    }
    public void SearchPlayersAndTeams(int currentNumberOfItem , String search , final FootballRepositoryCallback callback)
    {
        service.GetSearchRequest(setUpItem(SearchType.BOTH, currentNumberOfItem, search), new ServiceCallbacks() {
            @Override
            public void OnFail(String reason) {
                callback.OnFail();
            }

            @Override
            public void OnSuccess(String jsonResult) {
                try {

                    callback.OnSuccess(jsonConverter.FromJson(jsonConverter.inconsistentTypeWorkAround(jsonResult),SearchResultFromApiModel.class));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    callback.OnFail();
                }
            }
        } );
    }


    public void SearchPlayer(int currentNumberOfItem , String search , final FootballMorePlayersCallback callback)
    {
        service.GetSearchRequest( setUpItem(SearchType.PLAYER,currentNumberOfItem,search) , new ServiceCallbacks() {
            @Override
            public void OnFail(String reason) {
                callback.OnFail();
            }

            @Override
            public void OnSuccess(String jsonResult) {
                try {
                    callback.OnSuccess(jsonConverter.FromJson(jsonConverter.inconsistentTypeWorkAround(jsonResult),MorePlayersFromApiModel.class));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    callback.OnFail();
                }
            }
        });
    }

    public void SearchTeams(int currentNumberOfItem , String search , final FootballMoreTeamsCallback callback)
    {
        service.GetSearchRequest( setUpItem(SearchType.TEAM,currentNumberOfItem,search) , new ServiceCallbacks() {
            @Override
            public void OnFail(String reason) {
                callback.OnFail();
            }

            @Override
            public void OnSuccess(String jsonResult) {
                try {
                    callback.OnSuccess(jsonConverter.FromJson(jsonConverter.inconsistentTypeWorkAround(jsonResult),MoreTeamsFromApiModel.class));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    {
                        callback.OnFail();
                }
            }
        }});
    }

    private String setUpItem(SearchType searchType , int offSet , String search )
    {
        SearchRequestBodyModel item = new SearchRequestBodyModel();
        item.offset = offSet;
        item.searchString = search;
        switch (searchType)
        {
            case BOTH:
                item.searchType = "";
                break;
            case PLAYER:
                item.searchType = "players";
                break;
            case TEAM:
                item.searchType = "teams";
                break;
        }
        return jsonConverter.ToJson(item , item.getClass());
    }


}
