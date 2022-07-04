package sensetime.senseme.com.effects.adapter;

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
import sensetime.senseme.com.effects.view.FullBeautyItem;

public class FullBeautyItemAdapter extends RecyclerView.Adapter{

    ArrayList<FullBeautyItem> mFullBeautyItem;
    private View.OnClickListener mOnClickBeautyItemListener;
    private int mSelectedPosition = 0;
    Context mContext;

    public FullBeautyItemAdapter(Context context, ArrayList<FullBeautyItem> list){
        mContext = context;
        mFullBeautyItem = list;
    }

    public ArrayList<FullBeautyItem> getData() {
        return mFullBeautyItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beauty_item, null);
        return new BeautyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BeautyItemViewHolder viewHolder = (BeautyItemViewHolder) holder;
        viewHolder.mName.setText(mFullBeautyItem.get(position).getText());
        viewHolder.mSubscription.setVisibility(View.INVISIBLE);
        viewHolder.mName.setTextColor(Color.parseColor("#ffffff"));
        viewHolder.mSubscription.setTextColor(Color.parseColor("#ffffff"));
        viewHolder.mImage.setImageBitmap(mFullBeautyItem.get(position).getUnselectedIcon());
        holder.itemView.setSelected(mSelectedPosition == position);
        if(mSelectedPosition == position){
            viewHolder.mSubscription.setTextColor(Color.parseColor("#bc47ff"));
            viewHolder.mName.setTextColor(Color.parseColor("#bc47ff"));
            viewHolder.mImage.setImageBitmap(mFullBeautyItem.get(position).getSelectedIcon());
        }
        if (mOnClickBeautyItemListener != null) {
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mOnClickBeautyItemListener);
            holder.itemView.setSelected(mSelectedPosition == position);

        }
    }

    @Override
    public int getItemCount() {
        return mFullBeautyItem.size();
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    public void setClickBeautyListener(View.OnClickListener listener) {
        mOnClickBeautyItemListener = listener;
    }

    static class BeautyItemViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView mImage;
        TextView mName;
        TextView mSubscription;

        public BeautyItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mName = (TextView) itemView.findViewById(R.id.beauty_item_description);
            mSubscription = (TextView) itemView.findViewById(R.id.beauty_item_subscription);
            mImage = (ImageView) itemView.findViewById(R.id.beauty_item_iv);
        }
    }
}
