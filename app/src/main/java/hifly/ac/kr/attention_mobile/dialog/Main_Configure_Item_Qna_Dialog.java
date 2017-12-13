package hifly.ac.kr.attention_mobile.dialog;

import android.os.Bundle;
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
import kr.ac.hifly.attention.adapter.Main_Configure_RecyclerView_Dialog_QNA_Adapter;
import kr.ac.hifly.attention.adapter_item.Main_Configure_RecyclerView_Dialog_QNA_Adapter_Item;


/**
 * Created by hscom-018 on 2017-12-07.
 */


public class Main_Configure_Item_Qna_Dialog extends AppCompatActivity {

    private RecyclerView fourth_RecyclerView_Dialog;
    private Main_Configure_RecyclerView_Dialog_QNA_Adapter main_configure_recyclerView_dialog_qna_adapter;
    private List<Main_Configure_RecyclerView_Dialog_QNA_Adapter_Item> main_configure_recyclerView_dialog_qna_adapter_items;

    private ImageButton imageButton;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //firebase 접속
    private DatabaseReference databaseReference = firebaseDatabase.getReference();  //firebase json tree 접근

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //타이틀바 없애는코드
        setContentView(R.layout.main_configure_recycler_qna_dialog);
        setFinishOnTouchOutside(false);         //다이얼로그 테마로 다이얼로그를 띄울때 다른곳을 터치할시에 꺼지는것을 방지
        fourth_RecyclerView_Dialog = (RecyclerView) findViewById(R.id.main_Configure_RecyclerView_Dialog_qna);
        imageButton = (ImageButton) findViewById(R.id.main_Configure_qna_dialog_exit_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_Configure_Item_Qna_Dialog.this.finish();
            }
        });

        setRecyclerView();

    }

    private void setRecyclerView() {

        databaseReference.child("QNA").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                main_configure_recyclerView_dialog_qna_adapter_items = new ArrayList<Main_Configure_RecyclerView_Dialog_QNA_Adapter_Item>();
                main_configure_recyclerView_dialog_qna_adapter_items.add(new Main_Configure_RecyclerView_Dialog_QNA_Adapter_Item("1", dataSnapshot.child("1").getValue(String.class)));
                main_configure_recyclerView_dialog_qna_adapter_items.add(new Main_Configure_RecyclerView_Dialog_QNA_Adapter_Item("2", dataSnapshot.child("2").getValue(String.class)));
                main_configure_recyclerView_dialog_qna_adapter_items.add(new Main_Configure_RecyclerView_Dialog_QNA_Adapter_Item("3", dataSnapshot.child("3").getValue(String.class)));
                main_configure_recyclerView_dialog_qna_adapter_items.add(new Main_Configure_RecyclerView_Dialog_QNA_Adapter_Item("4", dataSnapshot.child("4").getValue(String.class)));
                main_configure_recyclerView_dialog_qna_adapter_items.add(new Main_Configure_RecyclerView_Dialog_QNA_Adapter_Item("5", dataSnapshot.child("5").getValue(String.class)));
                main_configure_recyclerView_dialog_qna_adapter_items.add(new Main_Configure_RecyclerView_Dialog_QNA_Adapter_Item("6", dataSnapshot.child("6").getValue(String.class)));

                main_configure_recyclerView_dialog_qna_adapter = new Main_Configure_RecyclerView_Dialog_QNA_Adapter(getApplicationContext(), main_configure_recyclerView_dialog_qna_adapter_items);
                fourth_RecyclerView_Dialog.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                fourth_RecyclerView_Dialog.setAdapter(main_configure_recyclerView_dialog_qna_adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}













