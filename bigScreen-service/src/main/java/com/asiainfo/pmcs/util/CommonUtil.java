package com.asiainfo.pmcs.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 * @author wangyy
 */
public class CommonUtil {
	private static Random rand=new Random();
	
	/**
	 * 判断对象是否为空(null或元素为0)
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if (obj == null)
			return true;
		if (obj == "")
			return true;
		if (obj instanceof String) {
			if (((String) obj).length() == 0) {
				return true;
			}
		} else if (obj instanceof Collection) {
			if (((Collection) obj).size() == 0) {
				return true;
			}
		} else if (obj instanceof Map) {
			if (((Map) obj).size() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否不为空（null,''）
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Object pObj) {
		if (pObj == null)
			return false;
		if (pObj == "")
			return false;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return false;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return false;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 当时毫秒值 + 7位随机数字
	 */
	public static String getUUID20() {
		return System.currentTimeMillis() + getRandomNumber(7);
	}
	
	/**
	 * 随机获取UUID字符串(无中划线)
	 */
	public static String getUUID32() {
		String uuid = UUID.randomUUID().toString();
		return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
	}
	
	/**
	 * 随机获取UUID字符串(有中划线)
	 */
	public static String getUUID36() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 获取指定长度的随机数字
	 */
	public static synchronized String getRandomNumber(int count){
		String S = "0123456789";
		Random r = new Random();
		String tmp ="";
		for (int i=0;i<count;i++){
			int index = r.nextInt(S.length());
			tmp += S.charAt(index);
		}
		return tmp;
	}

    /** 
    * 获得随机颜色
    */  
	public static String getRandomColor(int count){
		String S = "0123456789ABCDEF";
		Random r = new Random();
		String tmp ="";
		for (int i=0;i<count;i++){
			int index = r.nextInt(S.length());
			tmp += S.charAt(index);
		}
		return tmp;
	}
	
	/**
	 * 
	 */
	public static String fixStringLen(String str,int len,String fixStr){
		if (isEmpty(str) || str.length()>len){
			return str;
		}
		int qty = len - str.length(); //缺少多少位
		String rtn = "";
		for (int i=0;i<qty;i++){
			rtn +=fixStr;
		}
		return rtn+str;
	}
	
	/**
	 * 获取系统名称
	 */
	public static String getOSName(){
		return System.getProperties().getProperty("os.name");
	}
	
	/**
	 * 是否是window系统
	 */
	public static Boolean isWindows(){
		if (getOSName().toLowerCase().indexOf("window")>-1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 是否linux系统
	 */
	public static Boolean isLinux(){
		if (getOSName().toLowerCase().indexOf("linux")>-1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据正则表达式查找符合规则的子串
	 * @param _regexp 正则表达式
	 * @param content 源字符串
	 * @return 返回子串
	 * @return List<String>  
	 * @author add by chengxw
	 * @date 2013-5-24
	 */
	public static List<String> findString(String _regexp,String content){
		Pattern pattern = Pattern.compile(_regexp);  
	    Matcher match = pattern.matcher(content); 
	    List<String> list = new ArrayList<String>();
	    while(match.find()){
	    	list.add(match.group());
	    }
		return list;
	}
	
	
	/**
	 * 字符串解密
	 * @param s
	 * @return
	 * @return String
	 * @author add by chengxw
	 * @date 2013-5-31
	 */
	public static String decrypt(String s){
        int iLen = 0;
        String sText =s;
        String sResult ="";
        //String sRtn = "";
        iLen = sText.length();
        for (int i =1;i <= iLen;i++){
            sResult =  (char)(sText.substring(i - 1, i ).codePointAt(0) - (2 * (iLen - i -2))) +sResult ;
        }
        return sResult;
    }

	/**
	 * 字符串加密
	 */
	public static String encrypt(String str){
		StringBuffer resStr = new StringBuffer();
		StringBuffer buf = new StringBuffer(str.trim());
		buf = buf.reverse();//字符串翻转
		for (int i = 1; i <= buf.length(); i++) {
			char c = (char)(((buf.substring(i-1, i)).codePointAt(0))+((buf.length() - i - 2) * 2));
			resStr.append(c);
		}
		return resStr.toString();
	}
	
	/**JAVA 加密
	 * @param strSrc 需要加密人字符串，支持中文
	 * @param encName  空位MD5，支持MD5 ,SHA-1 SHA-256
	 * @return String
	 */
	public static String encrypt(String strSrc, String encName) {
		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes();
		try {
			if (encName == null || encName.equals("")) {
				encName = "MD5";
			}
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			return null;
		}
		return strDes;
	}

	/**
	 * 
	 */
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	/**
	 * 获取MAC地址的方法  
	 */
    private static String getMACAddress(InetAddress ia)throws Exception{  
        //获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。  
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();  
          
        //下面代码是把mac地址拼装成String  
        StringBuffer sb = new StringBuffer();  
          
        for(int i=0;i<mac.length;i++){  
            if(i!=0){  
                sb.append("-");  
            }  
            //mac[i] & 0xFF 是为了把byte转化为正整数  
            String s = Integer.toHexString(mac[i] & 0xFF);  
            sb.append(s.length()==1?0+s:s);  
        }  
          
        //把字符串所有小写字母改为大写成为正规的mac地址并返回  
        return sb.toString().toUpperCase();  
    }
    
    /**
     * 获取mac地址
     */
	public static String getMAC(){
    	String macAddress = "";
    	try {
    		InetAddress ia = InetAddress.getLocalHost();//获取本地IP对象  
    		macAddress = getMACAddress(ia);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return macAddress;
    }
	
	public static List<Object[]> getOrderedData(List<Object[]> list){
		List<Object[]> listres = new ArrayList<Object[]>();
		for(int i=0;i<list.size();i++){
			Object[] obj = list.get(i);
			Object[] result = new Object[obj.length+1];
			result[0] = i+1;
			for (int j = 1; j < result.length; j++) {
				result[j] = obj[j-1]==null ? "" : obj[j-1] + "";
			}
			listres.add(result);
		}
		return listres;
	}
	
	public static <T> T getDataFromList(T obj, List<T> list){
		T tagObj = null;
		for (T t : list) {
			if(t.equals(obj)){
				tagObj = t;
			}
		}
		return tagObj;
	}
	
	public static int getNextInt(){
		return rand.nextInt();
	}
	
}