package nhannt.musicplayer.ui.playlist;

import android.os.AsyncTask;

import java.util.ArrayList;

import nhannt.musicplayer.R;
import nhannt.musicplayer.data.database.DBQuery;
import nhannt.musicplayer.data.provider.MediaProvider;
import nhannt.musicplayer.interfaces.LoaderListener;
import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.utils.AppController;

/**
 * Created by NhanNT on 04/21/2017.
 */

public class PlaylistListInteractor implements IPlaylistListInteractor {
    private LoaderListener listener;

    @Override
    public void loadPlaylistList(LoaderListener listener) {
        this.listener = listener;
        new LoadListPlaylist().execute();
    }

    private class LoadListPlaylist extends AsyncTask<Void, Void, Void> {
        ArrayList<PlayList> lstPlaylist;
        ArrayList<Song> lstSongAdded;
        ArrayList<Song> lstSongRecent;

        @Override
        protected Void doInBackground(Void... params) {
            lstPlaylist = DBQuery.getInstance(AppController.getContext()).getAllPlayList();
            lstSongAdded = MediaProvider.getInstance().getListSong();
            lstSongRecent = DBQuery.getInstance(AppController.getContext()).getListSongRecentPlay();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            PlayList playlistAdded = new PlayList(FragmentPlaylist.RECENT_ADDED_ID, AppController.getContext().getString(R.string.last_added), lstSongAdded.size(),
                    lstSongAdded, true);
            lstPlaylist.add(0, playlistAdded);
            if (lstSongRecent.size() > 0) {
                PlayList playlistRecent = new PlayList(FragmentPlaylist.RECENT_PLAYED_ID, AppController.getContext().getString(R.string.recent_played), lstSongAdded.size(),
                        lstSongRecent, true);
                lstPlaylist.add(1, playlistRecent);
            }
            listener.onFinished(lstPlaylist);
            super.onPostExecute(aVoid);
        }
    }
}