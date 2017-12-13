package hifly.ac.kr.attention_mobile.adapter_item;



public class ChatActivity_RecyclerView_Item {
    private String sender_name;
    private String chat_content;
    private String time;
    private int itemViewType;
    private String sender_uuid;


    //빈 생성자 파이어베이스에 데이터 넣기 전에 꼭 필요!
    public ChatActivity_RecyclerView_Item() {

    }
    
    public ChatActivity_RecyclerView_Item(String sender_name, String chat_content, String time, int itemViewType, String sender_uuid) {
        this.sender_name = sender_name;
        this.chat_content = chat_content;
        this.time = time;
        this.itemViewType = itemViewType;
        this.sender_uuid = sender_uuid;

    }


    public String getSender_Uuid() {
        return sender_uuid;
    }

    public void setSender_Uuid(String sender_uuid) {
        this.sender_uuid = sender_uuid;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String chat_name) {
        this.sender_name = chat_name;
    }

    public String getChat_content() {
        return chat_content;
    }

    public void setChat_content(String chat_content) {
        this.chat_content = chat_content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getItemViewType() {
        return itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }


}
