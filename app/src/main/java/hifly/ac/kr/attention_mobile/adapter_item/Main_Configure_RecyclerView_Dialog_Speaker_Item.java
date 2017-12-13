package hifly.ac.kr.attention_mobile.adapter_item;

/**
 * Created by hscom-018 on 2017-12-07.
 */


public class Main_Configure_RecyclerView_Dialog_Speaker_Item {
    private String position;
    private String content;

    public Main_Configure_RecyclerView_Dialog_Speaker_Item(String position, String content) {
        this.position = position;
        this.content = content;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
