package com.yang;

import com.github.yhl452493373.utils.CommonUtils;
import com.hankcs.hanlp.HanLP;
import com.yang.blog.entity.Article;
import com.yang.blog.entity.Message;
import com.yang.blog.es.doc.EsArticle;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTest {
    @Test
    public void testEsArticleUpdate() {
        Article article = new Article();
        article.setId(CommonUtils.uuid());
        article.setContent("66666");
        article.setTitle("99999");

        EsArticle esArticle = new EsArticle();
        esArticle.update(false, article, EsArticle.class);

        System.out.println(esArticle.getType());
        System.out.println(esArticle.getId());
        System.out.println(esArticle.getTitle());
        System.out.println(esArticle.getContent());
    }

    @Test
    public void testArticleUpdate() {
        Article article = new Article();
        article.setId(CommonUtils.uuid());
        article.setTitle("标题");
        article.setContent("内容");

        Article newArticle = new Article();
        newArticle.setSummary("摘要");
        newArticle.update(true, article);
        System.out.println(newArticle.getId());
        System.out.println(newArticle.getTitle());
        System.out.println(newArticle.getContent());
        System.out.println(newArticle.getSummary());
    }

    @Test
    public void testClone() {
        Message message = new Message();
        message.setContent("666666");
        Message message2 = message.clone();
        message2.setContent("999999");
        System.out.println(message.getContent());
        System.out.println(message2.getContent());
        System.out.println(message.getContent());
    }

    @Test
    public void testGetSummary(){
        String content = "<p>&nbsp; &nbsp; &nbsp; &nbsp; 经过半个多月的努力，我的个人博客终于可以用了。</p><p>&nbsp; &nbsp; &nbsp; &nbsp; 欢迎大家没事过来看看。</p><p>&nbsp; &nbsp; &nbsp; &nbsp; 我的博客主要更新一些在java、js、css等方面遇到的问题及解决方案。</p><p>&nbsp; &nbsp; &nbsp; &nbsp; 多多捧场哦！</p><p><br></p><p>&nbsp; &nbsp; &nbsp; &nbsp; 我的github地址：https://github.com/yhl452493373</p>";
        String summary = HanLP.getSummary(content, 255);
        System.out.println(summary);
    }
}
