package test.dd.com.androidmcs.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dd.com.androidmcs.Controllers.ControllerProvider;
import test.dd.com.androidmcs.Controllers.FootballController;
import test.dd.com.androidmcs.Fragments.Interfaces.IFootballFragmentHost;
import test.dd.com.androidmcs.Fragments.Interfaces.IOnMoreClickedHost;
import test.dd.com.androidmcs.Fragments.Interfaces.ISearchReciver;
import test.dd.com.androidmcs.Models.LastSearchResultsListModel;
import test.dd.com.androidmcs.Models.MorePlayersFromApiModel;
import test.dd.com.androidmcs.Models.MoreTeamsFromApiModel;
import test.dd.com.androidmcs.Models.PlayerModel;
import test.dd.com.androidmcs.Models.SearchResultFromApiModel;
import test.dd.com.androidmcs.Models.TeamModel;
import test.dd.com.androidmcs.R;
import test.dd.com.androidmcs.Repository.Interfaces.FootballMorePlayersCallback;
import test.dd.com.androidmcs.Repository.Interfaces.FootballMoreTeamsCallback;
import test.dd.com.androidmcs.Repository.Interfaces.FootballRepositoryCallback;

public class FootballFragment extends Fragment implements ISearchReciver, IFootballFragmentHost, IOnMoreClickedHost {

    private FootballController controller;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.list)
    RecyclerView recyclerView;

    FootballRecyclerViewAdapter adaptor;

    private String lastSearch = "";

    public FootballFragment() {
    }


    public static FootballFragment newInstance() {
        return new FootballFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_footbal_list, container, false);
        ButterKnife.bind(this, view);
        controller = new ControllerProvider().GetFootballController();

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        LastSearchResultsListModel lastResults = controller.GetLastResults();
        SetUpAdaptor(lastResults.players, lastResults.teams);

        return view;
    }

    private  void SetUpAdaptor(List<PlayerModel> players , List<TeamModel> teams)
    {

        adaptor = new FootballRecyclerViewAdapter(players, teams, this ,this, false);
        recyclerView.setAdapter(adaptor);
    }

    @Override
    public void OnReciveSearchResults(String search) {

        lastSearch = search;
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        controller.GetSearchQuery(search, new FootballRepositoryCallback() {
            @Override
            public void OnFail() {
                Toast.makeText(getContext(), R.string.request_failed_string, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            @Override
            public void OnSuccess(final SearchResultFromApiModel queryResult) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                SetUpAdaptor(queryResult.result.players, queryResult.result.teams);
            }
        });

    }

    @Override
    public void onListFragmentInteraction(double id, boolean isPlayer) {
       boolean added = controller.SetItemAsFavourite(id , isPlayer , adaptor.GetPlayerList() , adaptor.GetTeamList());
        String message = getString(R.string.fail_to_add);
        if(added)
        {
            message = getString(R.string.add_to_fav);
        }
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void MoreClicked(boolean IsPlayerClicked) {

        if(lastSearch.equals(""))
        {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        if(IsPlayerClicked)
        {
            this.controller.GetMorePlayers(lastSearch, new FootballMorePlayersCallback() {
                @Override
                public void OnFail() {
                    Toast.makeText(getContext(), R.string.request_failed_string, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void OnSuccess(MorePlayersFromApiModel result) {
                    List<PlayerModel> list = adaptor.GetPlayerList();
                    list.addAll(result.result.players);
                    adaptor.SetPlayerList(list);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }, adaptor.GetPlayerList().size());
        }
        else
        {
            this.controller.GetMoreTeams(lastSearch, new FootballMoreTeamsCallback() {
                @Override
                public void OnFail() {
                    Toast.makeText(getContext(), R.string.request_failed_string, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void OnSuccess(MoreTeamsFromApiModel result) {
                    List<TeamModel> list = adaptor.GetTeamList();
                    list.addAll(result.result.teams);
                    adaptor.SetTeamList(list);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }, adaptor.GetPlayerList().size() - 1);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        controller.SaveLastResults(adaptor.GetPlayerList(), adaptor.GetTeamList());
    }
}
