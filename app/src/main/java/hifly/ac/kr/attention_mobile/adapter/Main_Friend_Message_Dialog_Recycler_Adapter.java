package hifly.ac.kr.attention_mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.adapter_item.Main_Configure_RecyclerView_Dialog_Speaker_Item;
import kr.ac.hifly.attention.adapter_item.Main_Friend_Message_Dialog_Recycler_Item;

/**
 * Created by hscom-018 on 2017-12-12.
 */


public class Main_Friend_Message_Dialog_Recycler_Adapter extends RecyclerView.Adapter<Main_Friend_Message_Dialog_Recycler_Adapter.ViewHolder> {
    private Context context;
    private List<Main_Friend_Message_Dialog_Recycler_Item> main_friend_message_dialog_recycler_items;

    public Main_Friend_Message_Dialog_Recycler_Adapter(Context context, List<Main_Friend_Message_Dialog_Recycler_Item> main_friend_message_dialog_recycler_items) {
        this.context = context;
        this.main_friend_message_dialog_recycler_items = main_friend_message_dialog_recycler_items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_friend_message_recycler_item_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position.setText(main_friend_message_dialog_recycler_items.get(position).getPosition());
        holder.name.setText(main_friend_message_dialog_recycler_items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return main_friend_message_dialog_recycler_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public TextView position;
        public TextView name;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            position = (TextView) itemView.findViewById(R.id.main_friend_message_item_position);
            name = (TextView) itemView.findViewById(R.id.main_friend_message_item_name);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.fourth_RecyclerView_item_setClick:
                    int position = getAdapterPosition();    //viewHolder 안에 onclick 을 달아줘야 각 position 에 접근가능
                    switch (position){
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            return;
                        //break;
                        default:
                            return;
                    }
                    break;
            }
        }
    }

}
