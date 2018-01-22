package vn.loitp.app.activity.home.favmanga;

/**
 * Created by www.muathu@gmail.com on 1/2/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import loitp.basemaster.R;
import vn.loitp.app.activity.view.ComicItem;
import vn.loitp.app.common.Constants;
import vn.loitp.app.data.ComicData;
import vn.loitp.app.model.comic.Comic;
import vn.loitp.app.util.AppUtil;
import vn.loitp.core.utilities.LImageUtil;
import vn.loitp.views.recyclerview.parallaxrecyclerviewyayandroid.ParallaxViewHolder;

public class TestRecyclerAdapter extends RecyclerView.Adapter<TestRecyclerAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;

    public TestRecyclerAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public interface Callback {
        public void onClick(Comic comic, int position);

        public void onLongClick(Comic comic, int position);
    }

    private ComicItem.Callback callback;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(inflater.inflate(R.layout.item_comic, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.rootView.setBackgroundColor(AppUtil.getColor(context));
        LImageUtil.load((Activity) context, ComicData.getInstance().getComicFavList().get(position).getUrlImg(), viewHolder.getBackgroundImage());
        viewHolder.tvTitle.setText(ComicData.getInstance().getComicFavList().get(position).getTitle());
        viewHolder.tvDate.setText(ComicData.getInstance().getComicFavList().get(position).getDate());
        viewHolder.ivIsFav.setVisibility(ComicData.getInstance().getComicFavList().get(position).isFav() == Constants.IS_FAV ? android.view.View.VISIBLE : android.view.View.GONE);

        // # CAUTION:
        // Important to call this method
        viewHolder.getBackgroundImage().reuse();
    }

    @Override
    public int getItemCount() {
        if (ComicData.getInstance().getComicFavList() == null) {
            return 0;
        }
        return ComicData.getInstance().getComicFavList().size();
    }

    /**
     * # CAUTION:
     * ViewHolder must extend from ParallaxViewHolder
     */
    public static class ViewHolder extends ParallaxViewHolder {
        public RelativeLayout rootView;
        public ImageView ivIsFav;
        public TextView tvTitle;
        public TextView tvDate;

        public ViewHolder(View v) {
            super(v);
            rootView = (RelativeLayout) v.findViewById(R.id.root_view);
            ivIsFav = (ImageView) v.findViewById(R.id.iv_is_fav);
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
        }

        @Override
        public int getParallaxImageId() {
            return R.id.backgroundImage;
        }

    }


}