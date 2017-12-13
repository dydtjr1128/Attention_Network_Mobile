package hifly.ac.kr.attention_mobile.adapter_item;

/**
 * Created by hscom-018 on 2017-11-30.
 */

public class Main_Configuration_RecyclerView_Item {
    private String name;
    private int draw;
    public Main_Configuration_RecyclerView_Item(String name, int draw) {
        this.name = name;
        this.draw =  draw;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}