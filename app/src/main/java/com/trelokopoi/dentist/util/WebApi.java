package com.trelokopoi.dentist.util;

import android.content.Context;
import android.provider.Settings;

import com.trelokopoi.dentist.App;
import com.trelokopoi.dentist.BuildConfig;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class WebApi {

    private static final String URL = "";
    private static final String FINALURL = "http://52.58.254.12/K2m/";

    public static final String URL_CHECKINTERNET = "http://52.58.254.12/K2m/checkinternet.html";

    private static final String PLAYMAKER_URL = "playmaker.php";

    private static final String ACTION_AUTHENTICATE = "?action=authenticate";
    private static final String ACTION_NEWUSER = "?action=newUser";
    private static final String ACTION_GETCHILDREN = "?action=getChildren";
    private static final String ACTION_GETCHILDINFO = "?action=getChildInfo";
    private static final String ACTION_GETPRODUCTS = "?action=getProducts";
    private static final String ACTION_ADDPRODUCTTOCHILD = "?action=addProductToChild";
    private static final String ACTION_ADDPRODUCT = "?action=addProduct";
    private static final String ACTION_CHANGEPASSWORD = "?action=changePassword";
    private static final String ACTION_CHANGEEMAIL = "?action=changeEmail";
    private static final String ACTION_DELETEPRODUCTFROMUSER = "?action=deleteProductFromUser";
    private static final String ACTION_CHANGEPRODUCTFORCHILD = "?action=changeProductForChild";

    public final static String VALUE_FIELD_VERSION = "&v=";
    private static final String VALUE_USEREMAIL = "&userEmail=";
    private static final String VALUE_ACCESSCODE = "&accessCode=";
    private static final String VALUE_USERPASSWORD = "&userPassword=";
    private static final String VALUE_USERID = "&userId=";
    private static final String VALUE_USER_DEVICE_OS = "&userDeviceOs=";
    private static final String VALUE_USER_DEVICE_HW = "&userDeviceHardware=";
    private static final String VALUE_USER_DEVICE_SCR = "&userDeviceScreen=";
    private static final String VALUE_USER_NETWORK_CARRIER = "&userNetworkCarrier=";
    private static final String VALUE_USER_NETWORK_OPERATOR = "&userNetworkOperator=";
    private static final String VALUE_USER_PHONE_NUMBER = "&userPhoneNumber=";
    private static final String VALUE_DATE = "&date=";
    private static final String VALUE_CHILD_ID = "&childId=";
    private static final String VALUE_LETTERS = "&letters=";
    private static final String VALUE_ITEMID = "&itemId=";
    private static final String VALUE_AMOUNT = "&amount=";
    private static final String VALUE_TIME = "&time=";
    private static final String VALUE_NAME = "&name=";
    private static final String VALUE_SUGAR = "&sugar=";
    private static final String VALUE_NEWPASSWORD = "&newPassword=";
    private static final String VALUE_NEWEMAIL = "&newEmail=";
    private static final String VALUE_DIARYID = "&diaryId=";

    private static String returnURL() {
        if (!BuildConfig.DEBUG) {
            return FINALURL;
        }
        return FINALURL;
    }

    private static String deviceDataUrl() {

        String url = VALUE_USER_DEVICE_OS+DeviceInfo.returnAndroidVersion();
        url += VALUE_USER_DEVICE_HW+DeviceInfo.returnDeviceName();
        url += VALUE_USER_DEVICE_SCR+DeviceInfo.returnScreenDimensions();
        url += VALUE_USER_NETWORK_CARRIER+DeviceInfo.returnNetworkCarrier();
        url += VALUE_USER_NETWORK_OPERATOR+DeviceInfo.returnNetworkOperator();
        url += VALUE_USER_PHONE_NUMBER+DeviceInfo.returnPhoneNumber();
        return url;
    }

    public static JSONObject authenticate(Context context, String userEmail, String password) {

        String uniqueId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        L.debug(App.TAG, "uniqueId: "+uniqueId);

        String url  = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_AUTHENTICATE;
        url += VALUE_USEREMAIL+userEmail;
        url += VALUE_USERPASSWORD+sha1Hash(password);
        url += deviceDataUrl();

        url += VALUE_FIELD_VERSION+App.VERSION;
        return WebInterface.executeWeb(url);
    }

    public static JSONObject newUser(Context context, String userEmail, String accessCode, String password) {
        String uniqueId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        L.debug(App.TAG, "uniqueId: "+uniqueId);

        String url  = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_NEWUSER;
        url += VALUE_USEREMAIL+userEmail;
        url += VALUE_ACCESSCODE+accessCode;
        url += VALUE_USERPASSWORD+sha1Hash(password);
        url += deviceDataUrl();

        url += VALUE_FIELD_VERSION+App.VERSION;
        return WebInterface.executeWeb(url);
    }

    public static String getChidren() {
        String url = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_GETCHILDREN;

        url += VALUE_USEREMAIL+App.userEmail;
        url += VALUE_USERPASSWORD+App.password;
        url += VALUE_USERID+App.userId;

        return url;
    }

    public static String getChildInfo(String date, int childId) {
        String url = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_GETCHILDINFO;

        url += VALUE_USEREMAIL+App.userEmail;
        url += VALUE_USERPASSWORD+App.password;
        url += VALUE_USERID+App.userId;

        url += VALUE_DATE+date;
        url += VALUE_CHILD_ID+childId;

        return url;
    }

    public static JSONObject getChildInfo(Context context, String date, int childId) {
        String url = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_GETCHILDINFO;

        url += VALUE_USEREMAIL+App.userEmail;
        url += VALUE_USERPASSWORD+App.password;
        url += VALUE_USERID+App.userId;

        url += VALUE_DATE+date;
        url += VALUE_CHILD_ID+childId;

        return WebInterface.executeWeb(url);
    }

    public static JSONObject getProducts(String letters) {
        String url  = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_GETPRODUCTS;
        url += VALUE_USEREMAIL+App.userEmail;
        url += VALUE_USERPASSWORD+App.password;
        url += VALUE_USERID+App.userId;

        url += VALUE_LETTERS+letters;
        return WebInterface.executeWeb(url);
    }

    public static JSONObject addProductToChild(Integer prodId, Integer quantity, String date, String time, Integer belongs) {
        String url  = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_ADDPRODUCTTOCHILD;
        url += VALUE_USEREMAIL+App.userEmail;
        url += VALUE_USERPASSWORD+App.password;
        url += VALUE_USERID+App.userId;

        url += VALUE_ITEMID+prodId;
        url += VALUE_AMOUNT+quantity;
        url += VALUE_DATE+date;
        url += VALUE_TIME+time;
        url += VALUE_CHILD_ID+belongs;
        return WebInterface.executeWeb(url);
    }

    public static String addProduct(String name, String sugar) {
        String url = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_ADDPRODUCT;

        url += VALUE_USEREMAIL+App.userEmail;
        url += VALUE_USERPASSWORD+App.password;
        url += VALUE_USERID+App.userId;

        url += VALUE_NAME+name;
        url += VALUE_SUGAR+sugar;

        return url;
    }

    public static String changePassword(String password) {
        String url = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_CHANGEPASSWORD;

        url += VALUE_USEREMAIL+App.userEmail;
        url += VALUE_USERPASSWORD+App.password;
        url += VALUE_USERID+App.userId;

        url += VALUE_NEWPASSWORD+password;

        return url;
    }

    public static String changeEmail(String email) {
        String url = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_CHANGEEMAIL;

        url += VALUE_USEREMAIL+App.userEmail;
        url += VALUE_USERPASSWORD+App.password;
        url += VALUE_USERID+App.userId;

        url += VALUE_NEWEMAIL+email;

        return url;
    }

    public static String deleteProductFromUser(String diaryId) {
        String url = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_DELETEPRODUCTFROMUSER;

        url += VALUE_USEREMAIL+App.userEmail;
        url += VALUE_USERPASSWORD+App.password;
        url += VALUE_USERID+App.userId;

        url += VALUE_DIARYID+diaryId;

        return url;
    }

    public static String changeProductForChild(String diaryId, Integer quantity, String date, String time) {
        String url  = returnURL();
        url += PLAYMAKER_URL;
        url += ACTION_CHANGEPRODUCTFORCHILD;

        url += VALUE_USEREMAIL+App.userEmail;
        url += VALUE_USERPASSWORD+App.password;
        url += VALUE_USERID+App.userId;

        url += VALUE_DIARYID+diaryId;
        url += VALUE_AMOUNT+quantity;
        url += VALUE_DATE+date;
        url += VALUE_TIME+time;

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
                url += VALUE_FIELD_VERSION+App.VERSION;
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
