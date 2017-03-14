package nhannt.musicplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.model.Artist;

/**
 * Created by nhannt on 03/03/2017.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>{

    public static int LAYOUT_ITEM_LIST = 0;
    public static int LAYOUT_ITEM_GRID = 1;

    private Context mContext;
    private ArrayList<Artist> mData;
    private LayoutInflater mLayoutInflater;
    private int layoutType;

    public ArtistAdapter(Context mContext, ArrayList<Artist> mData, int layoutType) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.layoutType = layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public int getLayoutType(){
        return layoutType;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ArtistViewHolder holder;
        View itemView;
        if(layoutType == LAYOUT_ITEM_LIST){
            itemView = mLayoutInflater.inflate(R.layout.item_artist_list, parent, false);
        }else{
            itemView = mLayoutInflater.inflate(R.layout.item_artist_list, parent, false);
        }
        holder = new ArtistViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        Artist item = mData.get(position);
        holder.tvArtistName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_cover_item_artist)
        protected ImageView artistArt;
        @BindView(R.id.tv_artist_title_item)
        protected TextView tvArtistName;
        @BindView(R.id.tv_album_song_artist_item)
        protected TextView tvArtistInfo;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
