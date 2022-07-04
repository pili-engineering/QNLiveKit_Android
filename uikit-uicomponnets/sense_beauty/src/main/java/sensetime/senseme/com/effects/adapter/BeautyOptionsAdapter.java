package sensetime.senseme.com.effects.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sensetime.senseme.com.effects.R;
import sensetime.senseme.com.effects.view.BeautyOptionsItem;

public class BeautyOptionsAdapter extends RecyclerView.Adapter {
    private ArrayList<BeautyOptionsItem> mBeautyOptions = new ArrayList<>();
    private int mSelectedPosition = 0;
    private Context mContext;
    private OnItemClickListener mListener;

    public BeautyOptionsAdapter(ArrayList<BeautyOptionsItem> list, Context context) {
        mBeautyOptions = list;
        mContext = context;
    }

    public List<BeautyOptionsItem> getData() {
        return mBeautyOptions;
    }

    public void refreshData(ArrayList<BeautyOptionsItem> data) {
        mBeautyOptions.clear();
        mBeautyOptions.addAll(data);
        notifyDataSetChanged();
    }

    public BeautyOptionsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beauty_options_item, null);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final FilterViewHolder viewHolder = (FilterViewHolder) holder;
        final BeautyOptionsItem entity = mBeautyOptions.get(position);
        viewHolder.mName.setText(mBeautyOptions.get(position).name);
        viewHolder.mName.setTextColor(Color.parseColor("#80FFFFFF"));
        viewHolder.mFlag.setVisibility(View.INVISIBLE);
        holder.itemView.setSelected(mSelectedPosition == position);
        if (mSelectedPosition == position) {
            viewHolder.mFlag.setVisibility(View.VISIBLE);
            viewHolder.mName.setTextColor(Color.parseColor("#ffffff"));
        }
//        if (mOnClickBeautyListener != null) {
//            holder.itemView.setTag(position);
//            holder.itemView.setOnClickListener(mOnClickBeautyListener);
//            holder.itemView.setSelected(mSelectedPosition == position);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) {
                    holder.itemView.setTag(position);
                    holder.itemView.setSelected(mSelectedPosition == position);
                    mListener.onItemClick(position, entity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeautyOptions.size();
    }

    static class FilterViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView mName;
        View mFlag;

        public FilterViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mName = (TextView) itemView.findViewById(R.id.iv_beauty_options);
            mFlag = itemView.findViewById(R.id.iv_select_flag);
        }
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, BeautyOptionsItem entity);
    }

    public BeautyOptionsItem getPositionItem(int index) {
        if (index >= 0 && index < getItemCount()) {
            return mBeautyOptions.get(index);
        }
        return null;
    }

    public BeautyOptionsItem getItem(int position) {
        if (position >= 0 && position < getItemCount()) {
            return mBeautyOptions.get(position);
        }
        return null;
    }
}
