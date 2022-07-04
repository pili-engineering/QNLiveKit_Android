package sensetime.senseme.com.effects.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sensetime.senseme.com.effects.R;
import sensetime.senseme.com.effects.view.StickerItem;

public class NewStickerAdapter extends RecyclerView.Adapter {

    ArrayList<StickerItem> mStickerList;
    private View.OnClickListener mOnClickStickerListener;
    private int mSelectedPosition = 0;
    Context mContext;
    public int[] selectedPosition;
    public Map<Integer,Integer> map = new HashMap<>();

    public NewStickerAdapter(ArrayList<StickerItem> list, Context context) {
        mStickerList = list;
        mContext = context;
        selectedPosition = new int[list.size()];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item, null);
        view.setOnClickListener(mOnClickStickerListener);
        return new FilterViewHolder(view);
    }
    /**
     * loading 状态绑定
     *
     * @param stickerItem
     * @param holder
     * @param position
     */
    private void bindState(StickerItem stickerItem, FilterViewHolder holder, int position) {
        if (stickerItem != null) {
            switch (stickerItem.state) {
                case NORMAL_STATE:
                    //设置为等待下载状态
                    if (holder.normalState.getVisibility() != View.VISIBLE) {
                        holder.normalState.setVisibility(View.VISIBLE);
                        holder.downloadingState.setVisibility((View.INVISIBLE));
                        holder.downloadingState.setActivated(false);
                        holder.loadingStateParent.setVisibility((View.INVISIBLE));
                    }
                    break;
                case LOADING_STATE:
                    //设置为loading 状态
                    if (holder.downloadingState.getVisibility() != View.VISIBLE) {
                        holder.normalState.setVisibility(View.INVISIBLE);
                        holder.downloadingState.setActivated(true);
                        holder.downloadingState.setVisibility((View.VISIBLE));
                        holder.loadingStateParent.setVisibility((View.VISIBLE));
                    }

                    break;
                case DONE_STATE:
                    //设置为下载完成状态
                    if (holder.normalState.getVisibility() != View.INVISIBLE || holder.downloadingState.getVisibility() != View.INVISIBLE) {
                        holder.normalState.setVisibility(View.INVISIBLE);
                        holder.downloadingState.setVisibility((View.INVISIBLE));
                        holder.downloadingState.setActivated(false);
                        holder.loadingStateParent.setVisibility((View.INVISIBLE));
                    }

                    break;
            }
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FilterViewHolder viewHolder = (FilterViewHolder) holder;
        viewHolder.imageView.setImageBitmap(mStickerList.get(position).icon);
        holder.itemView.setSelected(checkSelected(position,selectedPosition));
        holder.itemView.setTag(position);
        bindState(getItem(position), viewHolder, position);
        holder.itemView.setOnClickListener(mOnClickStickerListener);
    }

    public void setClickStickerListener(View.OnClickListener listener) {
        mOnClickStickerListener = listener;
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

    static class FilterViewHolder extends ViewHolder {

        View view;
        ImageView imageView;
        ImageView normalState;
        ImageView downloadingState;
        ViewGroup loadingStateParent;

        public FilterViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.icon);
            normalState = (ImageView) itemView.findViewById(R.id.normalState);
            downloadingState = (ImageView) itemView.findViewById(R.id.downloadingState);
            loadingStateParent = (ViewGroup) itemView.findViewById(R.id.loadingStateParent);
        }
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    public void resizeSelectedPositionList(int length){
        int[] tmpList = new int[length];
        for (int i = 0; i < length && i < selectedPosition.length; i++) {
            tmpList[i] = selectedPosition[i];
        }
        selectedPosition = tmpList;
    }

    public boolean checkAllUnselected(int[] selectedPosition){
        for(int i = 0; i < selectedPosition.length; i++){
            if(selectedPosition[i] == 1){
                return false;
            }
        }
        return true;
    }

    public boolean checkSelected(int position, int[] selectedPosition) {
        if (selectedPosition == null || selectedPosition.length <= 0) {
            return false;
        }
        return selectedPosition[position] == 1;
    }

    public void allUnselected(){
        for(int i = 0; i < selectedPosition.length; i++){
            selectedPosition[i] = 0;
        }
    }
}
