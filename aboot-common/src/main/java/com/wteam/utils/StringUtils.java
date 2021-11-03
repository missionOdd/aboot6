/*
 * copyleft © 2019-2021
 */
package com.wteam.utils;


import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author mission
 * @since 2019/07/07 12:04
 */
@Slf4j
public class StringUtils extends org.apache.commons.lang3.StringUtils {


    private static File file = null;
    private static DbConfig config;
    private static final char SEPARATOR = '_';
    private static final String UNKNOWN = "unknown";

    static {
        /*
         * 此文件不必关闭
         */
        String path = "ip2region/ip2region.db";
        String name = "ip2region.db";
        try {
            config = new DbConfig();
            file = FileUtil.inputStreamToFile(new org.springframework.core.io.ClassPathResource(path).getInputStream(), name);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 不为空返回该值，为null返回""
     * @param obj 对象
     * @return String
     */
    public static String getStr(Object obj){
        return obj!=null?obj.toString():"";
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs) {
        if (str != null) {
            for (String s : strs) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 获取ip地址
     * @param request /
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if  (localhost.equals(ip))  {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 获取当前机器的IP
     */
    public static String getLocalIp() {
        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return UNKNOWN;
        }
        byte[] ipAddr = addr.getAddress();
        StringBuilder ipAddrStr = new StringBuilder();
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr.append(".");
            }
            ipAddrStr.append(ipAddr[i] & 0xFF);
        }
        return ipAddrStr.toString();
    }
    /**
     * 根据ip获取详细地址
     * @param ip /
     */
    public static String getCityInfo(String ip) {
        try {
            DataBlock dataBlock = new DbSearcher(config, file.getPath())
                    .binarySearch(ip);
            String region = dataBlock.getRegion();
            String address = region.replace("0|", "");
            char symbol = '|';
            if (address.charAt(address.length() - 1) == symbol) {
                address = address.substring(0, address.length() - 1);
            }
            return address.equals(Const.REGION) ? "内网IP" : address;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 获取当前浏览器
     * @param request /
     */
    public static String getBrowser(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * 获取请求的系统
     * @param request /
     */
    public static String getOS(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        return operatingSystem.getName();
    }

    /**
     * 获取当前根路径
     * @param request /
     */
    public static String getIpAddress(HttpServletRequest request) {
        StringBuilder str=new StringBuilder();
        str.append(request.getScheme()).append("://").append(request.getServerName());
        if(!Arrays.asList(80,443).contains(request.getServerPort())){
            str.append(":").append(request.getServerPort());
        }
        str.append(request.getContextPath());
        str.append("/");
        return str.toString();
    }
    /**
     * 获得当天是周几
     */
    public static String getWeekDay() {
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }


    /**
     * 过滤emoji
     * \pP 中小写 p 是property的意思，表示 Unicode 属性，用于 Unicode 正表达式的前缀
     * P	标点字符
     * L	字母
     * M	标记符号（一般不会单独出现）
     * Z	分隔符（比如空格、换行等）
     * S	符号（比如数学符号、货币符号等）
     * N	数字（比如阿拉伯数字、罗马数字等）
     * C	其他字符
     */
    public static String filterEmoji(String str) {
        if (str == null) {
            return str;
        }
        //第过滤表情
        Pattern emoji = Pattern.compile("[^\u4e00-\u9fa5^a-z^A-Z^0-9^\\pP^\\pZ^\\pS]");
        Matcher emojiMatcher = emoji.matcher(str);
        if (emojiMatcher.find()) {
            // 将转化的获取的表情转换为□
            str = emojiMatcher.replaceAll(" ");
            return str;
        }
        return str;
    }


    /**
     * 清除掉字符串前后的特殊字符
     * @param content  原字符串
     * @param spliter  要清除的字符串,注意只有一位，如"((1,2,3,4))",spliter也只为"("
     * @return /
     */
    public static String replaceHeadAndEndCharToArray(String content, String spliter){
        if(content.isEmpty() || spliter.isEmpty()){
            return content;
        }
        //要匹配替换正则表达式的特殊字符需要在前面加\进行转义
        if(spliter.equals("*")
                || spliter.equals("\\")
                || spliter.equals("^")
                || spliter.equals("$")
                || spliter.equals("(")
                || spliter.equals(")")
                || spliter.equals("+")
                || spliter.equals(".")
                || spliter.equals("[")
                || spliter.equals("?")
                || spliter.equals("{")
                || spliter.equals("|")){
            spliter = "\\\\" + spliter;
        }
        String rex = "^" + spliter + "*|" + spliter + "*$";
        return "["+content.replaceAll(rex, "")+"]";
    }

    public static String toCommaSeparate(String content){
        if(isBlank(content)){
            return ",,";
        }
        return content.replaceAll("[\\[\\]]", ",");
    }

}
