package hifly.ac.kr.attention_mobile.main;

import android.widget.TextView;

import java.util.List;

import kr.ac.hifly.attention.adapter.Main_Chat_Room_RecyclerView_Adapter;
import kr.ac.hifly.attention.adapter_item.Main_Chat_Room_RecyclerView_Item;

/**
 * Created by hscom-018 on 2017-12-07.
 */

public class ChatRoomWrapper {
    private Main_Chat_Room_RecyclerView_Adapter second_recyclerView_adapter;
    private List<Main_Chat_Room_RecyclerView_Item> second_recyclerView_items;
    private TextView null_second;

    public ChatRoomWrapper(Main_Chat_Room_RecyclerView_Adapter second_recyclerView_adapter, List<Main_Chat_Room_RecyclerView_Item> second_recyclerView_items, TextView null_second) {
        this.second_recyclerView_adapter = second_recyclerView_adapter;
        this.second_recyclerView_items = second_recyclerView_items;
        this.null_second = null_second;
    }

    public Main_Chat_Room_RecyclerView_Adapter getAdapter(){
        return  second_recyclerView_adapter;
    }

    public List<Main_Chat_Room_RecyclerView_Item> getItems(){
        return second_recyclerView_items;
    }

    public TextView getNullTextView(){
        return null_second;
    }
}
