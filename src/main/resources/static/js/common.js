$(document).ready(function () {
    // 菜单
    var $menuTitle = $('.menu-title');
    $menuTitle.parent('.list.active').find('.menu-item').css('display', 'block');
    $menuTitle.bind('click', function () {
        var $menuItem = $(this).next('.menu-item');
        var that = this;
        $menuItem.stop().slideToggle(300);
        $(that).parents('.list').toggleClass('active').siblings('.list');
        $(that).parents('.list').siblings('.list').removeClass('active').find('.menu-item').stop().slideUp(300);
    });

    // 查询表单重置
    var $searchForm = $('#searchForm');
    $("#resetBtn").bind('click', function () {
        $searchForm.get(0).reset();
        $searchForm.find('select').val(null).trigger('change')
    });

    // select
    var selectorConfig = {
        '.select2-default': {
            allowClear: true,
            minimumResultsForSearch: Infinity,
            placeholder: "请选择"
        },
        '.select2-province': {
            allowClear: true,
            minimumResultsForSearch: Infinity,
            placeholder: "省份"
        },
        '.select2-city': {
            allowClear: true,
            minimumResultsForSearch: Infinity,
            placeholder: "地级市"
        },
        '.select2-county': {
            allowClear: true,
            minimumResultsForSearch: Infinity,
            placeholder: "区/县/市"
        },
        '.select2-multiple': {
            placeholder: "请选择"
        },
        '.select2-order': {
            allowClear: false,
            minimumResultsForSearch: Infinity,
            placeholder: "请选择"
        }
    };
    for (var selector in selectorConfig) {
        $(selector).select2(selectorConfig[selector]);
    }
    $('form select').on('select2:select', function (evt) {
        $(this).valid()
    });

    // check
    $(".uniform-label input").uniform();

    // 自定义滚动条
    var slimScrollConfig = {
        '.slimScroll-default': {
            height: '100%',
            color: '#999999',
            size: '6px',
            wheelStep: 10,
            opacity: 0.7,
            alwaysVisible: false,
            borderRadius: 0,
            railBorderRadius: 0
        },
        '.slimScroll-inner': {
            height: '100%',
            color: '#999999',
            size: '6px',
            wheelStep: 10,
            opacity: 0.7,
            alwaysVisible: false,
            borderRadius: 0,
            railBorderRadius: 0
        }
    };
    for (var slimScroll in slimScrollConfig) {
        $(slimScroll).slimScroll(slimScrollConfig[slimScroll]);
    }

    // panel check全选
    $(document).on('change', '.panel-heading .uniform-label input', function () {
        var $panelBodyChecks = $(this).closest('.panel-heading').next('.panel-body').find('.uniform-label input');
        if ($(this).prop("checked")) {
            $panelBodyChecks.prop('checked',true);
            $.uniform.update();
        } else {
            $panelBodyChecks.prop('checked',false);
            $.uniform.update();
        }
    });
    $(document).on('change', '.panel-body .uniform-label input', function () {
        var $panelHeadingCheck = $(this).closest('.panel-body').prev('.panel-heading').find('.uniform-label input');
        var $panelBodyChecks = $(this).closest('.panel-body').find('.uniform-label input');
        var flag = true;
        $panelBodyChecks.each(function () {
            flag *= $(this).prop("checked");
        });
        if (flag) {
            $panelHeadingCheck.prop('checked',true);
            $.uniform.update();
        } else {
            $panelHeadingCheck.prop('checked',false);
            $.uniform.update();
        }
    });

    $('.date.date-mm input').datetimepicker({
        language: 'zh-CN', // 中文
        format: 'yyyy-mm', // 日期显示格式
        autoclose: true, // 选择后自动关闭
        todayBtn: true,
        minView: 3
    });
    $('.date.date-dd input').datetimepicker({
        language: 'zh-CN', // 中文
        format: 'yyyy-mm-dd', // 日期显示格式
        autoclose: true, // 选择后自动关闭
        todayBtn: true,
        minView: 3,
        todayHighlight: true
    });
    $('.date.date-MM input').datetimepicker({
        language: 'zh-CN', // 中文
        format: 'yyyy-mm-dd hh:ii', // 日期显示格式
        autoclose: true, // 选择后自动关闭
        todayBtn: true,
        minView: 0
    });
    // 表单校验
    $(".form-validate").each(function () {
        $(this).validate({
            onfocusout: function (element) {
                $(element).valid();
                $(".popover-validate").hide();
                $(element).parents('.form-controls').find(".popover-box").children(".popover-validate").show();
            }
        })
    });

    // collapse
    $(document).on('click', '.panel-group .title-area', function () {
        var $collapse = $(this).closest('.panel').find('.collapse');
        var $collapseAll = $(this).closest('.panel-group').find('.collapse');
        $collapseAll.stop().slideUp(300);
        $collapse.stop().slideToggle(300);
    });

    /**
     * 省市区三级联动
     */
    function AddressSelect(addressId, url) {
        this.$address = $('#' + addressId);
        this.$province = this.$address.find(".select2-province");
        this.$city = this.$address.find(".select2-city");
        this.$county = this.$address.find(".select2-county");
        this.url = url;
        this.fillOption(this.$province);
        var that = this;
        this.$province.on("change", function (e) {
            var provinceVal = $("#Province").val();
            if (provinceVal === "") {
                that.clearSelect(that.$city);
                return
            }
            that.fillOption($city, provinceVal);
        });
        this.$city.on("change", function (e) {
            var cityVal = $("#Province").val();
            if (cityVal === "") {
                that.clearSelect(that.$county);
                return
            }
            that.fillOption($county, cityVal);
        });
    }
    AddressSelect.prototype = {
        clearSelect: function ($select) {
            $select.html("<option value=''></option>").val(null).trigger("change");
        },
        fillOption: function ($select, changeSelectVal) {
            var that = this;
            changeSelectVal = changeSelectVal || null;
            $.ajax({
                url: this.url,
                cache: false,
                data: {code: changeSelectVal},
                dataType: "json",
                success: function(data) {
                    that.clearSelect($select); // 清空下拉框
                    $.each(data.address, function (i, item) {
                        $select.append("<option value='" + item.Value + "'>" + item.Text + "</option>");
                    });
                },
                error: function() {}
            });
        }
    }

});