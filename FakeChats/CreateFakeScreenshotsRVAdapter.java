package com.eL.FakeChats;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Omar Sheikh on 7/30/2017.
 */
public class CreateFakeScreenshotsRVAdapter extends RecyclerView.Adapter<CreateFakeScreenshotsRVAdapter.FakeScreenshotsViewHolder> {

    Context context;
    ArrayList<FakeChatClass> chatArray;
    Bitmap friendProfilePicture;


    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

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

    public CreateFakeScreenshotsRVAdapter(Context context, ArrayList<FakeChatClass> chatArray, Bitmap friendProfilePicture){
        this.context = context;
        this.chatArray = new ArrayList<>(chatArray);
        this.friendProfilePicture = friendProfilePicture;
    }


    @Override
    public FakeScreenshotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fakescreenshotsinglemessage_layout,parent,false);
        return new FakeScreenshotsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(FakeScreenshotsViewHolder viewHolder, int position) {
        FakeChatClass currentChat = chatArray.get(position);

        if(currentChat.getId() == 0) { //user
            viewHolder.friendRelativeLayout.setVisibility(View.GONE);
            viewHolder.userRelativeLayout.setVisibility(View.VISIBLE);
            if(currentChat.getSmileyPositions().size() != currentChat.getMessage().length())
                SetUserTextViewBackground(viewHolder,position);
            SetTextMessageWithImages(viewHolder.userTextView,currentChat.getMessage(),currentChat.getSmileyPositions(),currentChat.getSmileyTypes());
            if(position > 0 && chatArray.get(position - 1).getId() == 1)
                SetTextViewMargins(viewHolder);
        }
        else{
            viewHolder.friendRelativeLayout.setVisibility(View.VISIBLE);
            viewHolder.userRelativeLayout.setVisibility(View.GONE);
            SetFriendTextViewBackground(viewHolder,position);
            SetTextMessageWithImages(viewHolder.friendTextView,currentChat.getMessage(),currentChat.getSmileyPositions(),currentChat.getSmileyTypes());
            if(position > 0 && chatArray.get(position - 1).getId() == 0)
                SetTextViewMargins(viewHolder);
            if(currentChat.getSmileyPositions().size() == currentChat.getMessage().length()) {
                viewHolder.friendTextView.setBackgroundResource(0);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.RIGHT_OF,R.id.FakeScreenshotSingleMessageFriendImage);
                layoutParams.bottomMargin = 0;
                layoutParams.topMargin = 0;
                layoutParams.leftMargin = dpToPx(5);
                viewHolder.friendTextView.setLayoutParams(layoutParams);
                viewHolder.friendTextView.setPadding(0,0,0,0);
            }
        }
    }
    public void SetUserTextViewBackground(FakeScreenshotsViewHolder viewHolder, int position){
        if(position == 0){
            if(position + 1 < chatArray.size()){
                if(chatArray.get(position + 1).getId() == 0) // top
                    viewHolder.userTextView.setBackgroundResource(R.drawable.user_roundedtextview_top);
                else // alone
                    viewHolder.userTextView.setBackgroundResource(R.drawable.user_roundedtextview_alone);
            }
            else // alone
                viewHolder.userTextView.setBackgroundResource(R.drawable.user_roundedtextview_alone);
        }
        else if(position > 0 && position < chatArray.size() - 1){
            if(chatArray.get(position - 1).getId() == 0 && chatArray.get(position + 1).getId() == 0) // middle
                viewHolder.userTextView.setBackgroundResource(R.drawable.user_roundedtextview_middle);
            else if(chatArray.get(position - 1).getId() == 0) // bottom
                viewHolder.userTextView.setBackgroundResource(R.drawable.user_roundedtextview_bottom);
            else if(chatArray.get(position + 1).getId() == 0) // top
                viewHolder.userTextView.setBackgroundResource(R.drawable.user_roundedtextview_top);
            else // alone
                viewHolder.userTextView.setBackgroundResource(R.drawable.user_roundedtextview_alone);

        }
        else{
            if(chatArray.get(position - 1).getId() == 0) // bottom
                viewHolder.userTextView.setBackgroundResource(R.drawable.user_roundedtextview_bottom);
            else // alone
                viewHolder.userTextView.setBackgroundResource(R.drawable.user_roundedtextview_alone);
        }
    }
    public void SetFriendTextViewBackground(FakeScreenshotsViewHolder viewHolder, int position){
        if(position == 0){
            if(position + 1 < chatArray.size()){
                if(chatArray.get(position + 1).getId() == 1) { // top
                    viewHolder.friendTextView.setBackgroundResource(R.drawable.friend_roundedtextview_top);
                    viewHolder.friendImageView.setVisibility(View.INVISIBLE);

                }
                else { // alone
                    viewHolder.friendTextView.setBackgroundResource(R.drawable.friend_roundedtextview_alone);
                    viewHolder.friendImageView.setImageBitmap(friendProfilePicture);
                }
            }
            else { // alone
                viewHolder.friendTextView.setBackgroundResource(R.drawable.friend_roundedtextview_alone);
                viewHolder.friendImageView.setImageBitmap(friendProfilePicture);
            }
        }
        else if(position > 0 && position < chatArray.size() - 1){
            if(chatArray.get(position - 1).getId() == 1 && chatArray.get(position + 1).getId() == 1) { // middle
                viewHolder.friendTextView.setBackgroundResource(R.drawable.friend_roundedtextview_middle);
                viewHolder.friendImageView.setVisibility(View.INVISIBLE);
            }
            else if(chatArray.get(position - 1).getId() == 1) { // bottom
                viewHolder.friendTextView.setBackgroundResource(R.drawable.friend_roundedtextview_bottom);
                viewHolder.friendImageView.setImageBitmap(friendProfilePicture);
            }
            else if (chatArray.get(position + 1).getId() == 1) { // top
                viewHolder.friendTextView.setBackgroundResource(R.drawable.friend_roundedtextview_top);
                viewHolder.friendImageView.setVisibility(View.INVISIBLE);
            }
            else{ // alone
                viewHolder.friendTextView.setBackgroundResource(R.drawable.friend_roundedtextview_alone);
                viewHolder.friendImageView.setImageBitmap(friendProfilePicture);
            }
        }
        else{
            if(chatArray.get(position - 1).getId() == 1) { // bottom
                viewHolder.friendTextView.setBackgroundResource(R.drawable.friend_roundedtextview_bottom);
                viewHolder.friendImageView.setImageBitmap(friendProfilePicture);
            }
            else { // alone
                viewHolder.friendTextView.setBackgroundResource(R.drawable.friend_roundedtextview_alone);
                viewHolder.friendImageView.setImageBitmap(friendProfilePicture);
            }
        }
    }
    public void SetTextViewMargins(FakeScreenshotsViewHolder viewHolder){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = dpToPx(7);
        viewHolder.mainRelativeLayout.setLayoutParams(layoutParams);

    }

    private void SetTextMessageWithImages(TextView textView, String message, ArrayList<Integer> smileyPositions, ArrayList<Integer> smileyTypes ) {
        SpannableString spannableString = new SpannableString(message);
        for(int i = 0 ; i < smileyPositions.size() ; i++){
            Drawable smileyDrawable = ContextCompat.getDrawable(context, smileyArray[smileyTypes.get(i)]);
            int lineHeight = textView.getLineHeight();
            if(smileyPositions.size() == message.length()) {
                smileyDrawable.setBounds(dpToPx(2), 0, lineHeight + dpToPx(14), lineHeight + dpToPx(12));
            }
            else
                smileyDrawable.setBounds(dpToPx(2),0,lineHeight + dpToPx(2) ,lineHeight); // left top right bottom
            ImageSpan imageSpan = new ImageSpan(smileyDrawable);
            spannableString.setSpan(imageSpan,smileyPositions.get(i),smileyPositions.get(i) + 1,0);

        }
        textView.setText(spannableString);
        textView.setGravity(Gravity.CENTER_VERTICAL);
    }
    @Override
    public int getItemCount() {
        return chatArray.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public class FakeScreenshotsViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout userRelativeLayout, friendRelativeLayout, mainRelativeLayout;
        CircleImageView friendImageView;
        TextView userTextView,friendTextView;
        //CardView friendCardView;
        public FakeScreenshotsViewHolder(View itemView) {
            super(itemView);

            mainRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.CreateFakeScreenshotsSingleMessageMainRL);

            userRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.FakeScreenshotSingleMessageUserRL);
            friendRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.FakeScreenshotSingleMessageFriendRL);

            friendImageView = (CircleImageView) itemView.findViewById(R.id.FakeScreenshotSingleMessageFriendImage);

            userTextView = (TextView) itemView.findViewById(R.id.FakeScreenshotSingleMessageUserText);
            friendTextView = (TextView) itemView.findViewById(R.id.FakeScreenshotSingleMessageFriendText);
           // friendCardView = (CardView) itemView.findViewById(R.id.CardView);

        }
    }


}
