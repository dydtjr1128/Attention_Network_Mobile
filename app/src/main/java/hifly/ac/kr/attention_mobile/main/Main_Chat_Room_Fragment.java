package hifly.ac.kr.attention_mobile.main;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.List;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.Interface.TestCallback;
import kr.ac.hifly.attention.adapter.Main_Chat_Room_RecyclerView_Adapter;
import kr.ac.hifly.attention.adapter_item.ChatActivity_RecyclerView_Item;
import kr.ac.hifly.attention.adapter_item.Main_Chat_Room_RecyclerView_Item;
import kr.ac.hifly.attention.value.Values;

public class Main_Chat_Room_Fragment extends Fragment implements View.OnClickListener {

    private TextView null_second;
    private Main_Chat_Room_RecyclerView_Adapter second_recyclerView_adapter;
    private List<Main_Chat_Room_RecyclerView_Item> second_recyclerView_items;
    private RecyclerView second_RecyclerView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //firebase 접속
    private DatabaseReference databaseReference = firebaseDatabase.getReference();  //firebase json tree 접근
    private DatabaseReference chat = firebaseDatabase.getReference("chat");
    private DatabaseReference chatRoom = firebaseDatabase.getReference("ChatRoom");

    private Messenger messenger;
    private ChatRoomWrapper chatRoomWrapper;
    private String myUUID;
    private String value;
    private String innerValue;
    private String value_chat_room_name;
    private StringBuilder real_chat_room_name;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private StringBuilder shared_chat_room_name;
    int shared_index = 2;

