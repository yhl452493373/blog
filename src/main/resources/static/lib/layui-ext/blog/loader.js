/**

 @Name：layui.loader 闲言轻博客页面加载模块
 @Author：杨黄林
 @License：MIT
 @Site：http://www.yanghuanglin.com

 */
layui.define(['jquery', 'layer'], function (exports) {
    var $ = layui.jquery
        , layer = layui.layer;
    var loader;
    $(document).ajaxStart(function () {
        loader = layer.load();
    }).ajaxStop(function () {
        layer.close(loader);
    });

    function show() {
        layer.close(loader);
        layer.load();
    }

    function hide() {
        layer.close(loader);
    }

    //输出test接口
    exports('loader', {
        show: show,
        hide: hide
    });
});  
