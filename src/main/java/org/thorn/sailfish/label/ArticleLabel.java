package org.thorn.sailfish.label;

import freemarker.core.Environment;
import freemarker.template.*;
import org.thorn.sailfish.entity.Article;
import org.thorn.sailfish.enums.ArticleStatusEnum;
import org.thorn.sailfish.service.ArticleService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 *  <@article id="">
 *      ${title}
 *  </@article>
 * @Author: yfchenyun
 * @Since: 13-12-16 上午11:15
 * @Version: 1.0
 */
public class ArticleLabel implements TemplateDirectiveModel {

    private static final String KEY = "id";

    @Resource
    private ArticleService service;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        if(map.containsKey(KEY) && map.get(KEY) != null) {
            SimpleScalar scalar = (SimpleScalar) map.get(KEY);
            Integer id = Integer.parseInt(scalar.getAsString());
            Article article = service.queryArticle(id);

            if(article != null || article.getStatus() - ArticleStatusEnum.DELETE.getCode() == 0) {
                return ;
            }

            environment.setVariable("id", ObjectWrapper.DEFAULT_WRAPPER.wrap(article.getId()));
            environment.setVariable("category", ObjectWrapper.DEFAULT_WRAPPER.wrap(article.getCategory()));
            environment.setVariable("title", ObjectWrapper.DEFAULT_WRAPPER.wrap(article.getTitle()));
            environment.setVariable("content", ObjectWrapper.DEFAULT_WRAPPER.wrap(article.getContent()));
            environment.setVariable("creater", ObjectWrapper.DEFAULT_WRAPPER.wrap(article.getCreater()));
            environment.setVariable("modifyTime", ObjectWrapper.DEFAULT_WRAPPER.wrap(article.getModifyTime()));
            environment.setVariable("createTime", ObjectWrapper.DEFAULT_WRAPPER.wrap(article.getCreateTime()));
            environment.setVariable("starLevel", ObjectWrapper.DEFAULT_WRAPPER.wrap(article.getStarLevel()));
            environment.setVariable("status", ObjectWrapper.DEFAULT_WRAPPER.wrap(article.getStarLevel()));

            templateDirectiveBody.render(environment.getOut());
        }
    }
}