    public void setMessenger(Messenger messenger){
        this.messenger = messenger;
            Log.i("setmessage", "불림" + messenger.toString());
            Message message = new Message();
            chatRoomWrapper = new ChatRoomWrapper(second_recyclerView_adapter, second_recyclerView_items, null_second);

            message.what = Values.CHAT_ROOM;
            message.obj = chatRoomWrapper;

            try {
            messenger.send(message);

        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    private class RemoteHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_chat_room_fragment, container, false);
        second_RecyclerView = (RecyclerView) view.findViewById(R.id.chat_Room_RecyclerView);

        /* shared init */
        sharedPreferences = getActivity().getSharedPreferences(Values.shared_name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        shared_chat_room_name = new StringBuilder("");

        setRecyclerView();

        null_second = (TextView) view.findViewById(R.id.null_second_item);
        if (second_recyclerView_items.size() == 0) {
            null_second.setVisibility(view.VISIBLE);
        } else {
            null_second.setVisibility(view.INVISIBLE);
        }

        // 채팅창 방 업데이트
        if(second_recyclerView_items.isEmpty()){
            myUUID = getActivity().getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE).getString(Values.userUUID, "null");

            databaseReference.child(Values.USER).child(myUUID).child("friends").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        value = snapshot.getValue(String.class);

                        if (value != null && !value.equals("null")) {
                            innerValue = value;  // innerValue는 내가 가지고 있는 채팅방 이름

                            databaseReference.child("ChatRoom").child(innerValue).limitToLast(1).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot,String s) {

                                    Log.i("읭어디 init index", Integer.toString(shared_index));
                                    if(shared_index == 2)
                                        shared_index = 10;


                                    Log.i("1어디", shared_chat_room_name.toString());
                                    Log.i("2어디 init index", Integer.toString(shared_index));

                                    ChatActivity_RecyclerView_Item item = dataSnapshot.getValue(ChatActivity_RecyclerView_Item.class);
                                    value_chat_room_name = dataSnapshot.getRef().getParent().getKey();
                                    if (item == null || item.getSender_name() == null) {
                                        return;
                                    }

                                    int kk = 0;
                                    for(int i=0; i<second_recyclerView_items.size(); i++){

                                        real_chat_room_name = new StringBuilder("");

                                        if(second_recyclerView_items.get(i).getChatRoomName().equals(value_chat_room_name)){
                                            for(int aa = 0; aa < MainActivity.users.size(); aa++)
                                            {
                                                if(second_recyclerView_items.get(i).getChatRoomName().contains(MainActivity.users.get(aa).getUuid())){
                                                    Log.i("fragment", MainActivity.users.get(aa).getName() + Integer.toString(aa));
                                                    if(real_chat_room_name.toString().contains(MainActivity.users.get(aa).getName())) {
                                                        //if(!second_recyclerView_items.get(i).getChatRoomName().contains(MainActivity.users.get(aa).getUuid()))
                                                            continue;
                                                    }
                                                    real_chat_room_name.append(MainActivity.users.get(aa).getName() + " ");
                                                }
                                            }

                                            if(second_recyclerView_items.get(i).getName().contains(real_chat_room_name.toString())){
                                                String name_val = second_recyclerView_items.get(i).getName();
                                                if(!shared_chat_room_name.toString().contains(second_recyclerView_items.get(i).getName())) {
                                                    shared_chat_room_name.append(second_recyclerView_items.get(i).getName() + "!");
                                                    editor.putString(Values.shared_chat_room_name, shared_chat_room_name.toString());
                                                    editor.apply();
                                                }
                                                second_recyclerView_items.remove(i);
                                                second_recyclerView_items.add(0, new Main_Chat_Room_RecyclerView_Item(name_val, item.getChat_content(), item.getTime(), value_chat_room_name));
                                            }else {
                                                if(!shared_chat_room_name.toString().contains(second_recyclerView_items.get(i).getName())) {
                                                    shared_chat_room_name.append(real_chat_room_name.toString() + "!");
                                                    editor.putString(Values.shared_chat_room_name, shared_chat_room_name.toString());
                                                    editor.apply();
                                                }
                                                second_recyclerView_items.remove(i);
                                                second_recyclerView_items.add(0, new Main_Chat_Room_RecyclerView_Item(real_chat_room_name.toString(), item.getChat_content(), item.getTime(), value_chat_room_name));
                                            }
                                            kk = 1;
                                        }
                                    }
                                    if(kk == 0){
                                        Log.i("3어디", shared_chat_room_name.toString());
                                        shared_chat_room_name = new StringBuilder(sharedPreferences.getString(Values.shared_chat_room_name,""));
                                        Log.i("4어디", shared_chat_room_name.toString());
                                        if(shared_index == 10)
                                            shared_index = shared_chat_room_name.toString().split("!").length + 2;
                                        String shared_spilt_value[] = shared_chat_room_name.toString().split("!");   //여기 사실 스필트에 인덱스도 넣어서 만들어야함ㅠㅠ
                                        Log.i("5어디", Integer.toString(shared_spilt_value.length));
                                        Log.i("6어디 index", Integer.toString(shared_index));
                                        int index = (--shared_index)-2;
                                        if(index < 0)
                                            index = 0;
                                        second_recyclerView_items.add(new Main_Chat_Room_RecyclerView_Item(shared_spilt_value[index], item.getChat_content(), item.getTime(), value_chat_room_name));

                                    }

                                    if (second_recyclerView_items.size() != 0) {
                                        if(chatRoomWrapper.getNullTextView() != null)
                                            chatRoomWrapper.getNullTextView().setVisibility(chatRoomWrapper.getNullTextView().INVISIBLE);
                                    }
                                    null_second.setVisibility(null_second.INVISIBLE);
                                    second_recyclerView_adapter.notifyItemInserted(second_recyclerView_adapter.getItemCount()-1);
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setRecyclerView() {
        second_recyclerView_items = new ArrayList<Main_Chat_Room_RecyclerView_Item>();

        second_recyclerView_adapter = new Main_Chat_Room_RecyclerView_Adapter(getContext(), second_recyclerView_items, testCallback);
        second_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        second_RecyclerView.setAdapter(second_recyclerView_adapter);


    }

    @Override
    public void onClick(View view) {
        Log.i(Values.TAG, "룸 번호 클릭 리스너");
    }

    private TestCallback testCallback = new TestCallback() {
        @Override
        public void test(ChatActivity_RecyclerView_Item item, int position) {
            second_recyclerView_items.get(position).setLast_content(item.getChat_content());
            second_recyclerView_items.get(position).setTime(item.getTime());
            Log.e("time", item.getTime());
            second_recyclerView_adapter.notifyDataSetChanged();
        }
    };
}