package com.ashokvarma.sqlitemanager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see LinearLayout
 * @since 18 Jun 2017
 */
@SuppressLint("Instantiatable")
class ColumnNameView extends LinearLayout implements View.OnClickListener {

    private String[] mTableColumnNames;

    public static final byte NO_SORT = 0;
    public static final byte ASCENDING_SORT = 1;
    public static final byte DESCENDING_SORT = 2;

    private ColumnHeaderSortChangeListener mColumnHeaderSortChangeListener;

    public ColumnNameView(Context context) {
        this(context, null);
    }

    public ColumnNameView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColumnNameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColumnNameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
    }

    private TextView getTextView() {
        return (TextView) LayoutInflater.from(getContext()).inflate(R.layout.sqlite_manager_column_text_header_item, this, false);
    }

    ColumnNameView setData(String[] tableColumnNames) {
        removeAllViews();

        mTableColumnNames = tableColumnNames;

        int index = 0;
        for (String currentColumnName : mTableColumnNames) {
            TextView textView = getTextView();
            TableNameHeaderViewTag tableNameHeaderViewTag = new TableNameHeaderViewTag();
            tableNameHeaderViewTag.columnIndex = index;
            tableNameHeaderViewTag.sortOrder = NO_SORT;
            textView.setTag(tableNameHeaderViewTag);
            textView.setText(currentColumnName);
            textView.setOnClickListener(this);
            addView(textView);
            index++;
        }
        return this;
    }

    private class TableNameHeaderViewTag {
        private byte sortOrder = 0;
        private int columnIndex = 0;
    }

    @Override
    public void onClick(View v) {
        TableNameHeaderViewTag tableNameHeaderViewTag = (TableNameHeaderViewTag) v.getTag();
        byte nextSortOrder = getNextSortOrder(tableNameHeaderViewTag.sortOrder);

        for (int i = 0; i < getChildCount(); i++) {
            View currentChild = getChildAt(i);
            if (currentChild instanceof TextView) {
                ((TableNameHeaderViewTag) currentChild.getTag()).sortOrder = NO_SORT;
                int columnIndex = ((TableNameHeaderViewTag) currentChild.getTag()).columnIndex;
                ((TextView) currentChild).setText(mTableColumnNames[columnIndex]);
            }
        }

        boolean isAscendingOrder = nextSortOrder == ASCENDING_SORT;
        if (mColumnHeaderSortChangeListener != null) {
            mColumnHeaderSortChangeListener.onHeaderColumnSortChanged(mTableColumnNames[tableNameHeaderViewTag.columnIndex], isAscendingOrder);
        }

        tableNameHeaderViewTag.sortOrder = nextSortOrder;
        Drawable ascendingDescendingIcon;
        if (isAscendingOrder) {
            ascendingDescendingIcon = getContext().getResources().getDrawable(R.drawable.ic_sort_ascending_white_24dp);
        } else {
            ascendingDescendingIcon = getContext().getResources().getDrawable(R.drawable.ic_sort_descending_white_24dp);
        }

        String tableName = mTableColumnNames[tableNameHeaderViewTag.columnIndex];
        Spannable spannableString = new SpannableString(tableName + "   ");
        ascendingDescendingIcon.setBounds(0, 0, 56, 56);
        ImageSpan image = new ImageSpan(ascendingDescendingIcon, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(image, tableName.length() + 2, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ((TextView) v).setText(spannableString);
    }

    public void setColumnHeaderSortChangeListener(ColumnHeaderSortChangeListener columnHeaderSortChangeListener) {
        this.mColumnHeaderSortChangeListener = columnHeaderSortChangeListener;
    }

    public String[] getTableColumnNames() {
        return mTableColumnNames;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Business Logic
    ///////////////////////////////////////////////////////////////////////////

    private byte getNextSortOrder(int currentSortOrder) {
        if (currentSortOrder == NO_SORT) {
            return ASCENDING_SORT;
        } else if (currentSortOrder == ASCENDING_SORT) {
            return DESCENDING_SORT;
        } else if (currentSortOrder == DESCENDING_SORT) {
            return ASCENDING_SORT;
        }
        return ASCENDING_SORT;
    }

    interface ColumnHeaderSortChangeListener {
        void onHeaderColumnSortChanged(String columnName, boolean isAscendingOrder);
    }
}