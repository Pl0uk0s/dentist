package com.trelokopoi.dentist.util;

import com.trelokopoi.dentist.App;
import com.trelokopoi.dentist.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class WebApi {

    private static final String URL = "";
    private static final String FINALURL = "";

    public static final String URL_CHECKINTERNET = "http://checkinternet.example.gr/";

    private static String returnURL() {

        if (FINALURL.isEmpty()) {
            //WebApi.setApiDirectory(WebInterface.executeWeb(getApiDirectory()));
        }

        if (!BuildConfig.DEBUG) {
            return FINALURL;
        }

        //return EDUURL;
        return FINALURL;
        //return URL;
    }

    private static String deviceDataUrl() {

        String url = DeviceInfo.returnAndroidVersion();
        url += DeviceInfo.returnDeviceName();
        url += DeviceInfo.returnScreenDimensions();
        url += DeviceInfo.returnNetworkCarrier();
        url += DeviceInfo.returnNetworkOperator();
        url += DeviceInfo.returnPhoneNumber();
        return url;
    }

    public static String sha1Hash(String stringToHash) {

        MessageDigest digest = null;
        byte[] result = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            result = digest.digest(stringToHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Another way to make HEX, my previous post was only the method like your solution
        StringBuilder sb = new StringBuilder();

        for (byte b : result) // This is your byte[] result..
        {
            sb.append(String.format("%02X", b));
        }

        return sb.toString().toLowerCase(Locale.US);
    }

    public static String encryptedUrl(String tempUrl) {

        L.debug("encURL",tempUrl);

        String url = "";
        int x = tempUrl.indexOf("?");
        if (x > 0) {
            url += tempUrl.substring(0, x);

            //L.debug("encURL",tempUrl.substring(0, x));
            //L.debug("encURL",tempUrl.substring(x));
            try {
                url += "?p="+Crypt.bytesToHex(new Crypt().encrypt(tempUrl.substring(x)));
                url += App.VERSION;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            L.debug("encURL",url);
        } else {
            url = tempUrl;
        }
        return url;
    }

}
