package com.eL.FakeChats;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Omar Sheikh on 7/29/2017.
 */
public class EditUsername extends Activity {


    String currentUsername;
    EditText usernameEditText;
    Button updateButton, cancelButton;
    public void ShowSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null){
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }
    }
    public void HideSoftKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void SetStatusBar(){
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(EditUsername.this,R.color.grayShade2));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editusername_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        SetStatusBar();
        InitializeStuff();
        SetListners();
        ShowSoftKeyboard();
        SetCurrentUsername();
    }
    public void UpdateUsername(){
        String userName = usernameEditText.getText().toString();
        if(userName.length() > 0){
            HideSoftKeyboard();
            Intent intent = new Intent();
            intent.putExtra("NewUsername",userName);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Username cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
    public void InitializeStuff(){
        usernameEditText = (EditText) findViewById(R.id.EditUsernameEditText);
        updateButton = (Button) findViewById(R.id.EditUserNameUpdateButton);
        cancelButton = (Button) findViewById(R.id.EditUserNameCancelButton);
    }
    public void SetCurrentUsername(){
        currentUsername = getIntent().getExtras().getString("CurrentUsername");
        if(!currentUsername.equals(""))
            usernameEditText.setText(currentUsername);
    }
    public void SetListners(){
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUsername();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideSoftKeyboard();
                finish();
            }
        });
    }
}
