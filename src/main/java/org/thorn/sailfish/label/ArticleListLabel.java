package org.thorn.sailfish.label;

import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang.StringUtils;
import org.thorn.sailfish.core.Page;
import org.thorn.sailfish.entity.Article;
import org.thorn.sailfish.enums.ArticleStatusEnum;
import org.thorn.sailfish.service.ArticleService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 *
 * <@category_article category="" index="1" size="20">
 * </@category_article>
 * @Author: chen.chris
 * @Since: 13-12-16 下午1:20
 * @Version: 1.0
 */
public class ArticleListLabel implements TemplateDirectiveModel {

    private static final String KEY = "category";

    private static final String SIZE = "size";

    private static final String INDEX = "index";

    @Resource
    private ArticleService service;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        if(map.containsKey(KEY) && map.get(KEY) != null) {
            SimpleScalar scalar = (SimpleScalar) map.get(KEY);
            String category = scalar.getAsString();

            scalar = (SimpleScalar) map.get(SIZE);
            long size = 20;
            if(scalar != null && StringUtils.isNotEmpty(scalar.getAsString())) {
                size = Long.parseLong(scalar.getAsString());
            }

            scalar = (SimpleScalar) map.get(INDEX);
            long index = 1;
            if(scalar != null && StringUtils.isNotEmpty(scalar.getAsString())) {
                index = Long.parseLong(scalar.getAsString());
            }

            Page<Article> page = new Page<Article>(index, size);
            service.queryPage(page, null, ArticleStatusEnum.PUBLISH.getCode(), category, null, null);

            environment.setVariable("total", ObjectWrapper.DEFAULT_WRAPPER.wrap(page.getTotal()));
            environment.setVariable("list", ObjectWrapper.DEFAULT_WRAPPER.wrap(page.getResultSet()));
            templateDirectiveBody.render(environment.getOut());
        }
    }
}
