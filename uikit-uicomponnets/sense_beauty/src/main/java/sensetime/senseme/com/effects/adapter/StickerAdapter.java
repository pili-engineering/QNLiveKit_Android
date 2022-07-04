package sensetime.senseme.com.effects.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sensetime.senseme.com.effects.R;
import sensetime.senseme.com.effects.utils.GlideUtils;
import sensetime.senseme.com.effects.utils.SpUtils;
import sensetime.senseme.com.effects.view.StickerItem;
import sensetime.senseme.com.effects.view.widget.EffectType;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.NormalViewHolder> {

    ArrayList<StickerItem> mStickerList = new ArrayList<>();
    public int mSelectedPosition = -1;
    Context mContext;
    private final boolean supportInvertSelect;

    public ArrayList<StickerItem> getData() {
        return mStickerList;
    }

    public interface Listener {
        void onItemClickSticker(int position, StickerItem data, Boolean selected, StickerAdapter adapter);
    }

    private final LayoutInflater mLayoutInflater;
    private final EffectType mEffectType;

    public StickerAdapter(Context context, boolean supportInvertSelect, EffectType type) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.supportInvertSelect = supportInvertSelect;
        this.mEffectType = type;
    }

    @NonNull
    @Override
    public NormalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NormalViewHolder(mLayoutInflater.inflate(R.layout.sticker_item, parent, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull NormalViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final StickerItem entity = mStickerList.get(position);
        switch (entity.state) {
            case NORMAL_STATE:
                holder.mPbLoading.setVisibility(View.GONE);
                holder.normalState.setVisibility(View.VISIBLE);
                break;
            case LOADING_STATE:
                holder.normalState.setVisibility(View.INVISIBLE);
                holder.mPbLoading.setVisibility(View.VISIBLE);
                break;
            case DONE_STATE:
                holder.normalState.setVisibility(View.INVISIBLE);
                holder.mPbLoading.setVisibility(View.GONE);
                break;
        }

        holder.itemView.setSelected(mSelectedPosition == position);

        if (null!= mEffectType && mEffectType == EffectType.TYPE_STICKER_SYNC) {
            String iconUrl = entity.iconUrl;
            if (TextUtils.isEmpty(iconUrl)) {
                iconUrl = (String) SpUtils.getParam(entity.path, "");
            }
            GlideUtils.INSTANCE.load2(iconUrl, holder.imageView);
        } else {
            GlideUtils.INSTANCE.load1(entity.iconUrl, holder.imageView);
        }

        holder.itemView.setOnClickListener(view -> {
            if (mListener != null) {
                if (mSelectedPosition == position) {
                    if (supportInvertSelect) {
                        mSelectedPosition = -1;
                        mListener.onItemClickSticker(position, entity, false, StickerAdapter.this);
                    }
                } else {
                    mSelectedPosition = position;
                    mListener.onItemClickSticker(position, entity, true, StickerAdapter.this);
                }
                notifyDataSetChanged();
            }
        });
    }

    public StickerItem getItem(int position) {
        if (position >= 0 && position < getItemCount()) {
            return mStickerList.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mStickerList.size();
    }

    public void recycle() {
        for (StickerItem item :
                mStickerList) {
            item.recycle();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addHeadData(StickerItem item) {
        if (!hasItem(item)) {
            mStickerList.add(0, item);
            if(mSelectedPosition!=-1) {
                mSelectedPosition = mSelectedPosition + 1;
            }
            SpUtils.setParam(item.path + "time", System.currentTimeMillis());
            SpUtils.setParam(item.path, item.iconUrl);
            notifyDataSetChanged();
        }
    }

    private boolean hasItem(StickerItem item) {
        for (StickerItem i : mStickerList) {
            if (i.path.contains(item.name))
                return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(@NotNull List<?> data) {
        mStickerList.clear();
        mStickerList.addAll((Collection<? extends StickerItem>) data);
        notifyDataSetChanged();
    }

    public StickerAdapter getAdapter() {
        return this;
    }

    private Listener mListener;

    public void setListener(@NotNull Listener listener) {
        mListener = listener;
    }

    static class NormalViewHolder extends ViewHolder {

        ImageView imageView;
        ImageView normalState;
        ProgressBar mPbLoading;

        public NormalViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icon);
            mPbLoading = itemView.findViewById(R.id.pb_loading);
            normalState = itemView.findViewById(R.id.normalState);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        notifyDataSetChanged();
    }
}
