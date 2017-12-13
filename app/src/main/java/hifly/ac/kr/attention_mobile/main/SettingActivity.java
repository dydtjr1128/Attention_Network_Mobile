package hifly.ac.kr.attention_mobile.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import hifly.ac.kr.attention.R;
import kr.ac.hifly.attention.value.Values;

/**
 * Created by CYSN on 2017-11-30.
 */

public class SettingActivity extends AppCompatActivity{
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }
        editText = (EditText)findViewById(R.id.setting_activity_editText);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("user");
    }
    public void setUUID(){
        SharedPreferences sharedPreferences = getSharedPreferences(Values.userInfo, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(Values.userUUID,null);
        if(uuid == null) {
            uuid = UUID.randomUUID().toString().replace("-", "");
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String myNumber = null;
            TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            try{
                myNumber = mgr.getLine1Number();
                myNumber = myNumber.replace("+82", "0").replace("-","");
            }catch(Exception e){
                e.getStackTrace();
            }
            editor.putString(Values.userName, editText.getText().toString());
            editor.putString(Values.userUUID, uuid);
            editor.putString(Values.userTel, myNumber);
            editor.apply();
            databaseReference.child(uuid).child(Values.userName).setValue(editText.getText().toString());
            databaseReference.child(uuid).child("tel").setValue(myNumber);
            databaseReference.child(uuid).child("state").setValue("만나서 반갑습니다. " + editText.getText().toString() + "입니다.");
            databaseReference.child(uuid).child(Values.userUUID).setValue(uuid);
        }
    }
    public void moveMainActivity(View v){
        setUUID();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}
