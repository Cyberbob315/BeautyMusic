package nhannt.musicplayer.ui.playback;

import java.util.ArrayList;

import nhannt.musicplayer.model.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.ui.base.BaseView;

/**
 * Created by nhannt on 04/04/2017.
 */

public interface IPlayBackView extends BaseView {
    void updateSeekBar(int currentTime, int totalTime);

    void updateButtonState();

    MusicService getMusicService();

    void updateSongInfo();

    void updateTimeView(long currentTime);

    void setItems(ArrayList<Song> lstItem);

    void notifyDataSetChanged();
}
