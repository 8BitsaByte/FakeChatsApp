package com.eL.FakeChats;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Omar Sheikh on 8/3/2017.
 */
public class FollowUsFragment extends Fragment {
    View view;
    String appPackageName;
    private static CallbackManager fbManager;

    ShareDialog shareDialog;

    LinearLayout shareOnFacebook, rateOnGooglePlay,likeFacebookPage, shareOnMessenger, shareOnWhatsApp;

    //Add Mob
    private AdView footerAd;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.followus_layout,null,false);
        appPackageName = getContext().getPackageName();
        InitializeStuff();
        SetListners();
        HandleAds();
        return view;
    }
    public void InitializeStuff(){
        fbManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        shareOnFacebook = (LinearLayout) view.findViewById(R.id.FollowUsShareOnFacebook);
        rateOnGooglePlay = (LinearLayout) view.findViewById(R.id.FollowUsRateUsOnGooglePlay);
        likeFacebookPage = (LinearLayout) view.findViewById(R.id.FollowUsLikeUsOnFacebook);
        shareOnMessenger = (LinearLayout) view.findViewById(R.id.FollowUsShareOnMessenger);
        shareOnWhatsApp = (LinearLayout) view.findViewById(R.id.FollowUsShareOnWhatsApp);
    }
    public void SetListners(){
        shareOnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareAppOnFacebook();
            }
        });
        rateOnGooglePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateOnGooglePlay();
            }
        });
        likeFacebookPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToFacebookAppPage();
            }
        });
        shareOnMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForwardThroughMessenger();
            }
        });
        shareOnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForwardThroughWhatsApp();
            }
        });
    }
    public void HandleAds(){
        footerAd = (AdView) view.findViewById(R.id.FooterAds);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        footerAd.loadAd(adRequest);
    }
    public void ShareAppOnFacebook(){
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName))
                .build();
        shareDialog.show(content);
        shareDialog.registerCallback(fbManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if(result.getPostId() == null){
                    Toast.makeText(getContext(),"Canceled successfully",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });

    }
    public void GoToFacebookAppPage(){
        PackageManager pm = getContext().getPackageManager();
        startActivity(NewFacebookIntent(pm,"https://www.facebook.com/FCFakeChats/"));
    }
    public static Intent NewFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }
    public void ForwardThroughMessenger(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.facebook.orca");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            getContext().startActivity(sendIntent);
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Facebook messenger not installed", Toast.LENGTH_LONG).show();
        }
    }
    public void ForwardThroughWhatsApp(){
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
        whatsappIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            getContext().startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(),"Whatsapp messenger not installed", Toast.LENGTH_LONG).show();
        }
    }
    public void RateOnGooglePlay(){
        Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
        }
    }
}
