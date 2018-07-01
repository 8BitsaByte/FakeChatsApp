package com.eL.FakeChats;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * Created by Omar Sheikh on 7/26/2017.
 */
public class CreateFakeChat extends AppCompatActivity implements OnStartDragListener {


    public static Activity ChatActivity;

    RecyclerView fakeChatRecyclerView;
    CreateFakeChatRVAdapter fakeChatAdapter;

    EditText sendMessageEditText;
    TextView sendMessageFriendTextView;
    ImageView sendMessageFriendImageView;

    RelativeLayout sendMessageFriendRL,sendMessageUserRL;
    boolean firstMessageBoolean;

    ItemTouchHelper mItemTouchHelper;

    String username;
    Bitmap profilePicture;

    ImageButton previousButton, nextButton;
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

    SmileyGridViewAdapter smileyAdapter;

    LinearLayout smileyLinearLayout;
    ImageButton smileyBackButton;
    ImageButton smileyShowButton;
    boolean isSmileyShown;
    GridView smileyGridView;

    ArrayList<Integer> smileyPositions;
    ArrayList<Integer> smileyTypes;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void SetStatusBar(){
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(CreateFakeChat.this,R.color.grayShade2));
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    public void ShrinkSmileyTab(){
        isSmileyShown = false;
        smileyLinearLayout.setVisibility(View.GONE);
    }
    public void GrowSmileyTab(){
        isSmileyShown = true;
        smileyLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createfakechat_layout);
        ChatActivity = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ButterKnife.bind(this);
        SetStatusBar();
        InitializeStuff();
        SetListners();
        LoadFromPreviousActivity();
        SetRecyclerViewandAdapter();
        SetSmileyListViewandAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (profilePicture != null && !profilePicture.isRecycled()) {
            profilePicture.recycle();
            profilePicture = null;
        }
    }
    @Override
    public void onBackPressed() {
        PromptToGoBack();
    }

    public void PromptToGoBack(){
        new AlertDialog.Builder(this)
                .setMessage("The chats will be deleted" +
                        "\nDo you still wish to go back?")
                .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }}).show();
    }

    public void InitializeStuff(){

        sendMessageEditText  = (EditText) findViewById(R.id.CreateFakeChatChatEditText);

        sendMessageFriendTextView = (TextView) findViewById(R.id.CreateFakeChatFriendUsernameTextView);
        sendMessageFriendImageView = (ImageView) findViewById(R.id.CreateFakeChatAddToFriendImage);

        sendMessageFriendRL = (RelativeLayout) findViewById(R.id.CreateFakeChatAddToFriendRL);
        sendMessageUserRL = (RelativeLayout) findViewById(R.id.CreateFakeChatAddToUserRL);

        firstMessageBoolean = true;

        previousButton = (ImageButton) findViewById(R.id.CreateFakeChatBack);
        nextButton = (ImageButton) findViewById(R.id.CreateFakeChatNext);

        smileyPositions = new ArrayList<>();
        smileyTypes = new ArrayList<>();

        smileyLinearLayout = (LinearLayout) findViewById(R.id.CreateFakeChatSmileyLL);
        smileyBackButton = (ImageButton) findViewById(R.id.CreateFakeChatSmileyBackButton);
        smileyShowButton = (ImageButton) findViewById(R.id.CreateFakeChatChatSmiley);
        isSmileyShown = false;


    }
    public void SetListners(){
        sendMessageUserRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMessage(0);
            }
        });
        sendMessageFriendRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMessage(1);
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptToGoBack();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoNextActivity();
            }
        });

        smileyBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShrinkSmileyTab();
            }
        });

        smileyShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSmileyShown)
                    GrowSmileyTab();
                else
                    ShrinkSmileyTab();
            }
        });
    }
    public void LoadFromPreviousActivity(){
        Intent intent = getIntent();
        if(intent !=null) {
            username = intent.getExtras().getString("Username");
            profilePicture = (Bitmap) intent.getParcelableExtra("ProfilePicture");
            sendMessageFriendTextView.setText("Add message to \n" + username);
            sendMessageFriendImageView.setImageBitmap(profilePicture);
        }

    }
    public void GotoNextActivity(){
        ArrayList<FakeChatClass> tempArray = new ArrayList<>();
        if(!firstMessageBoolean)
            tempArray = new ArrayList<>(fakeChatAdapter.GetChatArray());
        Intent intent = new Intent(CreateFakeChat.this,CreateFakeScreenshots.class);
        intent.putExtra("Username",username);
        intent.putExtra("ProfilePicture",profilePicture);
        intent.putExtra("ChatArray",new ArrayList<>(tempArray));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void AddMessage(int code){
        String messageText = sendMessageEditText.getText().toString();
        if(!messageText.equals("")){
            if(firstMessageBoolean)
                fakeChatAdapter.ClearAdapter();
            fakeChatAdapter.UpdateAdapter(new FakeChatClass(code,messageText,smileyPositions,smileyTypes));
            fakeChatRecyclerView.smoothScrollToPosition(fakeChatAdapter.getItemCount());
            sendMessageEditText.setText("");
            smileyPositions.clear();
            smileyTypes.clear();
            firstMessageBoolean = false;
        }
        else
            Toast.makeText(getApplicationContext(),"Message cannot be empty",Toast.LENGTH_SHORT).show();

    }
    public void SetSmileyListViewandAdapter(){
        smileyAdapter = new SmileyGridViewAdapter(getApplicationContext(),smileyArray);
        smileyGridView = (GridView) findViewById(R.id.CreateFakeChatSmileyGridView);
        smileyGridView.setAdapter(smileyAdapter);

        smileyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                smileyPositions.add(sendMessageEditText.getText().length());
                smileyTypes.add(i);
                Drawable smileyDrawable = ContextCompat.getDrawable(CreateFakeChat.this, smileyArray[i]);
                if(smileyDrawable != null)
                    AddImageBetweenText(smileyDrawable);
            }
        });
    }
    public void SetRecyclerViewandAdapter(){
        fakeChatAdapter = new CreateFakeChatRVAdapter(getApplicationContext(),CreateFakeChat.this,profilePicture);
        fakeChatRecyclerView = (RecyclerView) findViewById(R.id.FakeChatRecyclerView);
        fakeChatRecyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new EditItemTouchHelperCallback(fakeChatAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(fakeChatRecyclerView);

        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(250);
        itemAnimator.setRemoveDuration(250);
        fakeChatRecyclerView.setItemAnimator(itemAnimator);

        fakeChatRecyclerView.setAdapter(fakeChatAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        fakeChatRecyclerView.setLayoutManager(mLayoutManager);


        fakeChatAdapter.UpdateAdapter(new FakeChatClass(0,"Users messages will be displayed here.\nThis message will automatically be deleted",smileyPositions,smileyTypes));
        fakeChatAdapter.UpdateAdapter(new FakeChatClass(1,username + "s messages will be displayed here.\nThis message will automatically be deleted",smileyPositions,smileyTypes));
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
    private void AddImageBetweenText(Drawable drawable) {
        drawable .setBounds(0, 0, dpToPx(20), dpToPx(20));

        int selectionCursor = sendMessageEditText.getSelectionStart();
        sendMessageEditText.getText().insert(selectionCursor, "-");
        selectionCursor = sendMessageEditText.getSelectionStart();

        SpannableStringBuilder builder = new SpannableStringBuilder(sendMessageEditText.getText());
        builder.setSpan(new ImageSpan(drawable), selectionCursor - "-".length(), selectionCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sendMessageEditText.setText(builder);
        sendMessageEditText.setSelection(selectionCursor);
    }
}

