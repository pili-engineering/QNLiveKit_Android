package sensetime.senseme.com.effects.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import sensetime.senseme.com.effects.R;
import sensetime.senseme.com.effects.view.MediaItem;
import sensetime.senseme.com.effects.view.RoundImageView;

public class MediaAdapter extends RecyclerView.Adapter {

    List<MediaItem> mMediaList;
    private View.OnClickListener mOnClickFilterListener;
    private int mSelectedPosition = 0;
    Context mContext;

    public MediaAdapter(List<MediaItem> list, Context context) {
        mMediaList = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item, null);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FilterViewHolder viewHolder = (FilterViewHolder) holder;
        viewHolder.imageView.setImageBitmap(mMediaList.get(position).icon);
        viewHolder.textView.setText(mMediaList.get(position).name);
        viewHolder.textView.setTextColor(Color.parseColor("#000000"));
        viewHolder.alphaView.getBackground().setAlpha(0);

        viewHolder.imageViewBg.setBackground(mContext.getResources().getDrawable(R.drawable.bg_filter_view_unselected));
        holder.itemView.setSelected(mSelectedPosition == position);

        if(mSelectedPosition == position){
            viewHolder.alphaView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_filter_alpha_selected));
            viewHolder.textView.setText(mMediaList.get(position).name);
            viewHolder.textView.setTextColor(Color.parseColor("#ffffff"));

            viewHolder.imageViewBg.setBackground(mContext.getResources().getDrawable(R.drawable.bg_filter_view_selected));
        }

        if(mOnClickFilterListener != null) {
            holder.itemView.setTag(position);

            holder.itemView.setOnClickListener(mOnClickFilterListener);
            holder.itemView.setSelected(mSelectedPosition == position);
        }
    }

    public void setClickFilterListener(View.OnClickListener listener) {
        mOnClickFilterListener = listener;
    }

    @Override
    public int getItemCount() {
        return mMediaList.size();
    }

    static class FilterViewHolder extends ViewHolder {

        View view;
        RoundImageView imageView;
        TextView textView;
        ImageView alphaView;
        LinearLayout imageViewBg;

        public FilterViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (RoundImageView) itemView.findViewById(R.id.iv_filter_image);
            textView = (TextView) itemView.findViewById(R.id.filter_text);
            alphaView = (ImageView) itemView.findViewById(R.id.iv_alpha_view);
            imageViewBg = (LinearLayout) itemView.findViewById(R.id.ll_filter_image);
        }
    }

    public void setSelectedPosition(int position){
        mSelectedPosition = position;
    }
}
