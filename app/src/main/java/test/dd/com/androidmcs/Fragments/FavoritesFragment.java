package test.dd.com.androidmcs.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dd.com.androidmcs.Controllers.ControllerProvider;
import test.dd.com.androidmcs.Controllers.FavouriteController;
import test.dd.com.androidmcs.Fragments.Interfaces.IFootballFragmentHost;
import test.dd.com.androidmcs.Fragments.Interfaces.IOnMoreClickedHost;
import test.dd.com.androidmcs.Fragments.Interfaces.ISearchReciver;
import test.dd.com.androidmcs.R;

public class FavoritesFragment extends Fragment implements ISearchReciver, IFootballFragmentHost ,  IOnMoreClickedHost {

    private FavouriteController controller;
    private FootballRecyclerViewAdapter adapter;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    public FavoritesFragment() {
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        controller = new ControllerProvider().GetFavouriteController();

        View view = inflater.inflate(R.layout.fragment_footbal_list, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new FootballRecyclerViewAdapter(controller.GetPlayers(), controller.GetTeams(), this ,this,true);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void OnReciveSearchResults(String search) {
        //maybe search thought fav list
    }

    @Override
    public void onListFragmentInteraction(double item, boolean isPlayer) {
        controller.RemoveFromFavourites(item , isPlayer);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void MoreClicked(boolean IsPlayerClicked) {
    //currently unneed but could be used to switch back to other fragment and search
    }
}
