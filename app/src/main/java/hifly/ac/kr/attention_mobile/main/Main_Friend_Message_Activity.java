package hifly.ac.kr.attention_mobile.main;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.adapter.ChatActivity_RecyclerView_Adapter;
import kr.ac.hifly.attention.adapter_item.ChatActivity_RecyclerView_Item;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.dialog.Main_Friend_Message_Dialog;
import kr.ac.hifly.attention.value.Values;

public class Main_Friend_Message_Activity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private Button button;
    private RecyclerView chatActivity_recyclerView;
    private ChatActivity_RecyclerView_Adapter chatActivity_recyclerView_adapter;
    private List<ChatActivity_RecyclerView_Item> chatActivity_recyclerView_items;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //firebase 접속
    private DatabaseReference databaseReference = firebaseDatabase.getReference();  //firebase json tree 접근

    private User user;
    private String chat_room = "";
    private String myUuid = "";
    private String yourUuid = "";
    private int chat_room_flag  = 0;
    private String senderName = "default";

    private boolean send_user_or_char_room_name; //true,  false
    private HashMap<Integer, String> imoticonContents;
    private int imageIcons[] = {R.id.main_friend_message_emoticon1, R.id.main_friend_message_emoticon2, R.id.main_friend_message_emoticon3, R.id.main_friend_message_emoticon4, R.id.main_friend_message_emoticon5, R.id.main_friend_message_emoticon6};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_friend_message);
        initImoticon();
        if(getIntent().getStringExtra("chat_room_name") != null){
            chat_room = getIntent().getStringExtra("chat_room_name");
            send_user_or_char_room_name = false;
        }

        user = (User) getIntent().getSerializableExtra("object");
        if (user == null) {
            Log.i(Values.TAG, "user create error");
        }else {
            yourUuid = user.getUuid();
            send_user_or_char_room_name = true;
        }
        myUuid = getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE).getString(Values.userUUID, "null");
        senderName = getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE).getString(Values.userName, "null");
        for(int i=0; i<imageIcons.length; i++){
            ((ImageView)findViewById(imageIcons[i])).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat setformat = new SimpleDateFormat("HH:mm");
                    String time = setformat.format(date);
                    ChatActivity_RecyclerView_Item item = new ChatActivity_RecyclerView_Item(senderName, imoticonContents.get(view.getId()), time, 1, myUuid);
                    databaseReference.child("ChatRoom").child(chat_room).push().setValue(item);
                }
            });
        }

        init();
    }
    public void initImoticon(){
        imoticonContents = new HashMap<Integer, String>();
        for(int i=0; i<imageIcons.length; i++){
            imoticonContents.put(imageIcons[i],Values.imageIconsName[i]);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        chatActivity_recyclerView_items.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                LinearLayout chat_activity_RecyclerView0 = (LinearLayout)findViewById(R.id.main_chat_activity_RecyclerView);
                LinearLayout.LayoutParams params0 = (LinearLayout.LayoutParams) chat_activity_RecyclerView0.getLayoutParams();

                LinearLayout main_friend_message_new_place0 = (LinearLayout)findViewById(R.id.main_friend_message_new_place);
                main_friend_message_new_place0.setVisibility(view.GONE);

                LinearLayout main_friend_message_new_place_emoticon0 = (LinearLayout)findViewById(R.id.main_friend_message_new_place_emoticon);
                main_friend_message_new_place_emoticon0.setVisibility(view.GONE);

                // 7, 9
                params0.weight = 9;
                chat_activity_RecyclerView0.setLayoutParams(params0);
                if(editText.getText().toString().equals(""))
                    return;
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat setformat = new SimpleDateFormat("HH:mm");
                String time = setformat.format(date);

                /*    내가 보냈을 때 flag  1,   chat_room 방 번호  */
                ChatActivity_RecyclerView_Item item = new ChatActivity_RecyclerView_Item(senderName, editText.getText().toString(), time, 1, myUuid);
                databaseReference.child("ChatRoom").child(chat_room).push().setValue(item);
                editText.setText("");

                /* Child Listener 달기 ( 처음 채팅을 하는 경우에는 여기서!,  한번만 달아야 함!)  */
                if(chat_room_flag == 0){
                    chat_room_flag = 1;
                    databaseReference.child("ChatRoom").child(chat_room).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            ChatActivity_RecyclerView_Item item = dataSnapshot.getValue(ChatActivity_RecyclerView_Item.class);
                            if(item.getSender_name().equals(senderName) && item.getSender_Uuid().equals(myUuid)) {
                                item.setItemViewType(1);
                            }
                            else {
                                item.setItemViewType(0);
                            }
                            chatActivity_recyclerView_items.add(item);
                            chatActivity_recyclerView_adapter.notifyItemInserted(chatActivity_recyclerView_adapter.getItemCount()-1);
                            chatActivity_recyclerView.scrollToPosition(chatActivity_recyclerView_adapter.getItemCount()-1);

                            //chatActivity_recyclerView.scrollToPosition(chatActivity_recyclerView.getAdapter().getItemCount() - 1); //@@
                            Log.i(Values.TAG, "message Add");
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

                /* 스크롤 맨 밑으로 내리기, 어댑터 갱신하기 */
                chatActivity_recyclerView_adapter.notifyDataSetChanged();
                //chatActivity_recyclerView_adapter.notifyItemInserted(chatActivity_recyclerView_items.size() - 1);
                chatActivity_recyclerView.getLayoutManager().scrollToPosition(chatActivity_recyclerView.getAdapter().getItemCount() - 1);  //@@
                break;

            case R.id.main_friend_message_plus_btn:
                LinearLayout chat_activity_RecyclerView = (LinearLayout)findViewById(R.id.main_chat_activity_RecyclerView);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chat_activity_RecyclerView.getLayoutParams();

                LinearLayout main_friend_message_new_place = (LinearLayout)findViewById(R.id.main_friend_message_new_place);
                main_friend_message_new_place.setVisibility(view.VISIBLE);

                LinearLayout main_friend_message_new_place_emoticon = (LinearLayout)findViewById(R.id.main_friend_message_new_place_emoticon);
                main_friend_message_new_place_emoticon.setVisibility(view.GONE);

                // 7, 9
                params.weight = 7;
                chat_activity_RecyclerView.setLayoutParams(params);
                break;
            case R.id.myItemKing:
            case R.id.yourItemKing:
                LinearLayout chat_activity_RecyclerView1 = (LinearLayout)findViewById(R.id.main_chat_activity_RecyclerView);
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) chat_activity_RecyclerView1.getLayoutParams();

                LinearLayout main_friend_message_new_place1 = (LinearLayout)findViewById(R.id.main_friend_message_new_place);
                main_friend_message_new_place1.setVisibility(view.GONE);

                LinearLayout main_friend_message_new_place_emoticon1 = (LinearLayout)findViewById(R.id.main_friend_message_new_place_emoticon);
                main_friend_message_new_place_emoticon1.setVisibility(view.GONE);

                // 7, 9
                params1.weight = 9;
                chat_activity_RecyclerView1.setLayoutParams(params1);
                break;
            case R.id.main_friend_message_emoticon:
                LinearLayout chat_activity_RecyclerView2 = (LinearLayout)findViewById(R.id.main_chat_activity_RecyclerView);
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) chat_activity_RecyclerView2.getLayoutParams();

                LinearLayout main_friend_message_new_place2 = (LinearLayout)findViewById(R.id.main_friend_message_new_place);
                main_friend_message_new_place2.setVisibility(view.GONE);

                LinearLayout main_friend_message_new_place_emoticon2 = (LinearLayout)findViewById(R.id.main_friend_message_new_place_emoticon);
                main_friend_message_new_place_emoticon2.setVisibility(view.VISIBLE);
                // 7, 9
                params2.weight = 7;
                chat_activity_RecyclerView2.setLayoutParams(params2);
                break;
            case R.id.main_friend_message_invite_friend:
                Intent intent = new Intent(view.getContext(), Main_Friend_Message_Dialog.class);
                intent.putExtra("myUuid",myUuid);
                startActivity(intent);
                break;
            case R.id.main_friend_message_voice_chat:
                Intent mintent = new Intent(getApplicationContext(), Main_Friend_Call_Activity.class);
                mintent.putExtra("object",(User)getIntent().getSerializableExtra("object"));
                startActivity(mintent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        LinearLayout chat_activity_RecyclerView = (LinearLayout)findViewById(R.id.main_chat_activity_RecyclerView);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chat_activity_RecyclerView.getLayoutParams();
        LinearLayout main_friend_message_new_place = (LinearLayout)findViewById(R.id.main_friend_message_new_place);
        LinearLayout main_friend_message_new_place_emoticon = (LinearLayout)findViewById(R.id.main_friend_message_new_place_emoticon);

        if(main_friend_message_new_place.getVisibility() == main_friend_message_new_place.VISIBLE){
            main_friend_message_new_place.setVisibility(main_friend_message_new_place.GONE);
            params.weight = 9;
            chat_activity_RecyclerView.setLayoutParams(params);
        }
        else if(main_friend_message_new_place_emoticon.getVisibility() == main_friend_message_new_place_emoticon.VISIBLE){
            main_friend_message_new_place_emoticon.setVisibility(main_friend_message_new_place_emoticon.GONE);
            params.weight = 9;
            chat_activity_RecyclerView.setLayoutParams(params);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }

    private void init() {
        chatActivity_recyclerView_items = new ArrayList<ChatActivity_RecyclerView_Item>();
        chatActivity_recyclerView = (RecyclerView) findViewById(R.id.chat_activity_RecyclerView);
        chatActivity_recyclerView_adapter = new ChatActivity_RecyclerView_Adapter(getApplicationContext(), chatActivity_recyclerView_items);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        //linearLayoutManager.setStackFromEnd(true);
        chatActivity_recyclerView.setLayoutManager(linearLayoutManager);
        chatActivity_recyclerView.setAdapter(chatActivity_recyclerView_adapter);

        //스크롤
        chatActivity_recyclerView.scrollToPosition(chatActivity_recyclerView_adapter.getItemCount() - 1);


        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(this);

        if(send_user_or_char_room_name == true) {
        /* user 방 번호 세팅   여기서부터 생명주기를 생각하면서 inner class로 작성한 것이 중요  */
            databaseReference.child("user").child(myUuid).child("friends").child(yourUuid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    chat_room = dataSnapshot.getValue(String.class);

                    if (chat_room == null) {
                        chat_room = "chat_" + myUuid + "  " + yourUuid;
                    }
                    databaseReference.child("user").child(myUuid).child("friends").child(yourUuid).setValue(chat_room);
                    databaseReference.child("user").child(yourUuid).child("friends").child(myUuid).setValue(chat_room);

                /* ChatRoom에 이미 방을 연적이 있는지 (한번이라도 채팅을 한적이 있는지 체크)  */
                    databaseReference.child("ChatRoom").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                        /* Child Listener 달기 (Child 가 있다면 )*/
                            if (dataSnapshot.hasChild(chat_room)) {
                                chat_room_flag = 1;
                                databaseReference.child("ChatRoom").child(chat_room).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        ChatActivity_RecyclerView_Item item = dataSnapshot.getValue(ChatActivity_RecyclerView_Item.class);
                                        if (item.getSender_name().equals(senderName) && item.getSender_Uuid().equals(myUuid)) {
                                            item.setItemViewType(1);
                                        } else {
                                            item.setItemViewType(0);
                                        }
                                        chatActivity_recyclerView_items.add(item);
                                        chatActivity_recyclerView_adapter.notifyItemInserted(chatActivity_recyclerView_adapter.getItemCount()-1);  // @@
                                        chatActivity_recyclerView.scrollToPosition(chatActivity_recyclerView_adapter.getItemCount()-1);
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

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        else {    /* ChatRoom에 이미 방을 연적이 있는지 (한번이라도 채팅을 한적이 있는지 체크)  */
            databaseReference.child("ChatRoom").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        /* Child Listener 달기 (Child 가 있다면 )*/
                    if (dataSnapshot.hasChild(chat_room)) {
                        chat_room_flag = 1;
                        databaseReference.child("ChatRoom").child(chat_room).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                ChatActivity_RecyclerView_Item item = dataSnapshot.getValue(ChatActivity_RecyclerView_Item.class);
                                if (item.getSender_name().equals(senderName) && item.getSender_Uuid().equals(myUuid)) {
                                    Log.i("111111111111", item.getSender_name() + "   " + senderName);
                                    Log.i("2222222222222", item.getSender_Uuid() + "   " + myUuid);
                                    item.setItemViewType(1);
                                } else {
                                    item.setItemViewType(0);
                                }
                                chatActivity_recyclerView_items.add(item);
                                chatActivity_recyclerView_adapter.notifyItemInserted(chatActivity_recyclerView_adapter.getItemCount()-1);  // @@
                                chatActivity_recyclerView.scrollToPosition(chatActivity_recyclerView_adapter.getItemCount()-1);
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

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}