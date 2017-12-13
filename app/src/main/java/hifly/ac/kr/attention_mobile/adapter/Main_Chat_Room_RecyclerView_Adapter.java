package hifly.ac.kr.attention_mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.Interface.TestCallback;
import kr.ac.hifly.attention.adapter_item.ChatActivity_RecyclerView_Item;
import kr.ac.hifly.attention.adapter_item.Main_Chat_Room_RecyclerView_Item;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.main.MainActivity;
import kr.ac.hifly.attention.main.Main_Chat_Room_Fragment;
import kr.ac.hifly.attention.main.Main_Friend_Message_Activity;

/**
 * Created by User on 2017-10-14.
 */

public class Main_Chat_Room_RecyclerView_Adapter extends RecyclerView.Adapter<Main_Chat_Room_RecyclerView_Adapter.ViewHolder> {

    private Context context;
    private TestCallback testCallback;
    private List<Main_Chat_Room_RecyclerView_Item> main_chat_room_recyclerView_items;
    private RecyclerView second_RecyclerView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //firebase 접속
    private DatabaseReference databaseReference = firebaseDatabase.getReference();  //firebase json tree 접근
    private DatabaseReference chat = firebaseDatabase.getReference("chat");
    private DatabaseReference chatRoom = firebaseDatabase.getReference("ChatRoom");
    private Main_Chat_Room_RecyclerView_Adapter main_chat_room_recyclerView_adapter = this;

    public Main_Chat_Room_RecyclerView_Adapter(Context context, List<Main_Chat_Room_RecyclerView_Item> main_chat_room_recyclerView_items, TestCallback testCallback) {
        this.context = context;
        this.main_chat_room_recyclerView_items = main_chat_room_recyclerView_items;
        this.testCallback = testCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_chat_room_recyclerview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(main_chat_room_recyclerView_items.get(position).getName());
        holder.content.setText(main_chat_room_recyclerView_items.get(position).getLast_content());
        holder.time.setText(main_chat_room_recyclerView_items.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return main_chat_room_recyclerView_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView content;
        public TextView time;
        public LinearLayout linearLayout;
        private User user;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.second_RecyclerView_Item_Name);
            content = itemView.findViewById(R.id.second_RecyclerView_Item_Content);
            time = itemView.findViewById(R.id.second_RecyclerView_Item_Time);
            linearLayout = itemView.findViewById(R.id.main_chat_room_recyclerview_item_king);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_chat_room_recyclerview_item_king:
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent(view.getContext(), Main_Friend_Message_Activity.class);
                        /* 채팅방 클릭했을 시 */
                        Log.i("click", main_chat_room_recyclerView_items.get(position).getChatRoomName());
                        intent.putExtra("chat_room_name", main_chat_room_recyclerView_items.get(position).getChatRoomName());
                     /*   for(int i=0; i< MainActivity.users.size(); i++){
                            if(main_chat_room_recyclerView_items.get(position).getChatRoomName().contains()){

                            }
                        }*/
                        intent.putExtra("sender",main_chat_room_recyclerView_items.get(position).getName());
                        intent.putExtra("object",main_chat_room_recyclerView_items.get(position).getUser());

                        view.getContext().startActivity(intent);


                        break;
                    }
            }
        }
    }
}

