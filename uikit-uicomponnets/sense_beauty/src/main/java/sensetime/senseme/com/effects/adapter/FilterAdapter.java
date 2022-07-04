package sensetime.senseme.com.effects.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sensetime.senseme.com.effects.R;
import sensetime.senseme.com.effects.utils.GlideUtils;
import sensetime.senseme.com.effects.view.FilterItem;
import sensetime.senseme.com.effects.view.widget.ILinkageAdapter;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.NormalViewHolder> implements ILinkageAdapter {

    private final List<FilterItem> mFilterList = new ArrayList<>();
    private View.OnClickListener mOnClickFilterListener;
    private int mSelectedPosition = 0;
    Context mContext;
    private final LayoutInflater mLayoutInflater;
    private Listener mListener;

    public FilterAdapter(List<FilterItem> list, Context context) {
        mFilterList.addAll(list);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public FilterAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NormalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NormalViewHolder(mLayoutInflater.inflate(R.layout.filter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final NormalViewHolder holder, final int position) {
        final FilterItem data = mFilterList.get(position);
//        holder.imageView.setImageBitmap(mFilterList.get(position).icon);
        GlideUtils.INSTANCE.load(mContext, data.iconPath, holder.imageView);
        holder.textView.setText(mFilterList.get(position).name);
        holder.iv_bg.setVisibility(View.INVISIBLE);
        holder.itemView.setSelected(mSelectedPosition == position);
        holder.textView.setTextColor(Color.parseColor("#A7A7A7"));
        if (mSelectedPosition == position) {
            holder.textView.setText(mFilterList.get(position).name);
            holder.iv_bg.setVisibility(View.VISIBLE);
            holder.textView.setTextColor(Color.parseColor("#ffffff"));
        }

        if (mOnClickFilterListener != null) {
            holder.itemView.setTag(position);

            holder.itemView.setOnClickListener(mOnClickFilterListener);
            holder.itemView.setSelected(mSelectedPosition == position);
        }

        holder.imageView.setOnClickListener(view -> {
            holder.itemView.setTag(position);
            holder.itemView.setSelected(mSelectedPosition == position);
            mListener.onItemClickSticker(position, data, true, FilterAdapter.this);
            setSelectedPosition(position);
        });
    }

    public void setClickFilterListener(View.OnClickListener listener) {
        mOnClickFilterListener = listener;
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    @Override
    public void refreshData(@NotNull List<?> data) {
        mFilterList.clear();
        mFilterList.addAll((Collection<? extends FilterItem>) data);
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public RecyclerView.Adapter<?> getAdapter() {
        return this;
    }

    @Override
    public void setListener(@NotNull Listener listener) {
        mListener = listener;
    }

    static class NormalViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView iv_bg;
        TextView textView;
        ImageView imageView;

        public NormalViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.iv_filter_image);
            textView = (TextView) itemView.findViewById(R.id.filter_text);
            iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
        }
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        notifyDataSetChanged();
    }
}
