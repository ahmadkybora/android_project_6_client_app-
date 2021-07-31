package com.example.android_project_6_client_app;

import android.arch.lifecycle.ViewModelStoreOwner;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_project_5_server_app.IMyAidlInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_connect;
    private TextView txt_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        txt_status = findViewById(R.id.txt_status);
        btn_connect = findViewById(R.id.btn_connect);

        txt_status.setOnClickListener(this);
        btn_connect.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("IMyAidlInterface.aidl", "IMyAidlInterface.aidl.ServerService"));

                txt_status.setText("start ...");
                startService(intent);
                txt_status.setText("connecting ...");
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);

                break;
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            txt_status.setText("Connected ...!");
            IMyAidlInterface iMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
            try {
                Toast.makeText(MainActivity.this, iMyAidlInterface.sum(10, 12), Toast.LENGTH_SHORT).show();
                iMyAidlInterface.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            txt_status.setText("Disconnected ...!");
        }
    };
}