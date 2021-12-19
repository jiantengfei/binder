package com.example.javaclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.javaservice.IMyAidlInterface;
import com.example.javaservice.IServiceCallback;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button mBindButton;
    private Button mUnBindButton;
    private boolean mIsBind = false;
    private IMyAidlInterface mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBindButton = findViewById(R.id.bind_service);
        mBindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBindService();
            }
        });
        mUnBindButton = findViewById(R.id.unbind_service);
        mUnBindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUnBindService();
            }
        });
    }

    ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mIsBind = true;
            mService = IMyAidlInterface.Stub.asInterface(service);
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            mIsBind = false;
        }
    };

    private void doBindService() {
        if (!mIsBind) {
            Intent intent = new Intent();
            intent.setAction("com.example.javaservice.BIND_SERVICE");
            intent.setPackage("com.example.javaservice");
            bindService(intent, mConn, Context.BIND_AUTO_CREATE);
        }
    }

    private void doUnBindService() {
        if (mIsBind) {
            unbindService(mConn);
            mIsBind = false;
        }
    }

    private final IServiceCallback.Stub mCallback = new IServiceCallback.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            Log.d(TAG, "basicTypes");
        }
    };
}