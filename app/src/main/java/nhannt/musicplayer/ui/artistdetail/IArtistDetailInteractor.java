package nhannt.musicplayer.ui.artistdetail;

import nhannt.musicplayer.interfaces.LoaderListener;

/**
 * Created by NhanNT on 05/03/2017.
 */

public interface IArtistDetailInteractor {
    void loadListAlbumOfArtist(int artistId);
    void loadListSongOfArtist(int artistId);
}