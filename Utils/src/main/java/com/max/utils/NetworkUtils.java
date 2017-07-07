package com.max.utils;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.tencent.gpframework.log.ALog;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by elwinxiao on 2015/10/20.
 */
public class NetworkUtils {
    public static String getLocalIp() {
        return getIPAddress(true);
    }

    public static String getLocalIpv6() {
        return getIPAddress(false);
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = addr instanceof Inet4Address;
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public static String intIpToString(int n) {
        long ip = (n & 0xffffffff);
        StringBuffer sb = new StringBuffer();

        sb.append(ip & 0xFF).append(".");
        sb.append((ip >> 8) & 0xFF).append(".");
        sb.append((ip >> 16) & 0xFF).append(".");
        sb.append((ip >> 24) & 0xFF);

        return sb.toString();
    }


    private final static String TAG = "NetworkUtil";

    // ------------------ common -------------------
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        // 这里必须用isConnected,不能用avaliable，因为有网络的情况isAvailable也可能是false
        return info != null && info.isConnected();
    }

    public static boolean isWifiConnected(Context context) {
        if (context == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isViaMobile(Context context) {
        if (context == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public static NetworkInfo getActiveNetworkInfo(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            return connMgr.getActiveNetworkInfo();
        } catch (Throwable e) {
            ALog.e(TAG, "fail to get active network info: " + e);
            return null;
        }
    }

    // ------------------ apn & proxy -------------------
    public static class NetworkProxy implements Cloneable {

        public final String host;
        public final int port;

        NetworkProxy(String host, int port) {
            this.host = host;
            this.port = port;
        }

        final NetworkProxy copy() {
            try {
                return (NetworkProxy) clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String toString() {
            return host + ":" + port;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj != null && obj instanceof NetworkProxy) {
                NetworkProxy proxy = (NetworkProxy) obj;
                if (TextUtils.equals(this.host, proxy.host) && this.port == proxy.port)
                    return true;
            }

            return false;
        }
    }

    public final static String APN_NAME_WIFI = "wifi";

    private final static Uri PREFERRED_APN_URI
            = Uri.parse("content://telephony/carriers/preferapn");

    private final static HashMap<String, NetworkProxy> sAPNProxies
            = new HashMap<String, NetworkProxy>();

    static {
        sAPNProxies.put("cmwap", new NetworkProxy("10.0.0.172", 80));
        sAPNProxies.put("3gwap", new NetworkProxy("10.0.0.172", 80));
        sAPNProxies.put("uniwap", new NetworkProxy("10.0.0.172", 80));
        sAPNProxies.put("ctwap", new NetworkProxy("10.0.0.200", 80));
    }

    public static NetworkProxy getProxy(Context context, boolean apnProxy) {
        return !apnProxy ? getProxy(context) : getProxyByAPN(context);
    }

    public static NetworkProxy getProxy(Context context) {
        if (!isViaMobile(context)) {
            return null;
        }
        String proxyHost = getProxyHost(context);
        int proxyPort = getProxyPort(context);
        if (!isEmpty(proxyHost) && proxyPort >= 0) {
            return new NetworkProxy(proxyHost, proxyPort);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private static String getProxyHost(Context context) {
        String host = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            host = Proxy.getDefaultHost();
        } else {
            host = System.getProperty("http.proxyHost");
        }
        return host;
    }

    @SuppressWarnings("deprecation")
    private static int getProxyPort(Context context) {
        int port = -1;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            port = Proxy.getDefaultPort();
        } else {
            String portStr = System.getProperty("http.proxyPort");
            if (!isEmpty(portStr)) {
                try {
                    port = Integer.parseInt(portStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        if (port < 0 || port > 65535) {
            // ensure valid port.
            port = -1;
        }
        return port;
    }

    public static NetworkProxy getProxyByAPN(Context context) {
        if (!isViaMobile(context)) {
            return null;
        }
        String apn = getAPN(context);
        NetworkProxy proxy = sAPNProxies.get(apn);
        return proxy == null ? null : proxy.copy();
    }

    //接入点的名称：
    //移动2G/2.5G cmnet cmwap
    //移动3G
    //移动4G  cmnet

    //联通2G: uniwap uninet
    //联通3G: 3gwap  3gnet
    //联通4G: 同3G?

    //电信2G: ctwap ctnet
    //电信3G:
    //电信4G:
    public static String getAPN(Context context) {
        NetworkInfo activeNetInfo = getActiveNetworkInfo(context);
        if (activeNetInfo == null) {
            ALog.e(TAG, "no active network");
            return null;
        }

        final int subNetworkType = activeNetInfo.getSubtype();
        ALog.d(TAG, "getAPN, subNetworkType: " + subNetworkType);

        String apn = null;
        if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            apn = APN_NAME_WIFI;
        } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
                    while (cursor != null && cursor.moveToNext()) {
                        apn = cursor.getString(cursor.getColumnIndex("apn"));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }

            if (TextUtils.isEmpty(apn)) {
                apn = activeNetInfo.getExtraInfo();
            }
        }

        if (apn != null) {
            apn = apn.toLowerCase();
        }

        return apn;
    }

    // ---------------- dns ------------------
    public final static class DNS {
        public String primary;
        public String secondary;

        DNS() {
        }

        @Override
        public String toString() {
            return primary + "," + secondary;
        }
    }

    public static DNS getDNS(Context context) {
        DNS dns = new DNS();
        if (context != null) {
            if (isWifiConnected(context)) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                if (dhcpInfo != null) {
                    dns.primary = int32ToIPStr(dhcpInfo.dns1);
                    dns.secondary = int32ToIPStr(dhcpInfo.dns2);
                }
            }
        }
        if (dns.primary == null && dns.secondary == null) {
            // retrieve dns with property.
            dns.primary = PropertyUtils.get(PropertyUtils.PROPERTY_DNS_PRIMARY, null);
            dns.secondary = PropertyUtils.get(PropertyUtils.PROPERTY_DNS_SECNDARY, null);
        }
        return dns;
    }

    private static String int32ToIPStr(int ip) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(ip & 0xFF).append(".");
        buffer.append((ip >> 8) & 0xFF).append(".");
        buffer.append((ip >> 16) & 0xFF).append(".");
        buffer.append((ip >> 24) & 0xFF);

        return buffer.toString();
    }

    // ---------------- utils ------------------
    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    // -----------------------------------------
    private NetworkUtils() {
        // static use.
    }

    /****************************************************
     * 获取移动网络的类型
     ****************************************************/

    /**
     * 调用说明：手机当前活动连接是移动网络才意义，否则返回值为0
     *
     * @param context
     * @return
     */
    static public int getMobileDataNetWorkClass(Context context) {
        TelephonyManager tm;
        try {
            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                ALog.e(TAG, "fail to get TELEPHONY_SERVICE.");
                return NETWORK_TYPE_UNKNOWN;
            }
        } catch (Throwable e) {
            ALog.e(TAG, "fail to get TELEPHONY_SERVICE: " + e);
            return NETWORK_TYPE_UNKNOWN;
        }
        final int networkType = tm.getNetworkType();
        ALog.d(TAG, "Mobile network type: " + networkType);
        final int networkClass = getNetworkClass(networkType);
        return networkClass;
    }

    public static String getMacAdress(Context context) {
        WifiManager wifiMng = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMng.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

    /**
     * Unknown network class. {@hide}
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_4_G = 3;

    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;
    /**
     * Current network is GSM {@hide}
     */
    public static final int NETWORK_TYPE_GSM = 16;
    /**
     * Current network is TD_SCDMA {@hide}
     */
    public static final int NETWORK_TYPE_TD_SCDMA = 17;
    /**
     * Current network is IWLAN {@hide}
     */
    public static final int NETWORK_TYPE_IWLAN = 18;

    private static int getNetworkClass(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_GSM:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
            case NETWORK_TYPE_TD_SCDMA:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
            case NETWORK_TYPE_IWLAN:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }
}
