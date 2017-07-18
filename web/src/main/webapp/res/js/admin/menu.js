/**
 * 左侧菜单的封装插件
 */
define(['jquery', 'template'], function ($, template) {

    /**
     * 初始化菜单
     * build(".sidebar-menu", true);
     */

    //分级构建菜单
    function build(menu, root) {
        var $self = $(menu);
        $.getJSON("./sys/permission/i/json", {pid: $self.parent("li").attr("data-id")}, function (result) {
            $self.replaceWith(template("menu", {list: result, root: root}));
        });
    }

    //滚动到指定选项卡
    function scrollToTab(element) {
        var marginLeftVal = calSumWidth($(element).prevAll()), marginRightVal = calSumWidth($(element).nextAll());
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").outerWidth() < visibleWidth) {
            scrollVal = 0;
        } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
            if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
                scrollVal = marginLeftVal;
                var tabElement = element;
                while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content").outerWidth() - visibleWidth)) {
                    scrollVal -= $(tabElement).prev().outerWidth();
                    tabElement = $(tabElement).prev();
                }
            }
        } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
            scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
        }
        $('.page-tabs-content').animate({
            marginLeft: 0 - scrollVal + 'px'
        }, "fast");
    }

    //计算元素集合的总宽度
    function calSumWidth(elements) {
        var width = 0;
        $(elements).each(function () {
            width += $(this).outerWidth(true);
        });
        return width;
    }

    //监听菜单点击事件
    $(document).on('click', '.sidebar-menu li', function (e) {
        e.stopPropagation();
        var $self = $(this);
        if ($self.attr("data-leaf") == 'closed') {//如果不是叶子节点则展开
            if (!$self.attr("data-loaded")) {
                $self.attr("data-loaded", true);
                $self.delay(500).queue(function () {
                    build($self.find("ul"));
                });
            }
        } else {
            //控制左侧菜单的选中状态
            $self.addClass("active").siblings("li").removeClass('active');
            // 处理右侧的tab标签
            var dataUrl = $self.data('href'), menuName = $.trim($self.text()), flag = true;
            if (!$.trim(dataUrl))return false;
            // 遍历查看选项卡菜单是否已存在
            $('.J_menuTab').each(function () {
                if ($(this).data('id') == dataUrl) {
                    if (!$(this).hasClass('active')) {
                        $(this).addClass('active').siblings('.J_menuTab').removeClass('active');
                        scrollToTab(this);
                        // 显示tab对应的内容区
                        $('.content iframe').each(function () {
                            if ($(this).attr("src") == dataUrl) {
                                $(this).show().siblings('iframe').hide();
                                return false;
                            }
                        });
                    }
                    flag = false;
                    return false;
                }
            });
            // 选项卡菜单不存在
            if (flag) {
                var str = '<a class="active J_menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
                $('.J_menuTab').removeClass('active');

                // 添加选项卡对应的iframe
                var str1 = '<iframe width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
                $('.content').find('iframe').hide().parents('.content').append(str1);
                // 添加选项卡
                $('.J_menuTabs .page-tabs-content').append(str);
                scrollToTab($('.J_menuTab.active'));
            }
        }
    });

    //双击刷新iframe dbl
    $('.J_menuTabs').on('dblclick', '.J_menuTab', function () {
        var target = $('iframe[data-id="' + $(this).data('id') + '"]');
        target.attr('src', target.attr('src'));
    });

    // 左移按扭,查看左侧隐藏的选项卡
    $('.J_tabLeft').on('click', function () {
        var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").width() < visibleWidth) {
            return false;
        } else {
            var tabElement = $(".J_menuTab:first");
            var offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {//找到离当前tab最近的元素
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            offsetVal = 0;
            if (calSumWidth($(tabElement).prevAll()) > visibleWidth) {
                while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                    offsetVal += $(tabElement).outerWidth(true);
                    tabElement = $(tabElement).prev();
                }
                scrollVal = calSumWidth($(tabElement).prevAll());
            }
        }
        $('.page-tabs-content').animate({
            marginLeft: 0 - scrollVal + 'px'
        }, "fast");
    });

    // 右移按扭,查看右侧隐藏的选项卡
    $('.J_tabRight').on('click', function () {
        var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").width() < visibleWidth) {
            return false;
        } else {
            var tabElement = $(".J_menuTab:first");
            var offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {//找到离当前tab最近的元素
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            scrollVal = calSumWidth($(tabElement).prevAll());
            if (scrollVal > 0) {
                $('.page-tabs-content').animate({
                    marginLeft: 0 - scrollVal + 'px'
                }, "fast");
            }
        }
    });

    //点击叉叉关闭选项卡菜单
    $('.J_menuTabs').on('click', '.J_menuTab i', function () {
        var closeTabId = $(this).parents('.J_menuTab').data('id');
        var currentWidth = $(this).parents('.J_menuTab').width();
        // 当前元素处于活动状态
        if ($(this).parents('.J_menuTab').hasClass('active')) {
            // 当前元素后面有同辈元素，使后面的一个元素处于活动状态
            if ($(this).parents('.J_menuTab').next('.J_menuTab').size()) {
                var activeId = $(this).parents('.J_menuTab').next('.J_menuTab:eq(0)').data('id');
                $(this).parents('.J_menuTab').next('.J_menuTab:eq(0)').addClass('active');
                $('.content iframe').each(function () {
                    if ($(this).data('id') == activeId) {
                        $(this).show().siblings('.J_iframe').hide();
                        return false;
                    }
                });
                var marginLeftVal = parseInt($('.page-tabs-content').css('margin-left'));
                if (marginLeftVal < 0) {
                    $('.page-tabs-content').animate({
                        marginLeft: (marginLeftVal + currentWidth) + 'px'
                    }, "fast");
                }
                //  移除当前选项卡
                $(this).parents('.J_menuTab').remove();
                // 移除tab对应的内容区
                $('.content iframe').each(function () {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }
            // 当前元素后面没有同辈元素，使当前元素的上一个元素处于活动状态
            if ($(this).parents('.J_menuTab').prev('.J_menuTab').size()) {
                var activeId = $(this).parents('.J_menuTab').prev('.J_menuTab:last').data('id');
                $(this).parents('.J_menuTab').prev('.J_menuTab:last').addClass('active');
                $('.content iframe').each(function () {
                    if ($(this).data('id') == activeId) {
                        $(this).show().siblings('.J_iframe').hide();
                        return false;
                    }
                });
                //  移除当前选项卡
                $(this).parents('.J_menuTab').remove();
                // 移除tab对应的内容区
                $('.content iframe').each(function () {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }
        } else {// 当前元素不处于活动状态
            //  移除当前选项卡
            $(this).parents('.J_menuTab').remove();
            // 移除相应tab对应的内容区
            $('.content iframe').each(function () {
                if ($(this).data('id') == closeTabId) {
                    $(this).remove();
                    return false;
                }
            });
            scrollToTab($('.J_menuTab.active'));
        }
        return false;
    });

    // 点击选项卡菜单
    $('.J_menuTabs').on('click', '.J_menuTab', function () {
        if (!$(this).hasClass('active')) {
            var currentId = $(this).data('id');
            // 显示tab对应的内容区
            $('.content iframe').each(function () {
                if ($(this).data('id') == currentId) {
                    $(this).show().siblings('iframe').hide();
                    return false;
                }
            });
            $(this).addClass('active').siblings('.J_menuTab').removeClass('active');
            scrollToTab(this);
        }
    });

    //当tab标签过多时滚动到已激活的选项卡
    $('.J_tabShowActive').on('click', function () {
        scrollToTab($('.J_menuTab.active'));
    });

    // 关闭全部
    $('.J_tabCloseAll').on('click', function () {
        $('.page-tabs-content').children("[data-id]").not(":first").each(function () {
            $('iframe[data-id="' + $(this).data('id') + '"]').remove();
            $(this).remove();
        });
        $('.page-tabs-content').children(":first").addClass("active");
        $('iframe:first').show();
        $('.page-tabs-content').css("margin-left", "0");
    });

    //关闭其他选项卡
    $('.J_tabCloseOther').on('click', function () {
        $('.page-tabs-content').children("[data-id]").not(":first").not(".active").each(function () {
            $('iframe[data-id="' + $(this).data('id') + '"]').remove();
            $(this).remove();
        });
        $('.page-tabs-content').css("margin-left", "0");
    });

    return {
        init: build
    };
});