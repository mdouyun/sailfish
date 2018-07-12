package org.thorn.sailfish.label;

import freemarker.core.Environment;
import freemarker.template.*;
import org.thorn.sailfish.entity.Category;
import org.thorn.sailfish.service.CategoryService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 *  <@category id="">
 *      ${enName}
 *  </@category>
 *  列出栏目信息
 * @Author: chen.chris
 * @Since: 13-12-16 上午11:07
 * @Version: 1.0
 */
public class CategoryLabel implements TemplateDirectiveModel {

    private static final String KEY = "id";

    @Resource
    private CategoryService service;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        if(map.containsKey(KEY) && map.get(KEY) != null) {
            SimpleScalar scalar = (SimpleScalar) map.get(KEY);
            String enName = scalar.getAsString();
            Category category = service.queryById(enName);

            environment.setVariable("enName", ObjectWrapper.DEFAULT_WRAPPER.wrap(category.getEnName()));
            environment.setVariable("cnName", ObjectWrapper.DEFAULT_WRAPPER.wrap(category.getCnName()));
            environment.setVariable("path", ObjectWrapper.DEFAULT_WRAPPER.wrap(category.getPath()));
            environment.setVariable("sort", ObjectWrapper.DEFAULT_WRAPPER.wrap(category.getSort()));
            environment.setVariable("parent", ObjectWrapper.DEFAULT_WRAPPER.wrap(category.getParent()));
            environment.setVariable("hidden", ObjectWrapper.DEFAULT_WRAPPER.wrap(category.getHidden()));

            templateDirectiveBody.render(environment.getOut());
        }
    }
}
