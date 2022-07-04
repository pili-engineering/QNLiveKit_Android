package sensetime.senseme.com.effects.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sensetime.senseme.com.effects.R;
import sensetime.senseme.com.effects.view.BeautyItem;
import sensetime.senseme.com.effects.view.widget.EffectType;

public class BeautyItemAdapter extends RecyclerView.Adapter {

    ArrayList<BeautyItem> mBeautyItem;
    private View.OnClickListener mOnClickBeautyItemListener;
    private int mSelectedPosition = 0;
    Context mContext;

    public BeautyItemAdapter(Context context, ArrayList<BeautyItem> list) {
        mContext = context;
        mBeautyItem = list;
    }

    public ArrayList<BeautyItem> getData() {
        return mBeautyItem;
    }

    public BeautyItem getItemTyType(EffectType type) {
        for (BeautyItem item : mBeautyItem) {
            if (item.type == type)
                return item;
        }
        return null;
    }

    public BeautyItem getItem(int position) {
        return mBeautyItem.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beauty_item, null);
        return new BeautyItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final BeautyItemViewHolder viewHolder = (BeautyItemViewHolder) holder;
        final BeautyItem entity = mBeautyItem.get(position);
        if (entity.type != null && entity.type == EffectType.TYPE_HIGH_BACK || entity.type == EffectType.TYPE_MX_HIGH_THIN_FACE) {
            viewHolder.mSubscription.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mSubscription.setVisibility(View.VISIBLE);
        }
        viewHolder.mName.setText(mBeautyItem.get(position).getText());

        int strengthI = Math.round(mBeautyItem.get(position).getProgress() * 100);
        viewHolder.mSubscription.setText(strengthI + "");

        viewHolder.mName.setTextColor(Color.parseColor("#A7A7A7"));
        viewHolder.mSubscription.setTextColor(Color.parseColor("#A7A7A7"));

        if (null!=mBeautyItem.get(position).getUnselectedtIcon())
            viewHolder.mImage.setImageBitmap(mBeautyItem.get(position).getUnselectedtIcon());
        else
            viewHolder.mImage.setImageResource(mBeautyItem.get(position).getUnselectedtIconRes());

        holder.itemView.setSelected(mSelectedPosition == position);
        if (mSelectedPosition == position) {
            viewHolder.mSubscription.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.mName.setTextColor(Color.parseColor("#ffffff"));

            if (null!=mBeautyItem.get(position).getSelectedIcon())
                viewHolder.mImage.setImageBitmap(mBeautyItem.get(position).getSelectedIcon());
            else
                viewHolder.mImage.setImageResource(mBeautyItem.get(position).getSelectedtIconRes());
        }

        holder.itemView.setOnClickListener(view -> {
            mSelectedPosition = position;
            mListener.onItemClick(position, entity);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mBeautyItem.size();
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

//    public void setClickBeautyListener(View.OnClickListener listener) {
//        mOnClickBeautyItemListener = listener;
//    }

    static class BeautyItemViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView mImage;
        TextView mName;
        TextView mSubscription;

        public BeautyItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mName = itemView.findViewById(R.id.beauty_item_description);
            mSubscription = itemView.findViewById(R.id.beauty_item_subscription);
            mImage = itemView.findViewById(R.id.beauty_item_iv);
        }
    }

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void onItemClick(int position, BeautyItem item);
    }
}
