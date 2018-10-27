package com.ashokvarma.sqlitemanager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Class description :
 * ViewType is the column Count
 *
 * @author ashokvarma
 * @version 1.0
 * @see RecyclerView.Adapter
 * @since 18 Jun 2017
 */
class TableRecyclerAdapter extends RecyclerView.Adapter<TableRecyclerAdapter.ViewHolder> {

    private List<SparseArray<Object>> mColumnIndexToValuesArray;
    private Listener mListener;

    TableRecyclerAdapter(@Nullable List<SparseArray<Object>> columnIndexToValuesArray) {
        mColumnIndexToValuesArray = columnIndexToValuesArray;
    }

    @Override
    public int getItemViewType(int position) {
        return mColumnIndexToValuesArray.get(position).size();
    }

    void setData(List<SparseArray<Object>> mColumnIndexToValuesArray) {
        this.mColumnIndexToValuesArray = mColumnIndexToValuesArray;
        notifyDataSetChanged();
    }

    void setListener(Listener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new RowView(parent.getContext(), viewType));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mRowView.setData(mColumnIndexToValuesArray.get(position));
    }

    @Override
    public int getItemCount() {
        if (mColumnIndexToValuesArray == null) {
            return 0;
        }

        return mColumnIndexToValuesArray.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final RowView mRowView;

        ViewHolder(RowView itemView) {
            super(itemView);
            mRowView = itemView;

            mRowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    int layoutPosition = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onColumnValueClicked(mColumnIndexToValuesArray.get(layoutPosition));
                    }
                }
            });
        }
    }

    interface Listener {
        void onColumnValueClicked(SparseArray<Object> columnValues);
    }
}
