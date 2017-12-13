package hifly.ac.kr.attention_mobile.value;

import android.media.AudioFormat;

import hifly.ac.kr.attention.R;

/**
 * Created by CYSN on 2017-11-22.
 */

// 정수 interface
public class Values {

    public static final String TAG = "KKKK";
    public static final String SERVER_IP = "223.194.156.168";

    public static final String userInfo = "userInfo";
    public static final String userUUID = "uuid";
    public static final String userName = "name";
    public static final String userTel = "tel";
    public static final String userProfile = "userProfile";

    public static final String USER = "user";
    public static final String VOICE = "voice";
    public static final String VOICE_ROOM = "voice_room";
    public static final String VOICE_ROOM_FIRST = "voice_";
    public static final String VOICE_CALLER = "voice_caller";
    public static final String VOICE_USER_IP = "voice_user_ip";
    public static final String VOICE_USER_PORT = "voice_user_port";

    public static final String VOICE_CALL_STATE = "call_state";


    public static final int CALLER_SEND_PORT = 10035;
    public static int SERVER_UDP_PORT = 10036;
    public static final int RECORDING_RATE = 22050;
    //private final int RECORDING_RATE = 8000;
    public static final int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    public static final int AUDIO_OUT_CHANNEL = AudioFormat.CHANNEL_OUT_MONO;
    //private final int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_STEREO;
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    //private final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_8BIT;

    public static final int USER_VIEW = 0;
    public static final int NORMAL_VIEW = 1;

    public static final int REFUSE_CALL = 1;
    public static final String REFUSE = "refuse";
    public static final int RECEIVE_CALL = 2;
    public static final String RECEIVE = "receive";
    public static final int END_CALL = 3;
    public static final int START_CALL = 4;
    public static final String END = "end";
    public static final String CALLING = "calling";
    public static final int CHAT_ROOM = 5;

    public static String myUUID;
    public static String myName;
    public static String myTel;
    public static String SetProfilePhoto = "profile_photo";

    public static String imageIconsName[] = {"(웃음)", "(키스)", "(쿠우)", "(흑흑)", "(우웅)", "(우이씨)"};
    public static int imageIconsDrawable[] = {R.drawable.main_friend_message_emoticon_happy, R.drawable.main_friend_message_emoticon_kiss, R.drawable.main_friend_message_emoticon_sleepy, R.drawable.main_friend_message_emoticon_sad, R.drawable.main_friend_message_emoticon_sick, R.drawable.main_friend_message_emoticon_angry};

    public static String shared_chat_room_name = "";
    public static String shared_name = "";
}

