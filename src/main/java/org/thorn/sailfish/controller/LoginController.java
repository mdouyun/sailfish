package org.thorn.sailfish.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.entity.User;
import org.thorn.sailfish.enums.YesOrNoEnum;
import org.thorn.sailfish.service.UserService;

import javax.servlet.http.HttpSession;

/**
 * @Author: yfchenyun
 * @Since: 13-10-15 上午10:12
 * @Version: 1.0
 */
@Controller
@RequestMapping("/am")
public class LoginController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String username, String password, HttpSession session, ModelMap modelMap) {

        if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {

            User user = userService.findByPassword(username, password);

            if(user == null || user.getAvailable() - YesOrNoEnum.NO.getCode() == 0) {
                modelMap.put("error", "用户名或者密码错误");
                return "login";
            }

            session.setAttribute(Configuration.SESSION_USER, user);
            return "redirect:/am/index";
        } else {
            modelMap.put("error", "用户名或者密码为空");
            return "login";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }


}
