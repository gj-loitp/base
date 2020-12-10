package vn.loitp.app.activity.customviews.wwlmusic.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annotation.LogTag;
import com.core.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import vn.loitp.app.R;
import vn.loitp.app.activity.customviews.wwlmusic.interfaces.FragmentHost;
import vn.loitp.app.activity.customviews.wwlmusic.utils.WWLMusicDataset;

//TODO convert kotlin
@LogTag("WWLPlaylistFragment")
public class WWLPlaylistFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CustomAdapter mAdapter;
    private TextView mTitleView;
    private TextView mSubTitleView;
    private FragmentHost mFragmentHost;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.wwl_music_playlist_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.mTitleView = getFrmRootView().findViewById(R.id.liTitle);
        this.mSubTitleView = getFrmRootView().findViewById(R.id.liSubtitle);
        this.mRecyclerView = getFrmRootView().findViewById(R.id.recyclerView);
        this.mLayoutManager = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(mLayoutManager);
        this.mRecyclerView.scrollToPosition(0);

        this.mAdapter = new CustomAdapter(WWLMusicDataset.datasetItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    //TODO deprecated
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.mFragmentHost = (FragmentHost) activity;
    }

    private void onItemClicked(WWLMusicDataset.DatasetItem item) {
        if (this.mFragmentHost != null) {
            this.mFragmentHost.goToDetail(item);
        }
    }

    public void updateItem(WWLMusicDataset.DatasetItem item) {
        this.mTitleView.setText(item.title);
        this.mSubTitleView.setText(item.subtitle);
    }

    private class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final WWLMusicDataset.DatasetItem[] mDataSet;

        public CustomAdapter(WWLMusicDataset.DatasetItem[] datasetItems) {
            this.mDataSet = datasetItems;
        }

        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wwl_music_playlist_item, parent, false);
            return new CustomAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).getTitleView().setText(this.mDataSet[position].title);
            ((ViewHolder) holder).getSubTitleView().setText(this.mDataSet[position].subtitle);
        }

        @Override
        public int getItemCount() {
            return this.mDataSet.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView titleView;
            private final TextView subtitleView;

            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(v1 -> WWLPlaylistFragment.this.onItemClicked(CustomAdapter.this.mDataSet[getAdapterPosition()]));
                titleView = v.findViewById(R.id.liTitle);
                subtitleView = v.findViewById(R.id.liSubtitle);
            }

            public TextView getTitleView() {
                return titleView;
            }

            public TextView getSubTitleView() {
                return subtitleView;
            }
        }
    }
}
