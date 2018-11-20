<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>撰写-闲言轻博客</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="../res/layui/css/layui.css">
        <link rel="stylesheet" href="../res/static/css/mian.css">
        <style>
            .create-tags-container {
                background-color: #fff;
                border: 1px solid #e6e6e6;
            }
        </style>
    </head>
    <body class="lay-blog">
        <div class="header">
            <div class="header-wrap">
                <h1 class="logo pull-left">
                    <a href="index.html">
                        <img src="../res/static/images/logo.png" alt="" class="logo-img">
                        <img src="../res/static/images/logo-text.png" alt="" class="logo-text">
                    </a>
                </h1>
                <form class="layui-form blog-seach pull-left" action="">
                    <div class="layui-form-item blog-sewrap">
                        <div class="layui-input-block blog-sebox">
                            <i class="layui-icon layui-icon-search"></i>
                            <input type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                </form>
                <div class="blog-nav pull-right">
                    <ul class="layui-nav pull-left">
                        <li class="layui-nav-item"><a href="index.html">文章</a></li>
                        <li class="layui-nav-item layui-this"><a href="create.html">撰写</a></li>
                        <li class="layui-nav-item"><a href="message.html">留言</a></li>
                        <li class="layui-nav-item"><a href="about.html">关于</a></li>
                    </ul>
                    <a href="login.html" class="personal pull-left">
                        <i class="layui-icon layui-icon-username"></i>
                    </a>
                </div>
                <div class="mobile-nav pull-right" id="mobile-nav">
                    <a href="javascript:;">
                        <i class="layui-icon layui-icon-more"></i>
                    </a>
                </div>
            </div>
            <ul class="pop-nav" id="pop-nav">
                <li><a href="index.html">文章</a></li>
                <li><a href="create.html">撰写</a></li>
                <li><a href="message.html">留言</a></li>
                <li><a href="about.html">关于</a></li>
                <li><a href="login.html">登录</a></li>
            </ul>
        </div>
        <div class="container-wrap">
            <form class="layui-form create-form">
                <div class="layui-form-item">
                    <input type="text" class="layui-input create-title-input" placeholder="请输入文章标题">
                </div>
                <div class="layui-form-item">
                    <textarea id="content" placeholder="文章内容" style="display: none;"></textarea>
                </div>
                <div class="layui-form-item create-tags-container">
                    <label class="layui-form-label">文章标签:</label>
                    <div class="layui-input-block">
                        <div class="inputTags">
                            <input type="hidden" name="tags" id="tags">
                            <input type="text" id="inputTags" class="inputTagsInput" placeholder="输入标签">
                        </div>
                    </div>
                </div>
            </form>
            <div class="create-button-group">
                <button class="layui-btn layui-btn-normal">发布文章</button>
                <button class="layui-btn layui-btn-normal">保存草稿</button>
                <button class="layui-btn create-button-back">返回</button>
            </div>
        </div>
        <div class="footer">
            <p>
                <span>&copy; 2018</span>
                <span><a href="http://www.layui.com" target="_blank">layui.com</a></span>
                <span>MIT license</span>
            </p>
            <p><span>人生就是一场修行</span></p>
        </div>
        <script src="../res/layui/layui.js">

        </script>
        <script>
            layui.extend({
                blog: '{/}../res/layui-ext/blog/blog',
                inputTags: '{/}../res/layui-ext/input-tags/inputTags'
            }).use(['blog', 'layedit', 'inputTags', 'jquery'], function () {
                var layedit = layui.layedit;
                var inputTags = layui.inputTags;
                var $ = layui.jquery;

                layedit.set({
                    uploadImage: {
                        url: '' //图片上传接口url,此接口需要返回
                        /**
                         * {
                         *    "code": 0 //0表示成功，其它失败
                         *    ,"msg": "" //提示信息 //一般上传失败后返回
                         *    ,"data": {
                         *      "src": "图片路径"
                         *      ,"title": "图片名称" //可选
                         *    }
                         *  }
                         */
                        , type: 'post' //get or post
                        , down: function (data) {
                            //data -- 接口返回数据中的data
                        }
                    }
                });

                layedit.build('content', {
                    height: 400
                }); //建立编辑器

                inputTags.render({
                    elem: '#inputTags',
                    content: [],
                    aldaBtn: false,
                    focus: false,
                    done: function () {
                        console.log(this.get());
                    },
                    del: function () {
                        console.log(this.get());
                    }
                });
            });
        </script>
    </body>
</html>