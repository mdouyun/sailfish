package org.thorn.sailfish.utils;

import javax.servlet.http.HttpSession;

/**
 * @Author: yfchenyun
 * @Since: 13-10-16 下午5:54
 * @Version: 1.0
 */
public class PathUtils {

    public static String getContextPath(HttpSession session) {
        return session.getServletContext().getRealPath("");
    }

}
