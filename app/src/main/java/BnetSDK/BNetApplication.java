package BnetSDK;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import Service.BnetService;
import model.BnetServiceJoinParams;
import network.b.bnet.service.BnetAidlInterface;


public class BNetApplication extends Application {
    private Context context;
    private static final String TAG = "BNetApplication";
    private static BNetApplication bNetApplication = null;

    public static boolean isChecked = false;
    private static ServiceConnection serviceConnection;
    public static boolean serviceBind = false;

    public BnetAidlInterface getBnetAidlInterface() {
        return bnetAidlInterface;
    }

    private BnetAidlInterface bnetAidlInterface;
    private Intent mIntentConnectorService;
    private BnetServiceJoinParams bnetServiceJoinParams;

    @Override
    public void onCreate() {
        super.onCreate();
        bNetApplication = this;
        context = getApplicationContext();


    }

    public static BNetApplication getInstance() {
        return bNetApplication;
    }
    public Context getContext() {
        return context;
    }

    public void DestoryBnetService() {
        isChecked = false;
        if (serviceConnection != null && serviceBind) {
            Log.d(TAG, "DestoryBnetService:   unbindsercice ");
            unbindService(serviceConnection);
            serviceBind = false;
        }
        if (bnetAidlInterface != null) {
            try {
                bnetAidlInterface.leave();
                stopService(mIntentConnectorService);
                bnetAidlInterface = null;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void startAndBindService() {
        Intent bnetService = new Intent(this, BnetService.class);
        startService(bnetService);


        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.d(TAG, "onServiceConnected:..... ");
                bnetAidlInterface = BnetAidlInterface.Stub.asInterface(iBinder);
                isChecked = true;
                serviceBind = true;
                StartBnetServiceJoin();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
        mIntentConnectorService = new Intent(getApplicationContext(), BnetService.class);
        bindService(mIntentConnectorService, serviceConnection, BIND_AUTO_CREATE);
    }

    public void BnetServiceJoin(String nWalletAddr, String dWalletAddr, String deviceAddr, int maskBit) {
        bnetServiceJoinParams = new BnetServiceJoinParams(nWalletAddr, dWalletAddr, deviceAddr, maskBit);
        if (bnetAidlInterface == null || !serviceBind) {
            startAndBindService();
        } else {
            StartBnetServiceJoin();
        }
    }

    private void StartBnetServiceJoin() {
        if (bnetServiceJoinParams != null && bnetAidlInterface != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        bnetAidlInterface.join(bnetServiceJoinParams.getnWalletAddr(), bnetServiceJoinParams.getdWalletAddr(), bnetServiceJoinParams.getDeviceAddr(), bnetServiceJoinParams.getMaskBit());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            try {
                bnetAidlInterface.CStartService();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
