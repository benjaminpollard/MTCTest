package test.dd.com.androidmcs.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.dd.com.androidmcs.Fragments.Interfaces.IFootballFragmentHost;
import test.dd.com.androidmcs.Fragments.Interfaces.IOnMoreClickedHost;
import test.dd.com.androidmcs.Helpers.FlagHelper;
import test.dd.com.androidmcs.Models.PlayerModel;
import test.dd.com.androidmcs.Models.TeamModel;
import test.dd.com.androidmcs.R;


class FootballRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  List<PlayerModel> playerModelList;
    private  List<TeamModel> teamModelList;

    private IFootballFragmentHost mListener;
    private IOnMoreClickedHost moreClickHost;

    private static final String playerAgePrefix = "Age: ";
    private static final String playerClubPrefix = "Club: ";

    private static final int Player = 0;
    private static final int Team = 1;

    private static final String teamCityPrefix = "City";
    private static final String teamStadiumPrefix = "Stadium";

    private boolean showFooter;
    private FlagHelper flagHelper;

    FootballRecyclerViewAdapter(List<PlayerModel> players, List<TeamModel> teams, IFootballFragmentHost listener, IOnMoreClickedHost moreClickHost , boolean isFavFragment) {
        playerModelList = players;
        teamModelList = teams;
        mListener = listener;
        this.moreClickHost = moreClickHost;
        this.showFooter = isFavFragment;
        this.flagHelper = FlagHelper.getInstance();
    }
    void SetPlayerList(List<PlayerModel> playerModelList)
    {
        this.playerModelList = playerModelList;
        notifyDataSetChanged();
    }
    void SetTeamList(List<TeamModel> teamModelList )
    {
        this.teamModelList = teamModelList;
        notifyDataSetChanged();
    }

    List<PlayerModel> GetPlayerList( )
    {
        return this.playerModelList;
    }
    List<TeamModel> GetTeamList( )
    {
        return this.teamModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Player:
                View player = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_player, parent, false);
                return new PlayerViewHolder(player);
            case Team:
                View team = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_team, parent, false);
                return new TeamViewHolder(team);
            default :
                View def = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_team, parent, false);
                return new TeamViewHolder(def);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

            case Player:
                PlayerModel playerModel = playerModelList.get(position);
                PlayerViewHolder playerViewHolder = (PlayerViewHolder)holder;
                playerViewHolder.Id = playerModel.playerID;
                playerViewHolder.playerAge.setText(playerAgePrefix + playerModel.playerAge);
                playerViewHolder.playerClub.setText(playerClubPrefix + playerModel.playerClub);
                playerViewHolder.playerName.setText(playerModel.playerFirstName + " " + playerModel.playerSecondName);
                Context context = playerViewHolder.mView.getContext();
                playerViewHolder.playerFlag.setImageResource(context.getResources().getIdentifier("drawable/" + flagHelper.getFlagFilenameForNationality(playerModel.playerNationality,context )
                        , null, context.getPackageName()));
                AddHeaderOrFoot(true,playerViewHolder.mView,position);
                playerViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            // Notify the active callbacks interface (the activity, if the
                            // fragment is attached to one) that an item has been selected.
                            PlayerViewHolder tempHolder = (PlayerViewHolder)holder;
                            mListener.onListFragmentInteraction(tempHolder.Id, true);
                        }
                    }
                });
                break;

            case Team:
                TeamViewHolder teamViewHolder = (TeamViewHolder)holder;
                TeamModel teamModel = teamModelList.get(position - playerModelList.size() );
                teamViewHolder.Id = teamModel.teamID;
                teamViewHolder.teamCity.setText(teamCityPrefix + teamModel.teamCity);
                teamViewHolder.teamStadium.setText(teamStadiumPrefix + teamModel.teamStadium);
                teamViewHolder.teamName.setText(teamModel.teamName);
                Context contextTeam = teamViewHolder.mView.getContext();
                teamViewHolder.teamFlag.setImageResource(contextTeam.getResources().getIdentifier("drawable/" + flagHelper.getFlagFilenameForNationality(teamModel.teamNationality,contextTeam )
                        , null, contextTeam.getPackageName()));
                AddHeaderOrFoot(false,teamViewHolder.mView,position);
                teamViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            // Notify the active callbacks interface (the activity, if the
                            // fragment is attached to one) that an item has been selected.
                            TeamViewHolder tempHolder = (TeamViewHolder)holder;
                            mListener.onListFragmentInteraction(tempHolder.Id , false);
                        }
                    }
                });
                break;
        }
    }

    private void AddHeaderOrFoot(boolean isPlayer, View mView, int pos)
    {

        ViewGroup header = (ViewGroup) mView.findViewById(R.id.header);
        header.removeAllViews();
        ViewGroup footer = (ViewGroup) mView.findViewById(R.id.footer);
        footer.removeAllViews();

        if(isPlayer)
        {
            if(pos == 0)
            {
                SetUpHeader(mView, true);
                return;
            }
            if(pos == playerModelList.size() - 1 && !showFooter)
            {
                SetUpFooter(mView , true);
            }

        }else
        {
            if(pos ==  playerModelList.size())
            {
                SetUpHeader(mView , false);
                return;

            }

            if(pos + 1 ==  playerModelList.size()  + teamModelList.size()  && !showFooter)
            {
                SetUpFooter(mView, false);
            }

        }

    }
    private void SetUpHeader(View mView, boolean isPlayer )
    {
        LayoutInflater vi = (LayoutInflater) mView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView = vi.inflate(R.layout.item_header, null);
        TextView textView = (TextView) headerView.findViewById(R.id.header_textView);
        if(isPlayer)
        {
            textView.setText(R.string.header_player_text);

        }
        else
        {
            textView.setText(R.string.header_team_text);
        }

        ViewGroup header = (ViewGroup) mView.findViewById(R.id.header);
        header.addView(headerView, 0 , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void SetUpFooter(View mView , final boolean isPlayer)
    {
        LayoutInflater vi = (LayoutInflater) mView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View footerView = vi.inflate(R.layout.item_footer, null);
        TextView textView = (TextView) footerView.findViewById(R.id.footer_textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreClickHost.MoreClicked(isPlayer);
            }
        });
        ViewGroup footer = (ViewGroup) mView.findViewById(R.id.footer);
        footer.addView(footerView, 0 , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public int getItemViewType(int position) {
        if(position > playerModelList.size() - 1)
        {
            return Team;
        }
        else
        {
            return Player;
        }
    }
    @Override
    public int getItemCount()
    {
        return playerModelList.size() + teamModelList.size();
    }

     class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final View mView;

        @BindView(R.id.item_player_name)
        TextView playerName;
        @BindView(R.id.item_age)
        TextView playerAge;
        @BindView(R.id.item_club)
        TextView playerClub;
         @BindView(R.id.item_flag)
         ImageView playerFlag;
         private double Id;

         private PlayerViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {
        private final View mView;

        @BindView(R.id.item_team_name)
        TextView teamName;
        @BindView(R.id.item_team_city)
        TextView teamCity;
        @BindView(R.id.item_team_stadium)
        TextView teamStadium;
        @BindView(R.id.item_team_flag)
        ImageView teamFlag;

        private double Id;

        private TeamViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
