package org.thorn.sailfish.label;

import freemarker.core.Environment;
import freemarker.template.*;
import org.thorn.sailfish.entity.AdScript;
import org.thorn.sailfish.enums.YesOrNoEnum;
import org.thorn.sailfish.service.AdScriptService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: chen.chris
 * @Since: 13-12-19 下午7:59
 * @Version: 1.0
 */
public class AdScriptLabel implements TemplateDirectiveModel {

    private static final String KEY = "code";

    @Resource
    private AdScriptService adScriptService;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        if(map.containsKey(KEY) && map.get(KEY) != null) {
            SimpleScalar scalar = (SimpleScalar) map.get(KEY);
            String code = scalar.getAsString();

            AdScript adScript = adScriptService.queryById(code);

            if(adScript != null || adScript.getHidden() - YesOrNoEnum.YES.getCode() == 0) {
                return ;
            }

            environment.setVariable("code", ObjectWrapper.DEFAULT_WRAPPER.wrap(adScript.getCode()));
            environment.setVariable("html", ObjectWrapper.DEFAULT_WRAPPER.wrap(adScript.getHtml()));

            templateDirectiveBody.render(environment.getOut());
        }

    }




}
