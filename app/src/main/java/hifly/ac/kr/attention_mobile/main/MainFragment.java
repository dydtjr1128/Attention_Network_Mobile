package hifly.ac.kr.attention_mobile.main;

/**
 * Created by hscom-018 on 2017-11-22.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import java.util.Vector;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.value.Values;


public class MainFragment extends Fragment {

    private Animation animation;
    private Animation.AnimationListener animationListener;
    public MainFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_intro, container, false);
        initAnimation();

        animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext() ,R.anim.alpha);
        animation.setAnimationListener(animationListener);

        view.startAnimation(animation);
        return view;
    }
    public void getUUID(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(Values.userUUID,null);
        if(uuid == null){
            Intent intent = new Intent(getActivity().getApplicationContext(), SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            getActivity().finish();
        }
        else{
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
            getActivity().finish();
        }
    }
    public void initAnimation() {
        animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getUUID();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

    }
}

