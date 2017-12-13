package hifly.ac.kr.attention_mobile.main;


import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import hifly.ac.kr.attention.R;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.value.Values;

public class Main_Friend_Info_Activity extends AppCompatActivity {
    private ImageView profileView;
    private TextView nameTextView;
    private TextView stateTextView;
    private FloatingActionButton callFab;
    private FloatingActionButton messageFab;

    ////////////성원 메인 프사 리스트뷰 눌렀을때 내부 프사 동기화 시키기
    private FirebaseAuth mAuth;
    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = FirebaseAuth.getInstance();
    }
    //private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private StorageReference storageRef;
    //////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_friend_info);
        nameTextView = (TextView) findViewById(R.id.main_friend_info_name_textView);
        stateTextView = (TextView) findViewById(R.id.main_friend_info_state_textView);
        profileView = (ImageView) findViewById(R.id.activity_main_friend_profile);

        callFab = (FloatingActionButton)findViewById(R.id.main_friend_info_call_fab);
        messageFab = (FloatingActionButton) findViewById(R.id.main_friend_info_message_fab);
        User user = (User)getIntent().getSerializableExtra("object");
        if(user != null){
            nameTextView.setText(user.getName());
            stateTextView.setText(user.getStateMessage());

            //////////////성원 이미지 내려받기 파이어베이스 레퍼런스 가져오기
            mAuth = FirebaseAuth.getInstance();
            storageRef = storage.getReferenceFromUrl("gs://attention-469ab.appspot.com/" + user.getUuid() + "/profile/profile.jpg");
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplication()).load(uri).apply(RequestOptions.bitmapTransform(new CircleCrop())).thumbnail(0.1f).into(profileView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            ////////////////////////////////////////////////////////////
        }
        else{
            Glide.with(this).load(R.drawable.main_friend_basic_icon)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into((profileView));
        }
    }

    public void call(View v){
        Intent intent = new Intent(getApplicationContext(), Main_Friend_Call_Activity.class);
        intent.putExtra("object",(User)getIntent().getSerializableExtra("object"));
        startActivity(intent);
    }

    public void message(View v){
        Intent intent = new Intent(getApplicationContext(), Main_Friend_Message_Activity.class);
        intent.putExtra("object",(User)getIntent().getSerializableExtra("object"));
        startActivity(intent);
    }

}
