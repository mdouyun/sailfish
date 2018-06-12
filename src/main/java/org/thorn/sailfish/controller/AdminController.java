package org.thorn.sailfish.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thorn.sailfish.core.Configuration;

import javax.servlet.http.HttpSession;

/**
 * @Author: yfchenyun
 * @Since: 13-10-15 下午3:57
 * @Version: 1.0
 */
@Controller
@RequestMapping("/am")
public class AdminController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(Configuration.SESSION_USER);

        return "redirect:/am/login";
    }

}
