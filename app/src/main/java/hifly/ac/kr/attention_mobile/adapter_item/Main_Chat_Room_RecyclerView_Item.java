package hifly.ac.kr.attention_mobile.adapter_item;


import kr.ac.hifly.attention.data.User;

public class Main_Chat_Room_RecyclerView_Item {
    private String name;
    private String last_content;
    private String time;
    private String chat_room_name;
    private User user;

    public Main_Chat_Room_RecyclerView_Item(String name, String last_content, String time, String chat_room_name) {
        this.name = name;
        this.last_content = last_content;
        this.time = time;
        this.chat_room_name = chat_room_name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getChatRoomName() {
        return chat_room_name;
    }

    public void setChatRoomName(String chat_room_name) {
        this.chat_room_name = chat_room_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_content() {
        return last_content;
    }

    public void setLast_content(String last_content) {
        this.last_content = last_content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
