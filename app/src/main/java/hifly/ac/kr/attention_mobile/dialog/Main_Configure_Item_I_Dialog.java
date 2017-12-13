package hifly.ac.kr.attention_mobile.dialog;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import hifly.ac.kr.attention.R;


/**
 * Created by hscom-018 on 2017-12-07.
 */

public class Main_Configure_Item_I_Dialog extends AppCompatActivity {

    private ImageButton imageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //타이틀바 없애는코드

        setContentView(R.layout.main_configure_recycler_item_i_dialog);

        setFinishOnTouchOutside(false); //다이얼로그 테마로 다이얼로그를 띄울때 다른곳을 터치할시에 꺼지는것을 방지

        imageButton = (ImageButton)findViewById(R.id.main_configure_i_dialog_exit_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_Configure_Item_I_Dialog.this.finish();
            }
        });
    }
}


