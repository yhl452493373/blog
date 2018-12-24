/**

 @Name：layui.blog 闲言轻博客模块
 @Author：徐志文
 @License：MIT
 @Site：http://www.layui.com/template/xianyan/

 */
layui.define(['element', 'form', 'jquery', 'laytpl'], function (exports) {
    var element = layui.element
        , form = layui.form
        , $ = layui.jquery
        , laytpl = layui.laytpl;

    $(function () {
        var highlight = ace.require("ace/ext/static_highlight");
        $('.ace_code_highlight_container .ace_code_highlight').each(function () {
            var $this = $(this);
            if ($this.closest('.edit-content').length === 0) {
                highlight(this, {
                    mode: this.getAttribute("ace-mode"),
                    theme: this.getAttribute("ace-theme"),
                    startLineNumber: 1,
                    showGutter: this.getAttribute("ace-gutter"),
                    trim: false
                }, function (highlighted) {
                    $this.parent().attr('class', 'ace_code_highlight_container ' + highlighted.themeClassName);
                });
            }
        });
    });

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
            var that = this, $that = $(this), praiseId = $that.attr('data-id');
            if (localStorage.getItem(praiseId + '_praised') === 'yes') {
                layer.msg("不可重复点赞");
                return;
            }
            if (!$that.hasClass("layblog-this")) {
                $.ajax({
                    url: contextPath + '/data/praise/add',
                    data: {
                        praiseType: paramType,
                        praiseId: praiseId
                    },
                    type: 'post',
                    success: function (result) {
                        if (result.status === 'success') {
                            localStorage.setItem(praiseId + '_praised', 'yes');
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
                layer.msg('不可重复点赞');
            }
        });
    }

    praise.paramType = {
        article: 'article',
        comment: 'comment',
        message: 'message'
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
        if (e.key === 'Enter') {
            e.preventDefault();
            search();
        }
    });

    function search() {
        var $searchForm = $('#searchForm');
        $.ajax({
            url: $searchForm.attr('action'),
            type: $searchForm.attr('method'),
            data: $searchForm.serialize(),
            success: function (result) {
                if (result.count === 0) {
                    layer.alert('未找到相关记录,换个词试试');
                } else {
                    layer.open({
                        type: 1,
                        title: '搜索结果 -- ' + $searchForm.get(0).content.value,
                        area: ['800px', '600px'],
                        skin: 'search-popup',
                        content: function () {
                            var resultHtml = ['<div class="search-item-box">'];
                            result.data.forEach(function (item, index) {
                                item.index = index;
                                resultHtml.push(laytpl([
                                    '<a href="' + contextPath + '/details/{{ d.id }}" target="_self">',
                                    '   <div class="search-item">',
                                    '      <div class="search-item-content layer-photos-demo layer-photos-demo{{ d.index }}">',
                                    '          <h3>',
                                    '              <span>{{ d.title }}</span>',
                                    '          </h3>',
                                    '          <h5>发布于：<span>{{ d.publishTime }}</span></h5>',
                                    '          <div class="preview-content">{{ d.summary }}</div>',
                                    '      </div>',
                                    '      <div class="search-item-info">',
                                    '          <span>阅读{{ d.readCount }}</span>',
                                    '          <span>点赞{{ d.praiseCount }}</span>',
                                    '      </div>',
                                    '   </div>',
                                    '</a>'
                                ].join('')).render(item));
                            });
                            resultHtml.push('</div>');
                            return resultHtml.join('');
                        }(),
                        shadeClose: true
                    })
                }
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
