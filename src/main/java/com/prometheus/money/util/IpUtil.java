package com.prometheus.money.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// 核心变化在这里：使用 jakarta 替代 javax
import jakarta.servlet.http.HttpServletRequest;

/**
 * IP 地址工具类 (Spring Boot 3+ / Jakarta 版本)
 */
public class IpUtil {

    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    /**
     * 获取客户端真实IP地址
     */
    public static String getClientIpAddress() {
        // 1. 获取 RequestAttributes
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        
        // 2. 获取 Request 对象
        // 在 Spring Boot 3 中，attributes.getRequest() 返回的就是 jakarta.servlet.http.HttpServletRequest
        HttpServletRequest request = attributes.getRequest();

        String ipAddress = null;

        // 3. 循环检查所有可能的 Header
        for (String header : IP_HEADERS) {
            ipAddress = request.getHeader(header);
            if (!isUnknown(ipAddress)) {
                break;
            }
        }

        // 4. 如果 Header 中没有，则获取直连 IP
        if (isUnknown(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        // 5. 数据清洗与格式化
        return normalizeIp(ipAddress);
    }

    /**
     * 判断 IP 是否为空或 unknown
     */
    private static boolean isUnknown(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    /**
     * 标准化 IP 地址
     */
    private static String normalizeIp(String ipAddress) {
        if (ipAddress == null) {
            return null;
        }

        // A. 处理多级代理 (例如 "192.168.1.1, 10.0.0.1")
        if (ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.split(",")[0];
        }

        // B. 处理 IPv4 映射的 IPv6 地址 (例如 "::ffff:192.168.1.1")
        if (ipAddress.startsWith("::ffff:")) {
            ipAddress = ipAddress.substring(7);
        }

        // C. 处理本机 IPv6 地址 ("::1" 或 "0:0:0:0:0:0:0:1")
        if ("127.0.0.1".equals(ipAddress) || 
            "0:0:0:0:0:0:0:1".equals(ipAddress) || 
            "::1".equals(ipAddress)) {
            return null;
        }

        return ipAddress.trim();
    }
}