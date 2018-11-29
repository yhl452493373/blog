/**

 @Name：layui.blog 闲言轻博客模块
 @Author：徐志文
 @License：MIT
 @Site：http://www.layui.com/template/xianyan/

 */
layui.define(['element', 'form', 'laypage', 'jquery', 'laytpl'], function (exports) {
    var element = layui.element
        , form = layui.form
        , laypage = layui.laypage
        , $ = layui.jquery
        , laytpl = layui.laytpl;

    //statr 分页

    laypage.render({
        elem: 'test1' //注意，这里的 test1 是 ID，不用加 # 号
        , count: 50 //数据总数，从服务端得到
        , theme: '#1e9fff'
    });

    // end 分頁

    // start 导航显示隐藏

    $("#mobile-nav").on('click', function () {
        $("#pop-nav").toggle();
    });

    // end 导航显示隐藏


    //start 评论的特效

    (function ($) {
        $.extend({
            tipsBox: function (options) {
                options = $.extend({
                    obj: null,  //jq对象，要在那个html标签上显示
                    str: "+1",  //字符串，要显示的内容;也可以传一段html，如: "<b style='font-family:Microsoft YaHei;'>+1</b>"
                    startSize: "12px",  //动画开始的文字大小
                    endSize: "30px",    //动画结束的文字大小
                    interval: 600,  //动画时间间隔
                    color: "red",    //文字颜色
                    callback: function () {
                    }    //回调函数
                }, options);

                $("body").append("<span class='num'>" + options.str + "</span>");

                var box = $(".num");
                var left = options.obj.offset().left + options.obj.width() / 2;
                var top = options.obj.offset().top - 10;
                box.css({
                    "position": "absolute",
                    "left": left + "px",
                    "top": top + "px",
                    "z-index": 9999,
                    "font-size": options.startSize,
                    "line-height": options.endSize,
                    "color": options.color
                });
                box.animate({
                    "font-size": options.endSize,
                    "opacity": "0",
                    "top": top - parseInt(options.endSize) + "px"
                }, options.interval, function () {
                    box.remove();
                    options.callback();
                });
            }
        });
    })($);

    function niceIn(prop) {
        prop.find('i').addClass('niceIn');
        setTimeout(function () {
            prop.find('i').removeClass('niceIn');
        }, 1000);
    }

    /**
     * 文章点赞,参数写到带有class中有like的标签上.参数为data-id.
     * 页面初始化一次即可
     *
     * @param additionalClass 附加的class,用于判断是哪个点赞按钮
     * @param paramType 参数类型:articleId或者commentId
     */
    function praise(additionalClass, paramType) {
        $(document).on('click' + additionalClass, additionalClass + '.like', function (e) {
            e.preventDefault();
            var param = {}, that = this, $that = $(this);
            param[paramType] = $that.attr('data-id');
            if (!$that.hasClass("layblog-this")) {
                $.ajax({
                    url: contextPath + '/data/praise/add',
                    data: param,
                    type: 'post',
                    success: function (result) {
                        if (result.status === 'success') {
                            that.text = '已赞';
                            $that.addClass('layblog-this');
                            $.tipsBox({
                                obj: $that,
                                str: "+1",
                                callback: function () {
                                    var $count = $that.children('.count');
                                    if ($count.length === 1) {
                                        $count.text(result.data['praiseCount']);
                                    }
                                }
                            });
                            niceIn($that);
                            layer.msg('点赞成功', {
                                icon: 6
                                , time: 1000
                            });
                        }
                    },
                    error: function (result) {
                        layer.msg('点赞失败', {
                            icon: 6
                            , time: 1000
                        });
                    }
                });
            } else {
                layer.msg('不可重复点赞', {
                    icon: 6
                    , time: 1000
                });
            }
        });
    }

    praise.paramType = {
        articleId: 'articleId',
        commentId: 'commentId',
        messageId: 'messageId'
    };

    //end 评论的特效

    // start  图片遮罩
    var layerphotos = document.getElementsByClassName('layer-photos-demo');
    for (var i = 1; i <= layerphotos.length; i++) {
        layer.photos({
            photos: ".layer-photos-demo" + i + ""
            , anim: 0
        });
    }

    /**
     * 初始化幻灯片展示图片效果.用于异步加载后的回调
     * @param photos 图片所在容器的class
     */
    function initLayerPhotos(photos) {
        layer.photos({
            photos: photos
            , anim: 0
        });
    }

    // end 图片遮罩

    $(document).on('keydown', '#search', function (e) {
        e.preventDefault();
        if (e.key === 'Enter')
            search();
    });

    function search() {
        var $searchForm = $('#searchForm');
        $.ajax({
            url: $searchForm.attr('action'),
            type: $searchForm.attr('method'),
            data: $searchForm.serialize(),
            success: function (result) {
                layer.alert(result.message + '请看控制台');
                console.log(result);
            },
            error: function (result) {
                layer.alert('搜索出错，请稍后再试');
            }
        });
    }

    //输出test接口
    exports('blog', {
        praise: praise,
        initLayerPhotos: initLayerPhotos
    });
});  
