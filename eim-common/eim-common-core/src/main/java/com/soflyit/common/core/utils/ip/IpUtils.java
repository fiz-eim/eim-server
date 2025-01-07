package com.soflyit.common.core.utils.ip;

import com.soflyit.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 获取IP方法
 *
 * @author soflyit
 */
@Slf4j
public class IpUtils {

    private static final String IPV4_REGEX_ONE = "((\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})";
    private static final String IPV4_REGEX_TWO = "((\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})/(\\d{1,3})";

    private static final int[] IpATypeRange;

    private static final int[] IpBTypeRange;

    private static final int[] IpCTypeRange;

    private static final int DefaultIpAMask;
    private static final int DefaultIpBMask;
    private static final int DefaultIpCMask;


    public final static int IP_A_TYPE = 1;
    public final static int IP_B_TYPE = 2;
    public final static int IP_C_TYPE = 3;
    public final static int IP_OTHER_TYPE = 4;


    static {
        IpATypeRange = new int[2];
        IpATypeRange[0] = getIpV4Value("1.0.0.1");
        IpATypeRange[1] = getIpV4Value("126.255.255.254");

        IpBTypeRange = new int[2];
        IpBTypeRange[0] = getIpV4Value("128.0.0.1");
        IpBTypeRange[1] = getIpV4Value("191.255.255.254");

        IpCTypeRange = new int[2];
        IpCTypeRange[0] = getIpV4Value("192.168.0.0");
        IpCTypeRange[1] = getIpV4Value("192.168.255.255");

        DefaultIpAMask = getIpV4Value("255.0.0.0");
        DefaultIpBMask = getIpV4Value("255.255.0.0");
        DefaultIpCMask = getIpV4Value("255.255.255.0");
    }


    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String ip = null;


        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {

            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {

            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {

            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {

            ipAddresses = request.getHeader("X-Real-IP");
        }


        if (ipAddresses != null && !ipAddresses.isEmpty()) {
            ip = ipAddresses.split(",")[0];
        }


        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    public static boolean internalIp(String ip) {
        byte[] addr = textToNumericFormatV4(ip);
        return internalIp(addr) || "127.0.0.1".equals(ip);
    }

    private static boolean internalIp(byte[] addr) {
        if (StringUtils.isNull(addr) || addr.length < 2) {
            return true;
        }
        final byte b0 = addr[0];
        final byte b1 = addr[1];

        final byte SECTION_1 = 0x0A;

        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;

        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                if (b1 == SECTION_6) {
                    return true;
                }
            default:
                return false;
        }
    }


    public static byte[] textToNumericFormatV4(String text) {
        if (text.isEmpty()) {
            return null;
        }

        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return null;
                    }
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return null;
                    }
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return bytes;
    }

    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn(e.getMessage(), e);
        }
        return "127.0.0.1";
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn(e.getMessage(), e);
        }
        return "未知";
    }


    public static List<String> getHostIps() {
        List<String> result = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && !ip.getHostAddress().contains(":")) {
                        result.add(ip.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return result;
    }

    public static boolean checkSameSegment(String ip1, String ip2, int mask) {

        if (ipV4Invalidate(ip1)) {
            return false;
        }
        if (ipV4Invalidate(ip2)) {
            return false;
        }
        int ipValue1 = getIpV4Value(ip1);
        int ipValue2 = getIpV4Value(ip2);
        return (mask & ipValue1) == (mask & ipValue2);
    }


    public static boolean checkSameSegmentByDefault(String ip1, String ip2) {
        int mask = getDefaultMaskValue(ip1);     // 获取默认的Mask
        return checkSameSegment(ip1, ip2, mask);
    }


    public static int getDefaultMaskValue(String anyIpV4) {
        int checkIpType = checkIpV4Type(anyIpV4);
        int maskValue = 0;
        switch (checkIpType) {
            case IP_C_TYPE:
                maskValue = DefaultIpCMask;
                break;
            case IP_B_TYPE:
                maskValue = DefaultIpBMask;
                break;
            case IP_A_TYPE:
                maskValue = DefaultIpAMask;
                break;
            default:
                maskValue = DefaultIpCMask;
        }
        return maskValue;
    }


    public static int checkIpV4Type(String ipV4) {
        int inValue = getIpV4Value(ipV4);
        if (inValue >= IpCTypeRange[0] && inValue <= IpCTypeRange[1]) {
            return IP_C_TYPE;
        } else if (inValue >= IpBTypeRange[0] && inValue <= IpBTypeRange[1]) {
            return IP_B_TYPE;
        } else if (inValue >= IpATypeRange[0] && inValue <= IpATypeRange[1]) {
            return IP_A_TYPE;
        }
        return IP_OTHER_TYPE;
    }



    public int getSegmentValue(String ipV4, int mask) {
        int ipValue = getIpV4Value(ipV4);
        return (mask & ipValue);
    }

    public static int getIpV4Value(String ipOrMask) {
        byte[] addr = textToNumericFormatV4(ipOrMask);
        int address1 = addr[3] & 0xFF;
        address1 |= ((addr[2] << 8) & 0xFF00);
        address1 |= ((addr[1] << 16) & 0xFF0000);
        address1 |= ((addr[0] << 24) & 0xFF000000);
        return address1;
    }


    public static boolean ipV4Invalidate(String ipv4) {
        return !ipv4Validate(ipv4, IPV4_REGEX_ONE) && !ipv4Validate(ipv4, IPV4_REGEX_TWO);
    }


    public static String getClientIp(HttpHeaders headers, List<String> instIps) {
        log.debug("获取客户端ip开始");

        if (headers == null) {
            return null;
        }

        String ip = null;


        String ipAddresses = headers.getFirst("X-Forwarded-For");
        if (StringUtils.isEmpty(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {

            ipAddresses = headers.getFirst("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {

            ipAddresses = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {

            ipAddresses = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {

            ipAddresses = headers.getFirst("X-Real-IP");
        }

        if (StringUtils.isEmpty(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {

            ipAddresses = headers.getFirst("Host");
            if (StringUtils.isNotEmpty(ipAddresses)) {
                ipAddresses = ipAddresses.replaceAll(":\\d+", "");
            }
        }


        if (ipAddresses != null && !ipAddresses.isEmpty()) {
            ip = ipAddresses.split(",")[0];
        }

        if (StringUtils.isEmpty(ip) || StringUtils.equals("localhost", ip) || StringUtils.equals("127.0.0.1", ip)) {
            log.debug("检查本地ip");
            List<String> ips = IpUtils.getHostIps();
            String oldIp = ip;
            ip = filterIp(ips, instIps);
            log.info("服务内部请求，无法识别请求IP:{}, 获取本机Ip：{}", oldIp, ip);
        }


        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = "127.0.0.1";
        }
        ip = ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
        log.info("request ip is: {}", ip);
        return ip;
    }


    private static String filterIp(List<String> ips, List<String> instIps) {
        String result = null;
        if (CollectionUtils.isNotEmpty(ips)) {
            result = ips.get(0);

            if (CollectionUtils.isNotEmpty(instIps)) {
                for (String ip : ips) {
                    for (String instIp : instIps) {
                        if (IpUtils.checkSameSegmentByDefault(ip, instIp)) {
                            return ip;
                        }
                    }
                }
            }
        }
        return result;
    }

    private static boolean ipv4Validate(String addr, String regex) {
        if (addr == null) {
            return false;
        } else {
            return Pattern.matches(regex, addr.trim());
        }
    }

}
