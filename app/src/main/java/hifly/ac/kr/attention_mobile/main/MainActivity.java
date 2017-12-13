package hifly.ac.kr.attention_mobile.main;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.data.User;
import kr.ac.hifly.attention.messageCore.MessageService;
import kr.ac.hifly.attention.value.Values;

public class MainActivity extends AppCompatActivity {

    private final MyHandler mHandler = new MyHandler(this);
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private static Main_Friend_Fragment mainFragment = new Main_Friend_Fragment();
    private static Main_Chat_Room_Fragment mainFragment2 = new Main_Chat_Room_Fragment();
    private static Main_Configuration_Fragment mainFragment3 = new Main_Configuration_Fragment();
    private Intent serviceIntent;   //@@
    private Messenger messenger;    //@@
    public static ArrayList<User> users = new ArrayList<>();
    private CardView cardView;
    private int count;
    private int telSize;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private File path;

    private ServiceConnection connection = new ServiceConnection() {            //@@
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            messenger = new Messenger(service);
            if (messenger != null) {
                mainFragment2.setMessenger(messenger);
                Message message = new Message();
                message.what = 0;
                message.obj = new Messenger(new RemoteHandler());
                try {
                    messenger.send(message);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messenger = null;
        }
    };

    private class RemoteHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "service Message!", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

    public void testSend() {
        Message message = new Message();
        message.what = 1;
        message.obj = "ff";

        //messenger.send(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Values.myUUID = getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE).getString(Values.userUUID, null);
        Values.myName = getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE).getString(Values.userName, "null");
        Values.myTel = getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE).getString(Values.userTel, "01037125066");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK}, 1001);
        }

        initViewPager();
        getDataInstance();
        serviceIntent = new Intent(this, MessageService.class);

        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        startService(serviceIntent);
    }

    public void getDataInstance() {
        ArrayList arrayList = null;
        try {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            Log.i(Values.TAG, path.getAbsolutePath() + " " + path + " " + path.getPath() + " " + getFilesDir().getAbsolutePath() + " !!!!!!!!!!!!!!");
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path + "/attentionTel.dat"));
          /*  objectOutputStream.writeObject(new User(0,"이거는 들어갈걸","상태메세지야"));

            User user = (User)objectInputStream.readObject();
            Log.i(Values.TAG,user.getName());*/
            arrayList = (ArrayList) objectInputStream.readObject();

            if (arrayList == null) {

            } else {
                users = (ArrayList<User>) arrayList;

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Tel is Empty!", Toast.LENGTH_SHORT).show();

            //getSynchronizePhone(null);
      /*      try {
                objectOutputStream.writeObject(users);
            }catch (Exception e1){
                e1.getStackTrace();
            }*/
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class PageAdapter extends FragmentPagerAdapter {
        public PageAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mainFragment;
                case 1:
                    return mainFragment2;
                case 2:
                    return mainFragment3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.main_frame_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        cardView = (CardView) findViewById(R.id.activity_main_friend_progressBar);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.people);
        tabLayout.getTabAt(1).setIcon(R.drawable.sungwon_message);
        tabLayout.getTabAt(2).setIcon(R.drawable.info);

        int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Black);
        int unSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Gray);

        tabLayout.getTabAt(0).getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Black);
                tab.getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.color_Gray);
                tab.getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    /*  @Override
      public boolean onOptionsItemSelected(MenuItem item) {
          switch (item.getItemId()) {
              case R.id.action_subactivity:
                  startActivity(new Intent(this, Main_Configuration_Fragment.class));
                  return true;
              case R.id.toolbar_item_configure:
                  getSynchronizePhone(((ImageButton)findViewById(R.id.toolbar_item_configure)));
                  return true;
              default:
                  return super.onOptionsItemSelected(item);
          }
      }*/
    public void synchronizeAboutTel(View view){
        getSynchronizePhone(((ImageButton)findViewById(R.id.toolbar_item_configure)));
    }


    // 핸들러 객체 만들기  @@
    public static class MyHandler extends Handler {
        public static final int CHANGE_FRIEND_INFO = 1;
        public static final int PROGRESS_END = 3;
        public static final int SAY_WORD = 2;
        private final WeakReference<MainActivity> mWeakActivity;


        public MyHandler(MainActivity activtiy) {
            mWeakActivity = new WeakReference<MainActivity>(activtiy);

        }

        @Override
        public void handleMessage(Message msg) {
            int message = msg.getData().getInt("message");
            switch (message) {
                case CHANGE_FRIEND_INFO:
                    Intent intent = new Intent(mWeakActivity.get().getApplicationContext(), Main_Friend_Info_Activity.class);
                    intent.putExtra("object", msg.getData().getSerializable("object"));
                    mWeakActivity.get().startActivity(intent);
                    break;
                case PROGRESS_END:
                    break;
                case SAY_WORD:
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public void getSynchronizePhone(final ImageButton button) {
        count = telSize = 0;
        button.setClickable(false);
        users.clear();
        cardView.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                String[] arrProjection = {
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME
                };

                String[] arrPhoneProjection = {
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };

                // get user list
                Cursor clsCursor = getApplicationContext().getContentResolver().query(
                        ContactsContract.Contacts.CONTENT_URI,
                        arrProjection,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1",
                        null, null
                );

                telSize = clsCursor.getCount();

                while (clsCursor.moveToNext()) {
                    String telid = null;
                    String name = null;
                    String tel = null;
                    telid = clsCursor.getString(0);
                    name = clsCursor.getString(1);

                    String strContactId = clsCursor.getString(0);
                    Cursor clsPhoneCursor = getApplicationContext().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            arrPhoneProjection,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + strContactId,
                            null, null
                    );
                    while (clsPhoneCursor.moveToNext()) {
                        tel = clsPhoneCursor.getString(0);
                    }
                    tel = tel.replace("-", "");
                   /* Log.d("Unity", "연락처 사용자 ID : " + telid);
                    Log.d("Unity", "연락처 사용자 이름 : " + name);
                    Log.d("Unity", "연락처 사용자 폰번호 : " + tel);*/
                    count++;
                    if (count >= telSize) {

                    }


                    databaseReference.child(Values.USER).orderByChild(Values.userTel).equalTo(tel).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String state = dataSnapshot.child("state").getValue(String.class);
                            String mtel = dataSnapshot.child("tel").getValue(String.class);
                            String uuid = dataSnapshot.getKey();
                            //Log.i(Values.TAG, name + " " + state + " " + uuid);
                            //                           databaseReference.child("user").child(myUUID).child("friends").child(uuid).setValue("null");
                            if (!uuid.equals(Values.myUUID) && !Values.myTel.equals(mtel)) {
                                users.add(new User(0, name, state, uuid));
                                mainFragment.refresh();
                            }
                            Log.i(Values.TAG, telSize + " " + count + "!!!!!!!!!!!!!!!!!!");
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    clsPhoneCursor.close();
                }
                clsCursor.close();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cardView.setVisibility(View.GONE);
                        try {
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path + "/attentionTel.dat"));
                            objectOutputStream.writeObject(users);
                            objectOutputStream.close();
                            Toast.makeText(getApplicationContext(), "object write success!!", Toast.LENGTH_SHORT).show();
                            button.setClickable(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 100);

                /*
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("message", 3);
                message.setData(bundle);
                mHandler.sendMessage(message);*/


            }
        }).start();
    }
}