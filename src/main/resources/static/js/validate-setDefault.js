/*
 * 以下为修改jQuery Validation插件兼容Bootstrap的方法，没有直接写在插件中是为了便于插件升级
 * 
 */
$.validator.setDefaults({
    highlight: function (element, errorClass, validClass) { // 错误的样式
        $(element).closest('.form-box').removeClass('has-success').addClass('has-error');
        if (element.type === "radio") {
            this.findByName(element.name).addClass(errorClass).removeClass(validClass);
        } else if (element.type === 'select-one') {
            console.log(1)
        } else {
            $(element).addClass(errorClass).removeClass(validClass);
        }
    },
    unhighlight: function (element, errorClass, validClass) { // 成功样式
        $(element).closest('.form-box').removeClass('has-error').addClass('has-success');
        if (element.type === "radio") {
            this.findByName(element.name).removeClass(errorClass).addClass(validClass);
        } else {
            $(element).removeClass(errorClass).addClass(validClass);
        }
    },
    errorElement: "span",
    errorPlacement: function (error, element) {
        if (element.is(":radio") || element.is(":checkbox")) {
            error.appendTo(element.closest('.form-controls'));
        } else {
            error.appendTo(element.closest('.form-controls'));
        }
    },
    // errorClass: "val_error",
    // validClass: "val_success"
});
