package hifly.ac.kr.attention_mobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.value.Values;

/**
 * Created by CYSN on 2017-11-09.
 */

public class Main_Friend_Recycler_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /////////성원 이미지 내려받기
    private FirebaseAuth mAuth;
    /////////////////////////
    private Typeface typeface;
    private ArrayList<User> arrayList;
    private Context context;
    private Handler handler;

    //////////////성원 이미지 내려받기 파이어베이스 레퍼런스 가져오기
    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = FirebaseAuth.getInstance();
    }
//
//    private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private StorageReference storageRef;

    public Main_Friend_Recycler_Adapter(Context context, Handler handler){
        this.context = context;
        this.handler = handler;
        typeface = ResourcesCompat.getFont(context, R.font.bm_jua);
        arrayList = new ArrayList<User>();
    }

    public void addUser(User user){
        arrayList.add(user);
        notifyItemInserted(arrayList.size()-1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType){
            case Values.USER_VIEW :
                view = inflater.inflate(R.layout.fragment_main_friend_item, parent, false);
                Main_Friend_ViewHolder viewHolder = new Main_Friend_ViewHolder(view);
                return viewHolder;

            case Values.NORMAL_VIEW:
                view = inflater.inflate(R.layout.fragment_main_friend_normal_item, parent, false);
                NormalViewHolder normalViewHolder = new NormalViewHolder(view);
                return normalViewHolder;

        }
        return null;
    }

    public void use_firebase_change_image(final User user, final Main_Friend_ViewHolder holder ){
        storageRef = storage.getReferenceFromUrl("gs://attention-469ab.appspot.com/" + user.getUuid() + "/profile/profile.jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {

                Log.i(Values.TAG, downloadUrl + " " + downloadUrl.getEncodedPath() + " #######################33");
                Glide.with(context).load(downloadUrl).apply(RequestOptions.bitmapTransform(new CircleCrop())).thumbnail(0.1f).into(holder.imageView);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.imageView.setImageResource(user.getIcon());
            }
        });
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {
        if(viewholder instanceof Main_Friend_ViewHolder) {
            final Main_Friend_ViewHolder holder = (Main_Friend_ViewHolder)viewholder;
            final User user = arrayList.get(position);
            //holder.imageView.setImageResource(user.getIcon());
            ////////////////////성원 이미지 내려받기
//
//                storageRef = storage.getReferenceFromUrl("gs://attention-469ab.appspot.com/" + user.getUuid() + "/profile/profile.jpg");
//                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri downloadUrl) {
//
//                        Log.i(Values.TAG, downloadUrl + " " + downloadUrl.getEncodedPath() + " #######################33");
//                        Glide.with(context).load(downloadUrl).apply(RequestOptions.bitmapTransform(new CircleCrop())).thumbnail(0.1f).into(holder.imageView);
//                    }
//
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        holder.imageView.setImageResource(user.getIcon());
//                    }
//                });
            use_firebase_change_image(user, holder);
            //////////////////////////////////////////이미지 내려받기 end;
            holder.nameTextView.setText(user.getName());
            holder.nameTextView.setTypeface(typeface);
            holder.stateTextView.setText(user.getStateMessage());
            holder.stateTextView.setTypeface(typeface);
        }
        else if(viewholder instanceof NormalViewHolder){
            NormalViewHolder normalViewHolder = (NormalViewHolder)viewholder;
            if(position == 0)
                normalViewHolder.textView.setText("내 정보");
            else if(position == 2)
              normalViewHolder.textView.setText("친구");

            normalViewHolder.textView.setTypeface(typeface);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position) != null ? Values.USER_VIEW : Values.NORMAL_VIEW;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Main_Friend_ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nameTextView;
        TextView stateTextView;
        LinearLayout linearLayout;

        public Main_Friend_ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.main_friend_user_icon_imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.main_friend_user_name_textView);
            stateTextView = (TextView) itemView.findViewById(R.id.main_friend_user_state_textView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.fragment_main_friend_linear_layout);


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user = arrayList.get(getLayoutPosition());
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("message",1);
                    bundle.putSerializable("object",user);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            });
        }
    }

    class NormalViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public NormalViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.fragment_main_friend_normal_text);
        }
    }
}
