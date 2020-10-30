package com.zuji.common.util;

/**
 * 自定义手机号加解密工具类
 *
 * @author Ink足迹
 * @create 2020-03-12 2:33 下午
 **/
public class CustomEncryUtil {

    private static final String[] DICTIONARIES = new String[]{"A", "=", "&", "q", "r", "t", "?", "B", "D", "h", "*", "l", "E", "F",
            "G", "b", "d", "H", "#", "L", "M", "N", "O", "Q", "R", "T", "Y", "@", "a", "e", "f", ":", "g", "m", "n", "y"};

    /**
     * 将手机号码中间4位变为*
     *
     * @param mobile 手机号码
     * @return
     */
    public static String concealMobile(String mobile) {
        if(mobile.length()==11){
            return String.format("%s****%s", mobile.substring(0, 3), mobile.substring(7));
        }else{
            return mobile;
        }

    }

    /**
     * 加密手机号码
     *
     * @param mobile 手机号码
     * @return 加密后内容
     */
    public static String encodeMobile(String mobile) {
        // 重新组合手机号码,取4，5两位放到后面，6，7两位放到前面
        String prefix = mobile.substring(3, 5);
        String suffix = mobile.substring(5, 7);
        StringBuffer sb = new StringBuffer();
        sb.append(suffix).append(mobile.substring(0, 3)).append(mobile.substring(7)).append(prefix);
        char[] regroupChar = sb.toString().toCharArray();

        StringBuffer encodeMobile = new StringBuffer();

        // 奇数位 乘以2，偶数位乘以3
        for (int i = 0; i < regroupChar.length; i++) {
            char c = regroupChar[i];
            int val = c - '0';
            if (0 == i % 2) {
                // 偶数
                encodeMobile.append(DICTIONARIES[val * 3]);
            } else {
                // 奇数
                encodeMobile.append(DICTIONARIES[val * 2]);
            }
        }
        return encodeMobile.toString();
    }

    /**
     * 解密手机号码
     *
     * @param encodeMobil 加密手机号
     * @return 返回解密的手机号码
     */
    public static String decodeMobil(String encodeMobil) {
        StringBuffer sb = new StringBuffer();

        char[] mobileChar = encodeMobil.toCharArray();
        for (int i = 0; i < mobileChar.length; i++) {
            char c = mobileChar[i];
            int index = getIndexByContent(c);
            if (0 == i % 2) {
                // 偶数
                sb.append(index / 3);
            } else {
                // 奇数
                sb.append(index / 2);
            }
        }
        String s = sb.toString();
        return String.format("%s%s%s%s",
                s.substring(2, 5), s.substring(s.length() - 2), s.substring(0, 2), s.substring(5, s.length() - 2));
    }

    /**
     * 通过内容获取下标
     *
     * @param val 内容
     * @return 下标值
     */
    private static int getIndexByContent(char val) {
        String valStr = String.valueOf(val);
        for (int i = 0; i < DICTIONARIES.length; i++) {
            if (valStr.equals(DICTIONARIES[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 加密身份证号码
     *
     * @param val 身份证
     * @return 加密数据
     */
    public static String encodeIdNo(String val) {
        if(val.length()>=15){
            return String.format("%s********%s",val.substring(0,6),val.substring(14));
        }else{
            return val;
        }

    }

    // public static void main(String[] args) {
    //     System.out.println(encodeMobile("18668276625"));
    // }
}
