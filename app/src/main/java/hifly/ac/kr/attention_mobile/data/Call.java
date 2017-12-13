package hifly.ac.kr.attention_mobile.data;

/**
 * Created by CYSN on 2017-12-01.
 */

public class Call {
    private String caller;//uuid
    private String call_state;
    private String voice_user_ip;


    private int voice_user_port;

    public Call() {

    }

    public Call(String caller, String call_state, String voice_user_ip, int voice_user_port) {
        this.caller = caller;
        this.call_state = call_state;
        this.voice_user_ip = voice_user_ip;
        this.voice_user_port = voice_user_port;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCall_state() {
        return call_state;
    }

    public void setCall_state(String call_state) {
        this.call_state = call_state;
    }

    public String getVoice_user_ip() {
        return voice_user_ip;
    }

    public void setVoice_user_ip(String voice_user_ip) {
        this.voice_user_ip = voice_user_ip;
    }

    public int getVoice_user_port() {
        return voice_user_port;
    }

    public void setVoice_user_port(int voice_user_port) {
        this.voice_user_port = voice_user_port;
    }

}
