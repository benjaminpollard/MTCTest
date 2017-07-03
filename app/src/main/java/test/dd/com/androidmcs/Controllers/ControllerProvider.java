package test.dd.com.androidmcs.Controllers;

import test.dd.com.androidmcs.Helpers.MoshiWrapper;
import test.dd.com.androidmcs.Repository.DatabaseRepository;
import test.dd.com.androidmcs.Repository.FootballRepository;
import test.dd.com.androidmcs.Repository.Interfaces.IDatabaseRepository;
import test.dd.com.androidmcs.Repository.Interfaces.IFootballRepository;
import test.dd.com.androidmcs.Repository.Interfaces.IJsonConverter;
import test.dd.com.androidmcs.Service.FootballService;
import test.dd.com.androidmcs.Service.Interfaces.IFootballService;

//Use Provided to control how controllers are made the the depednces they use
//could be replaced with DI/IoC lib
public class ControllerProvider {

    private IDatabaseRepository CreateDatabase()
    {
        return new DatabaseRepository();
    }
    private IFootballRepository CreateFootballRepo()
    {
        return new FootballRepository(CreateFootballService(),CreateJsonConvter());
    }
    private IFootballService CreateFootballService()
    {
        return new FootballService();
    }
    private IJsonConverter CreateJsonConvter()
    {
        return new MoshiWrapper();
    }

    public FavouriteController GetFavouriteController()
    {
        return new FavouriteController(CreateDatabase());
    }
    public FootballController GetFootballController()
    {
        return new FootballController(CreateDatabase(), CreateFootballRepo());
    }
}
