package hifly.ac.kr.attention_mobile.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.adapter.Main_Configure_RecyclerView_Dialog_Speaker_Adapter;
import kr.ac.hifly.attention.adapter.Main_Friend_Message_Dialog_Recycler_Adapter;
import kr.ac.hifly.attention.adapter_item.Main_Configure_RecyclerView_Dialog_Speaker_Item;
import kr.ac.hifly.attention.adapter_item.Main_Friend_Message_Dialog_Recycler_Item;
import kr.ac.hifly.attention.main.MainActivity;

/**
 * Created by hscom-018 on 2017-12-12.
 */

public class Main_Friend_Message_Dialog extends AppCompatActivity {

    private RecyclerView main_Friend_Message_RecyclerView_Dialog;
    private Main_Friend_Message_Dialog_Recycler_Adapter main_friend_message_dialog_recycler_adapter;
    private List<Main_Friend_Message_Dialog_Recycler_Item> main_friend_message_dialog_recycler_items;

    private ImageButton imageButton;
    private String myUuid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //타이틀바 없애는코드
        setContentView(R.layout.main_friend_message_recycler_dialog);
        setFinishOnTouchOutside(false);         //다이얼로그 테마로 다이얼로그를 띄울때 다른곳을 터치할시에 꺼지는것을 방지
        main_Friend_Message_RecyclerView_Dialog = (RecyclerView) findViewById(R.id.main_friend_message_recycler);
        imageButton = (ImageButton) findViewById(R.id.main_friend_message_dialog_exit_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_Friend_Message_Dialog.this.finish();
            }
        });
        myUuid = getIntent().getStringExtra("myUuid");

        setRecyclerView();

    }

    private void setRecyclerView() {
        main_friend_message_dialog_recycler_items = new ArrayList<Main_Friend_Message_Dialog_Recycler_Item>();

        for(int i=0; i<MainActivity.users.size(); i++) {
            if(myUuid.equals(MainActivity.users.get(i).getUuid()))
                continue;
            main_friend_message_dialog_recycler_items.add(new Main_Friend_Message_Dialog_Recycler_Item(Integer.toString(i+1), MainActivity.users.get(i).getName(),
                    MainActivity.users.get(i).getUuid(),false));
        }

        main_friend_message_dialog_recycler_adapter = new Main_Friend_Message_Dialog_Recycler_Adapter(getApplicationContext(), main_friend_message_dialog_recycler_items);
        main_Friend_Message_RecyclerView_Dialog.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        main_Friend_Message_RecyclerView_Dialog.setAdapter(main_friend_message_dialog_recycler_adapter);
    }
}













