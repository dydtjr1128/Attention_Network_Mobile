package hifly.ac.kr.attention_mobile.voiceCore;

import android.content.SyncStatusObserver;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

import kr.ac.hifly.attention.value.Values;


public class Call_Receive_Thread extends Thread{
    private String connect_IP;
    private int connect_PORT;
    private InetSocketAddress inetSocketAddress;
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private AudioTrack mAudioTrack;
    private byte audioBuffer[];
    private int BUFFER_SIZE;

    public Call_Receive_Thread(String userIP,int userPort){
        Log.i(Values.TAG,userIP + " " + userPort + "@@@RECEIVE");
        this.connect_IP = userIP;
        this.connect_PORT = userPort;
    }
    public void run(){
        try {
            datagramSocket = new DatagramSocket(connect_PORT);
            initAudioSetting();
            mAudioTrack.play();
            Log.i(Values.TAG,"Listen");
        }catch (Exception e){
            e.getStackTrace();
        }
        while(true) {
            try {
                datagramSocket.receive(new DatagramPacket(audioBuffer, 0, BUFFER_SIZE));
                Log.i(Values.TAG,"Listen" + audioBuffer[0] + " " + audioBuffer[1] + " " + audioBuffer[2]);
                mAudioTrack.write(audioBuffer,0,audioBuffer.length);

                //int read = audioRecord.read(audioBuffer, 0, audioBuffer.length);


            } catch (Exception e) {
                Log.i(Values.TAG, "Voice Error in Call_Receive_Thread");
                e.getStackTrace();
                return;
            }
        }
    }

    public void initAudioSetting() {
        BUFFER_SIZE = AudioRecord.getMinBufferSize(Values.RECORDING_RATE, Values.AUDIO_CHANNEL, Values.AUDIO_FORMAT);
        audioBuffer = new byte[BUFFER_SIZE];
        mAudioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL,Values.RECORDING_RATE ,Values.AUDIO_OUT_CHANNEL, Values.AUDIO_FORMAT, BUFFER_SIZE, AudioTrack.MODE_STREAM);

    }

}
