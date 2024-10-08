package com.sofudev.trackapptrl.Util;

import android.util.SparseArray;
import android.view.View;

@SuppressWarnings({ "unchecked" })
public class ViewFindUtils {
    public static <T extends View> T hold(View view, int id)
    {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null)
        {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);

        if (childView == null)
        {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        return (T) childView;
    }

    public static <T extends View> T find(View view, int id)
    {
        return (T) view.findViewById(id);
    }
}
