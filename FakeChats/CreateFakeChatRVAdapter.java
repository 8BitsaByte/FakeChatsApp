package com.eL.FakeChats;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Omar Sheikh on 7/27/2017.
 */
public class CreateFakeChatRVAdapter extends RecyclerView.Adapter<CreateFakeChatRVAdapter.FakeChatViewHolder> implements ItemTouchHelperAdapter {

    Context context;
    ArrayList<FakeChatClass> chatArray;
    Bitmap friendPicture;

    public OnItemClickListener mItemClickListener;
    public OnStartDragListener mDragStartListener;

    private static final int TYPE_ITEM = 0;

    int []smileyArray = {R.drawable.fbchat_thumbsupbutton,R.drawable.e1,R.drawable.e2,R.drawable.e3,R.drawable.e4,R.drawable.e5,R.drawable.e6,R.drawable.e7,R.drawable.e8,
            R.drawable.e9,R.drawable.e10,R.drawable.e11,R.drawable.e12,R.drawable.e13,R.drawable.e14,R.drawable.e15,R.drawable.e16,
            R.drawable.e17,R.drawable.e18,R.drawable.e19,R.drawable.e20,R.drawable.e21,R.drawable.e22,R.drawable.e23,R.drawable.e24,
            R.drawable.e25,R.drawable.e26,R.drawable.e27,R.drawable.e28,R.drawable.e29,R.drawable.e30,R.drawable.e31,R.drawable.e32,
            R.drawable.e33,R.drawable.e34,R.drawable.e35,R.drawable.e36,R.drawable.e37,R.drawable.e38,R.drawable.e39,R.drawable.e40,
            R.drawable.e41,R.drawable.e42,R.drawable.e43,R.drawable.e44,R.drawable.e45,R.drawable.e46,R.drawable.e47,R.drawable.e48,
            R.drawable.e49,R.drawable.e50,R.drawable.e51,R.drawable.e52,R.drawable.e53,R.drawable.e54,R.drawable.e55,R.drawable.e56,
            R.drawable.e57,R.drawable.e58,R.drawable.e59,R.drawable.e60,R.drawable.e61,R.drawable.e62,R.drawable.e63,R.drawable.e64,
            R.drawable.e65,R.drawable.e66,R.drawable.e67,R.drawable.e68,R.drawable.e69,R.drawable.e70,R.drawable.e71,R.drawable.e72,
            R.drawable.e73,R.drawable.e74,R.drawable.e75,R.drawable.e76,R.drawable.e77,R.drawable.e78,R.drawable.e79,R.drawable.e80,
            R.drawable.e81,R.drawable.e82,R.drawable.e83,R.drawable.e84,R.drawable.e85,R.drawable.e86,R.drawable.e87,R.drawable.e88,
            R.drawable.e89,R.drawable.e90,R.drawable.e91,R.drawable.e92,R.drawable.e93,R.drawable.e94,R.drawable.e95,R.drawable.e96,
            R.drawable.e97,R.drawable.e98,R.drawable.e99,R.drawable.e100};

