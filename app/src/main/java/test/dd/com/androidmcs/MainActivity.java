package test.dd.com.androidmcs;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import butterknife.BindView;
import butterknife.ButterKnife;
import test.dd.com.androidmcs.Fragments.FavoritesFragment;
import test.dd.com.androidmcs.Fragments.FootballFragment;
import test.dd.com.androidmcs.Fragments.Interfaces.ISearchReciver;

public class MainActivity extends AppCompatActivity  {

    @BindView(R.id.content)
    FrameLayout fragmentHolder;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.search_bar)
    SearchView searchBar;
    @BindView(R.id.search_button)
    Button searchButton;
    @BindView(R.id.search_bar_holder)
    LinearLayout searchHolder;

    private FragmentManager supportFragmentManager;
    private FootballFragment footBallFragment;
    private FavoritesFragment favoritesFragment;

    private long delay = 400;
    private long last_text_edit = 0;

    private Handler handler = new Handler();

    private Runnable typingFinishedChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //use butter knife to lower the amount of boilerplate code needed
        ButterKnife.bind(this);

        footBallFragment = FootballFragment.newInstance();
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().replace(R.id.content,footBallFragment).commit();
        SetUpSearch();

        SetUpBottomBarOnClickActions();
    }

    private void SetUpSearch() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
                if(fragment instanceof ISearchReciver)
                {
                    ((ISearchReciver) fragment).OnReciveSearchResults(searchBar.getQuery().toString());
                }
            }
        });
        searchBar.setQueryHint(getString(R.string.search_hint));

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
                if(fragment instanceof ISearchReciver)
                {
                    ((ISearchReciver) fragment).OnReciveSearchResults(query);
                }
                return false;
            }



            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    searchButton.setVisibility(View.GONE);
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(typingFinishedChecker, delay);
                }
                return false;
            }
        });


        typingFinishedChecker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() >= (last_text_edit + delay )) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
                    if(fragment instanceof ISearchReciver)
                    {
                        ((ISearchReciver) fragment).OnReciveSearchResults(searchBar.getQuery().toString());
                    }
                    searchButton.setVisibility(View.VISIBLE);

                }
            }
        };

    }

    private void SetUpBottomBarOnClickActions() {

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        supportFragmentManager.beginTransaction().replace(R.id.content, footBallFragment).commit();
                        searchHolder.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.navigation_favourite:

                        if (favoritesFragment == null) {
                            favoritesFragment = FavoritesFragment.newInstance();
                        }
                        searchHolder.setVisibility(View.GONE);

                        supportFragmentManager.beginTransaction().replace(R.id.content, favoritesFragment).commit();
                        return true;

                }
                return false;
            }

        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

}
