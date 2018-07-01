package com.eL.FakeChats;

/**
 * Created by Omar Sheikh on 7/28/2017.
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
