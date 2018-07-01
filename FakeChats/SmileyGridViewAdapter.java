package com.eL.FakeChats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Omar Sheikh on 8/13/2017.
 */
public class SmileyGridViewAdapter extends BaseAdapter {
    int [] smileyArray;
    Context context;
    SmileyGridViewAdapter(Context context, int [] smileyArray){
        this.context = context;
        this.smileyArray = smileyArray;
    }
    @Override
    public int getCount() {
        return smileyArray.length;
    }
    @Override
    public Object getItem(int i) {
        return smileyArray[i];
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflatero = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflatero.inflate(R.layout.singlesimelydisplay_layout,viewGroup,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else
        {
            holder =(ViewHolder) view.getTag();
        }
        holder.smileyImage.setImageResource(smileyArray[position]);
        return view;
    }
    private class ViewHolder{
        ImageView smileyImage;
        ViewHolder(View v){
            smileyImage = (ImageView) v.findViewById(R.id.SingleSmileyDisplaySmiley);
        }
    }
}