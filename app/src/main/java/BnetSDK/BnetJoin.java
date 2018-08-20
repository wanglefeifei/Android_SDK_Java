package BnetSDK;

import android.content.Context;

import java.util.UUID;


public class BnetJoin {
    public void StartVpvJoin(Context context) {
        String dWalletAddr = SharePreferenceMain.getSharedPreference(context.getApplicationContext()).getdWalletAddr();
        if (dWalletAddr == null) {
            dWalletAddr = UUID.randomUUID().toString();
            SharePreferenceMain.getSharedPreference(context.getApplicationContext()).savedWalletAddr(dWalletAddr);
        }
        BNetApplication.getInstance().BnetServiceJoin(null, dWalletAddr, "", 32);
    }
}
