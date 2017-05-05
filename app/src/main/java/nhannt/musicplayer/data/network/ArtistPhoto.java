package nhannt.musicplayer.data.network;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import nhannt.musicplayer.R;

/**
 * Created by NhanNT on 05/05/2017.
 */

public class ArtistPhoto {

    private static final String LAST_FM_API_KEY = "761226e2f2b94da7de6a61d73f50e33c";
    private static final String URL_1 = "http://ws.audioscrobbler.com/2.0/?method=artist.search&artist=";
    private static final String URL_2 = "&api_key=" + LAST_FM_API_KEY + "&format=json";

    private ImageView imgArtist;
    private String artistName;
    private Context context;
    private boolean isAnimate = false;

    public ArtistPhoto(Context context, String artistName, ImageView imgArtist, boolean isAnimate) {
        this.imgArtist = imgArtist;
        this.artistName = artistName;
        this.context = context;
        this.isAnimate = isAnimate;
    }

    public void execute() {
        String requestLink = URL_1 + artistName + URL_2;
        Log.d(artistName + " request link", requestLink);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                requestLink, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject result = response.optJSONObject("results");
                        JSONObject artistMatches = result.optJSONObject("artistmatches");
                        JSONArray listArtist = artistMatches.optJSONArray("artist");
                        if (listArtist != null) {
                            JSONObject artist = listArtist.optJSONObject(0);
                            if (artist != null) {
                                JSONArray photoList = artist.optJSONArray("image");
                                JSONObject photo = photoList.optJSONObject(2);
                                String url = photo.optString("#text");
                                if (isAnimate)
                                    Glide.with(context)
                                            .load(url)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .placeholder(R.drawable.google_play_music_logo)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .crossFade()
                                            .fitCenter()
                                            .into(imgArtist);
                                else
                                    Glide.with(context)
                                            .load(url)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .placeholder(R.drawable.google_play_music_logo)
                                            .crossFade()
                                            .fitCenter()
                                            .dontAnimate()
                                            .into(imgArtist);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleyConnection.getInstance(context).addRequestToQueue(jsonObjectRequest);
    }
}