    public CreateFakeChatRVAdapter(Context context, OnStartDragListener dragListener , Bitmap friendPicture){
        this.context = context;
        this.mDragStartListener = dragListener;
        this.friendPicture = friendPicture;
        this.chatArray = new ArrayList<>();
    }
    public void UpdateAdapter(FakeChatClass fakeChat){
        chatArray.add(new FakeChatClass(fakeChat.getId(),fakeChat.getMessage(),fakeChat.getSmileyPositions(),fakeChat.getSmileyTypes()));
        notifyItemInserted(chatArray.size());
    }
    public void ClearAdapter(){
        chatArray.clear();
        notifyDataSetChanged();
    }
    public void RemoveItem(int position){
        chatArray.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public FakeChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fakechatsinglemessage_layout,parent,false);
        return new FakeChatViewHolder(view);
    }
    public ArrayList<FakeChatClass> GetChatArray(){
        return chatArray;
    }

    @Override
    public void onBindViewHolder(FakeChatViewHolder viewHolder, int position) {
        FakeChatClass singleFakeChat = chatArray.get(position);
        if(singleFakeChat.getId() == 0){ // user
            SetTextMessageWithImages(viewHolder.userTextMessage,singleFakeChat.getMessage(),singleFakeChat.getSmileyPositions(),singleFakeChat.getSmileyTypes());
            viewHolder.userRelativeLayout.setVisibility(View.VISIBLE);
            viewHolder.friendRelativeLayout.setVisibility(View.GONE);
        }
        else{
            SetTextMessageWithImages(viewHolder.friendTextMessage,singleFakeChat.getMessage(),singleFakeChat.getSmileyPositions(),singleFakeChat.getSmileyTypes());
            viewHolder.friendRelativeLayout.setVisibility(View.VISIBLE);
            viewHolder.userRelativeLayout.setVisibility(View.GONE);
            viewHolder.friendPicture.setImageBitmap(friendPicture);
        }
        SetListners(viewHolder);
    }

    private void SetTextMessageWithImages(TextView textView,String message, ArrayList<Integer> smileyPositions, ArrayList<Integer> smileyTypes ) {
        SpannableString spannableString = new SpannableString(message);
        for(int i = 0 ; i < smileyPositions.size() ; i++){
            if(smileyPositions.get(i) < message.length() && "-".equals(String.valueOf(message.charAt(smileyPositions.get(i))))) {
                Drawable smileyDrawable = ContextCompat.getDrawable(context, smileyArray[smileyTypes.get(i)]);
                int lineHeight = textView.getLineHeight();
                smileyDrawable.setBounds(0, 0, lineHeight, lineHeight);
                ImageSpan imageSpan = new ImageSpan(smileyDrawable);
                spannableString.setSpan(imageSpan, smileyPositions.get(i), smileyPositions.get(i) + 1, 0);
            }
        }
        textView.setText(spannableString);
    }
    public void SetListners(final FakeChatViewHolder viewHolder){
        viewHolder.userSwipeImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });

        viewHolder.friendSwipeImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });

        viewHolder.userDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveItem(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.friendDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveItem(viewHolder.getAdapterPosition());
            }
        });
    }
    @Override
    public int getItemCount() {
        return chatArray.size();
    }
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        if (fromPosition < chatArray.size() && toPosition < chatArray.size()) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(chatArray, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(chatArray, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        chatArray.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class FakeChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,ItemTouchHelperViewHolder{
        RelativeLayout userRelativeLayout, friendRelativeLayout;
        TextView userTextMessage,friendTextMessage;
        ImageButton userSwipeImageButton, friendSwipeImageButton, userDeleteButton, friendDeleteButton;
        CircleImageView friendPicture;
        public FakeChatViewHolder(View itemView) {
            super(itemView);
            userRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.FakeChatSingleMessageUserMessageRL);
            friendRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.FakeChatSingleMessageFriendMessageRL);

            userTextMessage = (TextView) itemView.findViewById(R.id.FakeChatSingleMessageUserMessage);
            friendTextMessage = (TextView) itemView.findViewById(R.id.FakeChatSingleMessageFriendMessage);

            userSwipeImageButton = (ImageButton) itemView.findViewById(R.id.FakeChatSingleMessageUserMessageSwipeButton);
            friendSwipeImageButton = (ImageButton) itemView.findViewById(R.id.FakeChatSingleMessageFriendMessageSwipeButton);

            userDeleteButton = (ImageButton) itemView.findViewById(R.id.FakeChatSingleMessageUserMessageDeleteButton);
            friendDeleteButton = (ImageButton) itemView.findViewById(R.id.FakeChatSingleMessageFriendMessageDeleteButton);

            friendPicture = (CircleImageView) itemView.findViewById(R.id.FakeChatSingleMessageFriendPicture);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getPosition());
            }
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {

        }
    }
}
