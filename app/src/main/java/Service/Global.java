package Service;

import android.util.Log;

import java.io.FileDescriptor;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import BnetSDK.LogUtils;


class Global {
    public static byte u1RNodeStatus;
    public static FileDescriptor vpnFileDescriptor;
    public static DatagramSocket m_udpSocket;    //socket
    public static int             u2HNodeHbSeq;
    public static boolean         hnodereceivewhereis = false;
    public static byte            u1Tried = 10;
    public static byte[]          matchedToHeferId = new byte[64];
    public static byte[]          matched_b_u2ToRNodeId =  new byte[2];
    public static  boolean        defaultNodeMatched = false;
    public static InetAddress PeerNode = null;
    public static byte[]          b_u2ToRNodePort =  new byte[2];
    public static     int         toRNodePeerPort;
    public static  boolean        HNodeRcvMachDone = false;
    public static boolean         RcvDefaultNodeTryOrKeepLive = false;
    public static  boolean        hadStartRun =  false;
    public static  boolean        hadSendWhereIs = false;
    public static  boolean        hadRcvRegAck = false;
    public static  byte[]         hefer_header = new byte[136];
    public static String aesIv = "any";
    public static String aesKey = "any";
    public static FileChannel vpnOutput;
    // RNode_Config;
    public static     int         u2RNodeId;
    public static     int         u2DefaultRNodeId; //= 34;
    public static String strHNodeIp;
    public static     int         u2HNodePort;
    public static     int         u2HNodePort2;
    public static String strHeferId;
    public static String strSubnetScope;       //exp:10.0.1.0/24
    public static String strLanIp; //= "10.208.0.1";
    public static String strLanNetMask;

    public static String strGateway;
    public static String dns;
    public static String privateKey;
    public static String m_walletid;//address
    public  static String VPN_ADDRESS ;
    //public static   String      phoneNum = "17801113110";//need activity config
    //public static   String      phoneNum = "18911593481";//need activity config
    public static String phoneNum = "18511665456";//need activity config
    // public static   String      phoneNum = "17316063826";//need activity config zhangxiao
    // public static   String      heferid = "hefer_r9test";//russian
    public static String heferid = "172M8JQj7hh1Uf1sYvTf8NtT9vwxJTbRXg";//need activity config
    public static String configPara;
    public static String idAndKey;
    public static     int         RNode_PEERNODEMAX = 10;
    static Map<String, RouteItem> routeInfo = new HashMap<String, RouteItem>();
    static Map<String, MatchedNodeInfo> MatchedPeerNode = new HashMap<String, MatchedNodeInfo>();
    public static     long        curDestIp_lan;
    public static MatchedNodeInfo RNode_getLanMatchNodeInfo(long destIpLong)
    {
        long u4DestNet  = destIpLong & 0xFFFFFF00;
        String strDestNet = String.valueOf(u4DestNet);
        //Global.lanPeerMatchNode.put(strCurDestIp, new MatchedNodeInfo(Global.PeerNode,Global.toRNodePeerPort ));
        LogUtils.d("debug","222222strDestNet:"+strDestNet);

        Log.d("debug", "RNode_getLanMatchNodeInfo: " + Global.MatchedPeerNode.get(strDestNet));
        LogUtils.d("debug","run in func:RNode_getLanMatchNodeInfo()");
        return Global.MatchedPeerNode.get(strDestNet);
    }
    public static MatchedNodeInfo RNode_getWanOrOverMatchNodeInfo(long destIpLong)
    {
        String strDestIp = "0";
        LogUtils.d("debug","RNode_getWanOrOverMatchNodeInfo:"+Global.MatchedPeerNode.get(strDestIp));//��ѯ
        return Global.MatchedPeerNode.get(strDestIp);
    }

    public static RouteItem RNode_getOverWallRouteInfo(long destIpLong)
    {
        String strDestIp = "0";
        LogUtils.d("debug","RNode_getWanOrOverMatchNodeInfo:"+Global.routeInfo.get(strDestIp));//��ѯ
        return Global.routeInfo.get(strDestIp);
    }
    public static RouteItem RNode_getLanRouteInfo(long destIpLong)
    {
        long u4DestNet  = destIpLong & 0xFFFFFF00;
        String strDestNet = String.valueOf(u4DestNet);
        LogUtils.d("debug","RNode_getLanRouteInfo:"+Global.routeInfo.get(strDestNet));//��ѯ
        return Global.routeInfo.get(strDestNet);
    }

}