package hifly.ac.kr.attention_mobile.data;

import java.io.Serializable;
import java.util.ArrayList;

import hifly.ac.kr.attention.R;



public class User implements Serializable {

    private int icon;
    private String uuid;//유저간 고유 ID
    private String name;



    private String tel;
    private String stateMessage;
    private ArrayList<String> friends;

    public User(int icon, String name, String stateMessage) {
        if (icon == 0)
            this.icon = R.drawable.main_friend_basic_icon;
        else
            this.icon = icon;
        this.name = name;
        this.stateMessage = stateMessage;
        friends = new ArrayList<>();
    }
    public User(int icon, String name, String stateMessage, String uuid) {
        if (icon == 0)
            this.icon = R.drawable.main_friend_basic_icon;
        else
            this.icon = icon;
        this.uuid = uuid;
        this.name = name;
        this.stateMessage = stateMessage;
        friends = new ArrayList<>();
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }
}
