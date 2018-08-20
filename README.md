# Android_SDK_Java
BNET Android Java SDK

##Demo Case

**Open background VPN through startVPN () function in BnetSDK.
Close the background VPN through the DestoryBnetService () function in BnetSDK.**

    public void startVPN() {
        Intent vpnIntent = VpnService.prepare(MainActivity.this);
        if (vpnIntent != null) { 
            startActivityForResult(vpnIntent, VPN_REQUEST_CODE);//wait user confirmation, will call onActivityResult
        } else {
            onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK) {
            if (bnetJoin != null) {
                bnetJoin.StartVpvJoin(MainActivity.this);
            }
        }
    }
