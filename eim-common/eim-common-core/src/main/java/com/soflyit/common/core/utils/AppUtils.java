package com.soflyit.common.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLDecoder;

/**
 * 类说明
 *
 * @author Toney
 * @date 2023-06-26
 */
@Slf4j
public class AppUtils {

    public static final String OS = System.getProperty("os.name").toLowerCase();


    public static void initAppPath(Class mainClass) {
        URL url = mainClass.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {

            filePath = URLDecoder.decode(url.getPath(), "utf-8");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (filePath == null) {
            log.error("路径不存在");
        }
        String logConfigDir = null;

        if (filePath.endsWith(".jar")) {
            log.debug("filePath with jar:" + filePath);
            filePath = filePath.substring(0, filePath.lastIndexOf('/') + 1);
            logConfigDir = filePath + "config/";
        } else if (filePath.endsWith(".jar!/BOOT-INF/classes!/")) {
            filePath = filePath.substring(0, filePath.lastIndexOf(".jar!"));
            filePath = filePath.substring(0, filePath.lastIndexOf('/') + 1);
            logConfigDir = filePath + "config/";
        } else if (filePath.endsWith("/classes/")) {
            logConfigDir = filePath;
            filePath = filePath.substring(0, filePath.length() - "classes/".length());
        }
        if (OS.indexOf("windows") >= 0) {
            if (filePath.startsWith("file:")) {
                filePath = filePath.substring("file:".length());
            }
            if (filePath.startsWith("/")) {
                filePath = filePath.substring("/".length());
            }

            if (logConfigDir != null && logConfigDir.startsWith("file:")) {
                logConfigDir = logConfigDir.substring("file:".length());

                if (logConfigDir.startsWith("/")) {
                    logConfigDir = logConfigDir.substring("/".length());
                }
            }
        } else {
            if (filePath.startsWith("file:")) {
                filePath = filePath.substring("file:".length());
            }

            if (logConfigDir != null && logConfigDir.startsWith("file:")) {
                logConfigDir = logConfigDir.substring("file:".length());
            }
        }

        System.setProperty("soflyit-app.home", filePath);
        if (StringUtils.isNotEmpty(logConfigDir)) {
            System.setProperty("soflyit-app.log-config.dir", logConfigDir);
        }
    }
}
