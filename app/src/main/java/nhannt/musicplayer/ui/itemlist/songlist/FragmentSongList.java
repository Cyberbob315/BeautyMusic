package nhannt.musicplayer.ui.itemlist.songlist;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.App;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.SongAdapter;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.base.BaseFragment;
import nhannt.musicplayer.ui.itemlist.ItemListMvpView;
import nhannt.musicplayer.ui.itemlist.ItemListPresenter;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.DividerDecoration;
import nhannt.musicplayer.utils.Navigator;
import nhannt.musicplayer.utils.Setting;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSongList#newInstance} factory method to
 */
public class FragmentSongList extends BaseFragment implements ItemListMvpView<Song>, RecyclerItemClickListener {

    public static final String TITLE = "Songs";
    private ArrayList<Song> mData;
    @BindView(R.id.rv_song_list_main)
    protected RecyclerView rvSongList;
    @BindView(R.id.loading_indicator)
    protected AVLoadingIndicatorView mLoadingIndicator;

    private SongListPresenter songPresenter;
    private SongAdapter songAdapter;


    public FragmentSongList() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        songPresenter = new SongListPresenter();
        songPresenter.attachedView(this);
        songPresenter.onResume();
        enableDoBack();

    }

    public static FragmentSongList newInstance() {
        FragmentSongList fragment = new FragmentSongList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    final BroadcastReceiver playstateChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MusicService.PLAY_STATE_CHANGE)) {
                songAdapter.updatePlayPosition(App.getInstance().getCurrentPlayingSong().getId());
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicService.PLAY_STATE_CHANGE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(playstateChange, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(playstateChange);
    }

    @Override
    public void doBack() {
        songPresenter.cancelFetchingData();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bt_view_as_list:
                songPresenter.viewAs(ItemListPresenter.VIEW_AS_LIST);
                break;
            case R.id.bt_view_as_grid:
                songPresenter.viewAs(ItemListPresenter.VIEW_AS_GRID);
                break;
            case R.id.bt_sort_a_z:
                songPresenter.sortAs(ItemListPresenter.SORT_AS_A_Z);
                break;
            case R.id.bt_sort_z_a:
                songPresenter.sortAs(ItemListPresenter.SORT_AS_Z_A);
                break;
            case R.id.bt_sort_year:
                songPresenter.sortAs(ItemListPresenter.SORT_AS_YEAR);
                break;
            case R.id.bt_sort_artist:
                songPresenter.sortAs(ItemListPresenter.SORT_AS_ARTIST);
                break;
            case R.id.bt_sort_album:
                songPresenter.sortAs(ItemListPresenter.SORT_AS_ALBUM);
                break;
            case R.id.bt_sort_duration:
                songPresenter.sortAs(ItemListPresenter.SORT_AS_DURATION);
                break;
            case R.id.btn_shuffle_all_menu:
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        App.getInstance().shuffeAll();
                    }
                });
                break;
            case R.id.bt_equalizer:
                Navigator.navigateToEqualizer(getContext(), 125);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem itemSortBy = menu.findItem(R.id.bt_sort_by);
        Menu menuSortBy = itemSortBy.getSubMenu();
        menuSortBy.removeItem(R.id.bt_sort_album_no);
        menuSortBy.removeItem(R.id.bt_sort_song_no);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        Log.d("Option", "here");
    }


    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSongList.setLayoutManager(layoutManager);
        rvSongList.addItemDecoration(new DividerDecoration(getActivity()));
        rvSongList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_song_list;
    }

    @Override
    public void onItemClickListener(View view, int position) {
        songPresenter.onItemSelected(view, position);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void setItems(ArrayList<Song> itemList) {
        songAdapter = new SongAdapter(getActivity(), itemList, R.layout.item_song);
        songAdapter.setRecyclerItemClickListener(this);
        rvSongList.setAdapter(songAdapter);
        mData = itemList;
        songPresenter.sortAs(Setting.getInstance().get(Common.SONG_SORT_MODE, ItemListPresenter.SORT_AS_A_Z));
        Song currentSong = App.getInstance().getCurrentPlayingSong();
        if (currentSong != null)
            songAdapter.updatePlayPosition(currentSong.getId());

    }


    @Override
    public void showProgress() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        rvSongList.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        mLoadingIndicator.setVisibility(View.GONE);
        rvSongList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void notifyDataSetChanged() {
        songAdapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<Song> getListItem() {
        return mData;
    }

    @Override
    public void onDestroy() {
        songPresenter.detachView();
        super.onDestroy();
    }


    @Override
    public void navigateToDetailFragment(Object object, @Nullable View transitionView) {

    }

}
