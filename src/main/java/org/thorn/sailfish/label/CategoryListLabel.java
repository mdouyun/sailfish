package org.thorn.sailfish.label;

import freemarker.core.Environment;
import freemarker.template.*;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.entity.Category;
import org.thorn.sailfish.service.CategoryService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *  <@category_list parent="">
 *      <#list list as c></#list>
 *  </@category_list>
 *  列出栏目的下级可显示栏目
 * @Author: yfchenyun
 * @Since: 13-12-6 下午3:44
 * @Version: 1.0
 */
public class CategoryListLabel implements TemplateDirectiveModel {

    private static final String KEY = "parent";

    @Resource
    private CategoryService service;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {

        List<Category> list = null;
        if(map.containsKey(KEY) && map.get(KEY) != null) {
            SimpleScalar scalar = (SimpleScalar) map.get(KEY);
            String parent = scalar.getAsString();
            list = service.queryAllShowByParent(parent);
        } else {
            list = service.queryAllShowByParent(Configuration.CATEGORY_ROOT);
        }

        environment.setVariable("list", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
        templateDirectiveBody.render(environment.getOut());
    }
}
