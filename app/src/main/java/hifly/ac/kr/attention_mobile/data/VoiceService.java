package hifly.ac.kr.attention_mobile.data;/*

package hifly.ac.kr.attention;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;





public class VoiceService extends Service {
    private final String TAG = "KKKK";
    private final String SERVER_IP = "223.194.159.18";
    private final int SERVER_PORT = 10035;
    private int SERVER_UDP_PORT = 10036;
    private final int RECORDING_RATE = 44100;
    //private final int RECORDING_RATE = 8000;
    private final int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    //private final int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_STEREO;
    private final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    //private final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_8BIT;
    private InetSocketAddress inetSocketAddress;
    private DatagramSocket datagramSocket;
    private AudioRecord audioRecord;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private byte audioBuffer[];
    private int BUFFER_SIZE;
    private ConnectThread connectThread;

    @Override
    public void onCreate() {
        super.onCreate();
        Intent mintent = new Intent(this, VoiceTest.class);
        mintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 1, new Intent[]{mintent}, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle("통화중").setContentIntent(pendingIntent).setContentText("누르시면 이동합니다.").setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(001, builder.build());
        connectThread = new ConnectThread();
        connectThread.start();
        Log.i(TAG,"onCreateService!!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        if (connectThread != null)
            connectThread.interrupt();
        if (audioRecord != null)
            audioRecord.stop();
        if (socket != null && socket.isConnected()) {
            try {
                socket.close();
                datagramSocket.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ConnectThread extends Thread {
        public void run() {
            initSocket();
            initAudioSetting();

            try {
                dos.writeUTF("SendMsg " + BUFFER_SIZE);
                String path = dis.readUTF();
                if(path.startsWith("VoiceConnection")){

                }
                SERVER_UDP_PORT = 20003;
            } catch (Exception e) {
                e.getStackTrace();
                return;
            }

            audioRecord.startRecording();
            Log.i(TAG, "Voice Recording Start!!");
            while (true) {
                try {
                    int read = audioRecord.read(audioBuffer, 0, audioBuffer.length);
                    //Log.i(TAG, "sending");

                    datagramSocket.send(new DatagramPacket(audioBuffer, 0, read, inetSocketAddress));
                } catch (Exception e) {
                    Log.i(TAG, "Voice Error");
                    e.getStackTrace();
                    return;
                }

            }
        }
    }

    public void initSocket() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            inetSocketAddress = new InetSocketAddress(SERVER_IP, SERVER_UDP_PORT);
            datagramSocket = new DatagramSocket();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void initAudioSetting() {
        BUFFER_SIZE = AudioRecord.getMinBufferSize(RECORDING_RATE, AUDIO_CHANNEL, AUDIO_FORMAT);
        audioBuffer = new byte[BUFFER_SIZE];

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDING_RATE, AUDIO_CHANNEL, AUDIO_FORMAT, BUFFER_SIZE);
    }
}

*/
