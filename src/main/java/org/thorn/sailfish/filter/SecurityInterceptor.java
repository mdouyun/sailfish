package org.thorn.sailfish.filter;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.core.Status;
import org.thorn.sailfish.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: chen.chris
 * @Since: 13-10-14 下午5:49
 * @Version: 1.0
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    private String loginUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getServletPath();
        int firstQuestionMarkIndex = url.indexOf("?");

        if (firstQuestionMarkIndex != -1) {
            url = url.substring(0, firstQuestionMarkIndex);
        }

        if (StringUtils.equals(loginUrl, url)) {
            return true;
        }

        User sessionData = (User) request.getSession().getAttribute(Configuration.SESSION_USER);

        if (sessionData != null && !StringUtils.isEmpty(sessionData.getUserId())) {
            return true;
        }

        if (isJsonRequest(request)) {

            Status status = new Status();
            status.setSuccess(false);
            status.setMessage("用户未登陆");

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(status);

            response.setContentType("text/javascript;charset=utf-8");
            response.getWriter().write(json.toString());
            response.getWriter().flush();
        } else {
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + loginUrl);
        }

        return false;
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        return StringUtils.equalsIgnoreCase(request.getHeader("x-requested-with"), "XMLHttpRequest");
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
