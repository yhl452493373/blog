package com.yang.blog.config;

import com.yang.blog.shiro.ShiroRealm;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");

        //TODO 根据项目配置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //默认目录：
        filterChainDefinitionMap.put("/", "anon");
        //静态资源
        filterChainDefinitionMap.put("/static/**", "anon");
        //注册页面
        filterChainDefinitionMap.put("/register", "anon");
        //登录页面
        filterChainDefinitionMap.put("/login", "anon");
        //验证码
        filterChainDefinitionMap.put("/captcha", "anon");
        //注册过程地址
        filterChainDefinitionMap.put("/data/user/register", "anon");
        //登录过程地址
        filterChainDefinitionMap.put("/data/user/login", "anon");
        //留言页面
        filterChainDefinitionMap.put("/message", "anon");
        //文章列表页面
        filterChainDefinitionMap.put("/index", "anon");
        //关于页面
        filterChainDefinitionMap.put("/about", "anon");
        //文章页面
        filterChainDefinitionMap.put("/details/**", "anon");
        //评论页面
        filterChainDefinitionMap.put("/comment/**", "anon");
        //添加评论过程
        filterChainDefinitionMap.put("/data/comment/add", "anon");
        //获取评论
        filterChainDefinitionMap.put("/data/comment/list", "anon");
        //获取文章列表过程
        filterChainDefinitionMap.put("/data/article/list", "anon");
        //下载文件
        filterChainDefinitionMap.put("/data/file/download/**", "anon");

        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了.退出不用写.直接访问/logout就行
        filterChainDefinitionMap.put("/logout", "logout");

        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SessionManager sessionManager() {
        logger.debug("ShiroConfig.sessionManager()");
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //单位为毫秒（1秒=1000毫秒） 3600000毫秒为1个小时
        sessionManager.setSessionValidationInterval(3600000 * 12);
        //3600000 milliseconds = 1 hour
        sessionManager.setGlobalSessionTimeout(3600000 * 12);
        //是否删除无效的，默认也是开启
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启 检测，默认开启
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //创建会话Cookie
        Cookie cookie = new SimpleCookie(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
        sessionManager.setSessionIdCookie(cookie);
        return sessionManager;
    }

    @Bean
    public SecurityManager securityManager(RememberMeManager rememberMeManager) {
        logger.debug("ShiroConfig.securityManager()");
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(shiroRealm());
        securityManager.setSubjectFactory(new DefaultWebSubjectFactory());
        //记住我管理器
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        return new AuthorizationAttributeSourceAdvisor();
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //TODO 根据需要设置记住我cookie生效时间30天 ,单位秒
        simpleCookie.setMaxAge(30 * 24 * 60 * 60);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }
}
