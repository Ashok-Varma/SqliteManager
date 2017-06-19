package com.ashokvarma.sqlitemanager;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see LinearLayout
 * @since 18 Jun 2017
 */
class RowView extends LinearLayout {

    private final int mColumnCount;
    private final ArrayList<TextView> mTextViewList;
    private SparseArray<String> mColumnIndexToValues;

    RowView(Context context, int columnCount) {
        super(context);

        setOrientation(HORIZONTAL);

//        TypedValue outValue = new TypedValue();
//        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
//        setBackgroundResource(outValue.resourceId);

        mColumnCount = columnCount;
        mTextViewList = new ArrayList<>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            TextView textView = getTextView();
            mTextViewList.add(textView);
            addView(textView);
        }
    }

    private TextView getTextView() {
        return (TextView) LayoutInflater.from(getContext()).inflate(R.layout.sqlite_manager_column_text_item, this, false);
    }

    void setData(SparseArray<String> columnIndexToValues) {
        if (columnIndexToValues.size() != mColumnCount) {
            throw new IllegalArgumentException("columnIndexValues count doesn't match columnCount");
        }

        mColumnIndexToValues = columnIndexToValues;

        for (int i = 0; i < mColumnIndexToValues.size(); i++) {
            String columnEntry = mColumnIndexToValues.get(i);

            if (columnEntry == null) {
                mTextViewList.get(i).setText("(NULL)");
                mTextViewList.get(i).setTextColor(ContextCompat.getColor(getContext(), R.color.sqlite_manager_txt_disabled));
            } else if (TextUtils.isEmpty(columnEntry)) {
                mTextViewList.get(i).setText("(EMPTY)");
                mTextViewList.get(i).setTextColor(ContextCompat.getColor(getContext(), R.color.sqlite_manager_txt_disabled));
            } else {
                mTextViewList.get(i).setText(columnEntry);
                mTextViewList.get(i).setTextColor(ContextCompat.getColor(getContext(), R.color.sqlite_manager_txt_secondary));
            }

        }
    }
}
