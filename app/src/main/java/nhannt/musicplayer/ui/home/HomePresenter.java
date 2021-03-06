package nhannt.musicplayer.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import nhannt.musicplayer.App;
import nhannt.musicplayer.R;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.utils.Navigator;

/**
 * Created by nhannt on 17/03/2017.
 */

public class HomePresenter implements IHomePresenter, View.OnClickListener {

    private MusicService mService;
    private IHomeView mView;
    private static Handler handler;
    private final IHomeInteractor mInteractor;

    public HomePresenter() {
        handler = new Handler();
        mInteractor = new HomeInteractor(this);
    }

    @Override
    public void attachedView(IHomeView view) {
        this.mView = view;
        mService = view.getMusicService();

    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_toggle_play_current_bar:
                mService = mView.getMusicService();
                if (mService != null && mService.isSongSetted()) {
                    intent = new Intent(mView.getViewContext(), MusicService.class);
                    intent.setAction(MusicService.ACTION_TOGGLE_PLAY_PAUSE);
                    App.getContext().startService(intent);
                }
                break;
            case R.id.current_play_bar:
                Navigator.navigateToPlayBackActivity(mView.getViewContext());
                break;
        }
    }

    final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MusicService.PLAY_STATE_CHANGE)) {
                updateTimePlay();
                mView.updateSongInfo();
                mView.updatePlayPauseState();
            }
        }
    };

    public void updateTimePlay() {
        handler.removeCallbacks(mUpdateTimeTask);
        handler.post(mUpdateTimeTask);
        mUpdateTimeTask.run();
    }

    private final Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (mView == null) return;
            mService = mView.getMusicService();
            if (mService != null) {
                if (mService.getState() == MusicService.MusicState.Playing) {
                    mView.updateSeekBar(mService.getCurrentPosition(), mService.getDuration());
                    handler.postDelayed(mUpdateTimeTask, 200);
                }
            }

        }
    };

    @Override
    public void searchComplete(ArrayList lstResult) {
        mView.updateSearch(lstResult);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void cancelFetchingData() {

    }

    @Override
    public BroadcastReceiver getReceiver() {
        return receiver;
    }

    @Override
    public void searchArtistDetail(String query, int id) {
        mInteractor.searchArtist(query, id);
    }

    @Override
    public void searchAlbumDetail(String query, int id) {
        mInteractor.searchAlbum(query, id);
    }

    @Override
    public void searchAll(String query) {
        mInteractor.searchAll(query);
    }

    @Override
    public void searchPlayingQueue(String query, ArrayList<Song> lstPlayingSong) {
        mInteractor.searchPlayingQueue(query,lstPlayingSong);
    }
}
