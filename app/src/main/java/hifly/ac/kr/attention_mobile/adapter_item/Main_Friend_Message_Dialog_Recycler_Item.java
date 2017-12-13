package hifly.ac.kr.attention_mobile.adapter_item;

/**
 * Created by hscom-018 on 2017-12-12.
 */

public class Main_Friend_Message_Dialog_Recycler_Item {
    private String position;
    private String name;
    private String uuid;
    private Boolean check;

    public Main_Friend_Message_Dialog_Recycler_Item(String position, String name, String uuid, Boolean check) {
        this.position = position;
        this.name = name;
        this.uuid = uuid;
        this.check = check;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String content) {
        this.name = content;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.name = uuid;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
