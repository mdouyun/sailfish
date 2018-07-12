package org.thorn.sailfish.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thorn.sailfish.core.JsonResponse;
import org.thorn.sailfish.core.Page;
import org.thorn.sailfish.core.Status;
import org.thorn.sailfish.entity.User;
import org.thorn.sailfish.service.UserService;
/**
 * TODO.
 *
 * @author chen.chris, 2014-03-13.
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/am/user")
public class UserController {

    static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index(Long pageIndex, Long pageSize, String userId, String userName, ModelMap modelMap) {

        Page<User> page = new Page<User>(pageIndex, pageSize);

        try {
            userService.queryPage(page, userId, userName);
            modelMap.put("page", page);
        } catch (Exception e) {
            log.error("Query user page", e);
        }

        return "user";
    }

    @RequestMapping("/create")
    @ResponseBody
    public Status createUser(User user) {
        Status status = new Status();

        try {
            userService.save(user);
            status.setMessage("新增用户成功");
        } catch (Exception e) {
            log.error("createUser" + user.toString(), e);
            status.setSuccess(false);
            status.setMessage("新增用户失败：" + e.getMessage());
        }

        return status;
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Status editUser(User user) {
        Status status = new Status();

        try {
            userService.modify(user);
            status.setMessage("修改用户成功");
        } catch (Exception e) {
            log.error("editUser" + user.toString(), e);
            status.setSuccess(false);
            status.setMessage("修改用户失败：" + e.getMessage());
        }

        return status;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Status deleteUser(String userId) {
        Status status = new Status();

        if(StringUtils.isBlank(userId)) {
            status.setMessage("用户ID为空");
            status.setSuccess(false);
            return status;
        }

        try {
            userService.delete(userId);
            status.setMessage("用户删除成功");
        } catch (Exception e) {
            log.error("deleteUser[" + userId + "]", e);
            status.setSuccess(false);
            status.setMessage("用户删除失败：" + e.getMessage());
        }

        return status;
    }

    @RequestMapping("/get")
    @ResponseBody
    public JsonResponse<User> getUser(String userId) {
        JsonResponse<User> jsonResponse = new JsonResponse<User>();

        try {
            User user = userService.findById(userId);

            if(user == null) {
                jsonResponse.setSuccess(false);
                jsonResponse.setMessage("用户未找到");
            } else {
                jsonResponse.setData(user);
                jsonResponse.setMessage("数据加载成功");
            }

        } catch (Exception e) {
            log.error("getUser[" + userId + "]", e);
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage("数据加载失败：" + e.getMessage());
        }

        return jsonResponse;
    }


}
