package com.yang;

import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.entity.Article;
import com.yang.blog.entity.Message;
import com.yang.blog.es.doc.EsArticle;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTest {
    @Test
    public void testArticleUpdate(){
        Article article = new Article();
        article.setId(CommonUtils.uuid());
        article.setContent("66666");
        article.setTitle("99999");

        EsArticle esArticle = new EsArticle();
        esArticle.update(true,article);

        System.out.println(esArticle.getId());
        System.out.println(esArticle.getTitle());
        System.out.println(esArticle.getContent());
    }

    @Test
    public void testClone(){
        Message message = new Message();
        message.setContent("666666");
        Message message2 = message.clone();
        message2.setContent("999999");
        System.out.println(message.getContent());
        System.out.println(message2.getContent());
        System.out.println(message.getContent());
    }
}
