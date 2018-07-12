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
import org.thorn.sailfish.entity.AdScript;
import org.thorn.sailfish.service.AdScriptService;

/**
 * @Author: chen.chris
 * @Since: 13-12-17 下午1:51
 * @Version: 1.0
 */
@Controller
@RequestMapping("/am/ad")
public class AdController {

    static Logger log = LoggerFactory.getLogger(AdController.class);

    @Autowired
    private AdScriptService adScriptService;


    @RequestMapping("/index")
    public String index(Long pageIndex, Long pageSize, String code, Integer status, ModelMap modelMap) {
        Page<AdScript> page = new Page<AdScript>(pageIndex, pageSize);

        try {
            adScriptService.queryPage(page, code, status);
            modelMap.put("page", page);

        } catch (Exception e) {
            log.error("Query adScript page", e);
        }

        return "adScript";
    }

    @RequestMapping("/create")
    @ResponseBody
    public Status createAdScript(AdScript adScript) {
        Status status = new Status();

        try {
            adScriptService.save(adScript);
            status.setMessage("广告脚本创建成功");
        } catch (Exception e) {
            log.error("createAdScript" + adScript.toString(), e);
            status.setSuccess(false);
            status.setMessage("广告脚本创建失败：" + e.getMessage());
        }

        return status;
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Status editAdScript(AdScript adScript) {
        Status status = new Status();

        try {
            adScriptService.modify(adScript);
            status.setMessage("广告脚本修改成功");
        } catch (Exception e) {
            log.error("editAdScript" + adScript.toString(), e);
            status.setSuccess(false);
            status.setMessage("广告脚本修改失败：" + e.getMessage());
        }

        return status;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Status deleteAdScript(String code) {
        Status status = new Status();

        if (StringUtils.isBlank(code)) {
            status.setMessage("广告代码为空");
            status.setSuccess(false);
            return status;
        }

        try {
            adScriptService.delete(code);
            status.setMessage("广告删除成功");
        } catch (Exception e) {
            log.error("deleteAdScript[" + code + "]", e);
            status.setSuccess(false);
            status.setMessage("广告删除失败：" + e.getMessage());
        }

        return status;
    }

    @RequestMapping("/get")
    @ResponseBody
    public JsonResponse<AdScript> getAdScript(String code) {
        JsonResponse<AdScript> jsonResponse = new JsonResponse<AdScript>();

        try {
            AdScript adScript = adScriptService.queryById(code);

            if (adScript == null) {
                jsonResponse.setSuccess(false);
                jsonResponse.setMessage("广告未找到");
            } else {
                jsonResponse.setData(adScript);
                jsonResponse.setMessage("数据加载成功");
            }

        } catch (Exception e) {
            log.error("getAdScript[" + code + "]", e);
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage("数据加载失败：" + e.getMessage());
        }

        return jsonResponse;
    }


}
