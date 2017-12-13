package hifly.ac.kr.attention_mobile.main;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.adapter.Main_Friend_Recycler_Adapter;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.value.Values;

/**
 * Created by CYSN on 2017-11-09.
 */

public class Main_Friend_Fragment extends Fragment {
    private RecyclerView recyclerView;

    public Main_Friend_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_friend, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.main_friend_recyclerview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refresh();


    }
    public void refresh(){
        Handler handler = new MainActivity.MyHandler((MainActivity) getActivity());
        Main_Friend_Recycler_Adapter adapter = new Main_Friend_Recycler_Adapter(getContext(), handler);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE);
        adapter.addUser(null);
        adapter.addUser(new User(0, sharedPreferences.getString(Values.userName, "default"), "좋은 하루~", sharedPreferences.getString(Values.userUUID, null)));
        adapter.addUser(null);
        ArrayList<User> users = MainActivity.users;
        for (int i = 0; i < users.size(); i++) {
            adapter.addUser(users.get(i));

        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }


}
