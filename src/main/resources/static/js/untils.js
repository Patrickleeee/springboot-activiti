/**
 * confirm
 */
window.confirm = function(msg, title, callback){
    title = title || '对话框';
    msg = msg || '无提示内容';
    callback = callback || new Function;
    var confirmModalStr = "<div class='modal fade' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true' id='confirmModal'>\
            <div class='modal-dialog'>\
                <div class='modal-content'>\
                    <div class='modal-header'>\
                        <button type='button' class='close' data-dismiss='modal'><span aria-hidden='true' class='iconfont'>&#xe609;</span></button>\
                        <h4 class='modal-title'>" + title + "</h4>\
                    </div>\
                    <div class='modal-body'>\
                        <div class='text-center'>" + msg + "</div>\
                    </div>\
                    <div class='modal-footer'>\
                        <button type='button' class='btn btn-default btn-sm' data-dismiss='modal'>取消</button>\
                        <button type='button' class='btn btn-primary btn-sm' id='confirmSubmit'>确定</button>\
                    </div>\
                </div>\
            </div>\
        </div>";
    $("body").append(confirmModalStr);
    $('#confirmModal').modal({backdrop: 'static', keyboard: false});
    $('#confirmModal').on('hidden.bs.modal', function (e) {
        $('#confirmModal').remove();
    });
    $('#confirmSubmit').on('click', function () {
        $('#confirmModal').modal('hide');
        callback();
    });
}

/**
 * alert
 */
window.alert = function(msg){
    msg = msg || "";
    var alertModalStr = "<div class='modal fade' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true' id='alertModal'>\
            <div class='modal-dialog'>\
                <div class='modal-content'>\
                    <div class='modal-header'>\
                        <button type='button' class='close' data-dismiss='modal'><span aria-hidden='true' class='iconfont'>&#xe609;</span></button>\
                        <h4 class='modal-title'>提示信息</h4>\
                    </div>\
                    <div class='modal-body'>\
                        <div class='text-center'>" + msg + "</div>\
                    </div>\
                    <div class='modal-footer'>\
                        <button type='button' class='btn btn-primary btn-sm' data-dismiss='modal'>确定</button>\
                    </div>\
                </div>\
            </div>\
        </div>";
    $("body").append(alertModalStr);
    $('#alertModal').modal({backdrop: 'static', keyboard: false});
    $('#alertModal').on('hidden.bs.modal', function (e) {
        $('#alertModal').remove();
    });
}

/**
 * toast
 * type：toast类型
 *      success 成功
 *      warning 警告
 *      danger 错误
 * text：提示内容
 */
window.toast = function (type, text) {
    type = type || success;
    text = text || "";
    var typeClass = "alert-" + type;
    var toastStr = '<div class="alert toast ' + typeClass + '" role="alert">\
            <span class="text">'+ text +'</span>\
        </div>'
    $("body").append(toastStr);
    $('.toast').fadeIn(300);
    setTimeout(function(){
        $(".toast").fadeOut(300, function () {
            $(this).remove()
        });
    },2000);
}

/**
 * loading
 */
window.loading = {
    show: function (text) {
        text = text || "请稍后...";
        var loadingStr = '<div class="loading">\
                <div class="loading-info">\
                    <div class="loader">\
                        <div class="loader-inner ball-spin-fade-loader">\
                            <div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div>\
                        </div>\
                    </div>\
                    <span>' + text + '</span>\
                </div>\
            </div>';
        $("body").append(loadingStr);
        $('.loading').fadeIn(300);
    },
    close: function(){
        $('.loading').fadeOut(300, function () {
            $(this).remove()
        });
    }
};

/**
 * delete
 */
function deleteData (callback) {
    callback = callback || new Function;
    confirm('确定要删除吗？', '提示', callback)
}

// 树
(function ($) {
    $.fn.menuTree = function (options) {
        var defualts = { switchingMode: "click" };
        var opts = $.extend({}, defualts, options);
        $(this).on("click", function () {
            var parents = $(this).parents("tr");
            var num = parents.attr("id");
            var data = parents.data("status");
            $("[id^=" + num + "_]").removeClass("hide");
            if (data == "open") {
                $("[id^=" + num + "_]").stop().fadeOut();
                parents.data("status", "close");
                $(this).find('.menu_toggle').removeClass("icon-tree_open").addClass("icon-tree_close");
            } else if (data == "close") {
                $("[id^=" + num + "_]").each(function () {
                    var secondLevel = $(this).attr("id");
                    $(this).fadeIn();
                    if ($(this).data("status") == "open") {
                        $(this).stop().fadeIn();
                    } else {
                        var otherid = $(this).attr("id");
                        $("[id^=" + otherid + "_]").each(function () {
                            $(this).addClass("hide");
                        });
                    }
                });
                parents.data("status", "open");
                $(this).find('.menu_toggle').removeClass("icon-tree_close").addClass("icon-tree_open");
            }
        });
    }
})(jQuery);