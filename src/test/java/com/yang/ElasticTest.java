package com.yang;

import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.Application;
import com.yang.blog.es.dao.EsArticleDao;
import com.yang.blog.es.doc.EsArticle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ElasticTest {
    @Autowired
    private EsArticleDao esArticleDao;

    @Test
    public void delete(){

    }

    @Test
    public void insert(){
        EsArticle esArticle = new EsArticle();
        esArticle.setId(CommonUtils.uuid());
        esArticle.setTitle("张柏芝");
        esArticle.setContent("张柏芝士蛋糕店,夏洛特烦恼");
        esArticle.setPublishTime(LocalDateTime.now());

        esArticleDao.save(esArticle);
    }
}
