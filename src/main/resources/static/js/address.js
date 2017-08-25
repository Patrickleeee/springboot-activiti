$(document).ready(function () {
    //绑定字典内容到指定的Select控件
    /**
     *
     * @param addressId id
     * @param url   地址
     * @param status    标志位，是否清空(0-清空，1-不清空)
     * @constructor
     */
    function AddrSelect(addressId, url, status){
        var address = $('#' + addressId );
        var option = $('#' + addressId + ' option');
        //绑定Ajax的内容
        if(option.length == 1 && status == "1"){ // 省份、只有一条，变更url
            url = url + "?province=" + option.val();
        }else{
            address.empty();//清空下拉框
        }
        $.getJSON(url, function (data) {
            // 加入一条空的option，防止选择第一条数据，无法带出想关联子菜单（change事件不触发）
            address.append("<option></option>");
            /*if(addressId == 'housingAddressEdit .select2-city'){// 房产信息，编辑状态，不需要空option
                address.empty();
            }*/
            $.each(data, function (i, item) {
                if(item.levelCode == 2){ // 地区，直接带出地址code
                    address.append("<option value='" + item.code + "'>" + item.name + "</option>");
                }else{// 省
                    address.append("<option value='" + item.name + "'>" + item.name + "</option>");
                }
            });
        });
    }

    /**
     * 获取默认地址的地址集合
     * @param firstAddr
     * @param secondAddr
     * @param url
     * @param thirdAddr
     */
    function defaultCitySelect(firstAddr, secondAddr, url, thirdAddr) {
        var firstAddress = $('#' + firstAddr );
        var secondAddress = $('#' + secondAddr );
        if(thirdAddr == ''){ // 查询市集合
            var urlStr = url + "?firstAddress=" + firstAddress.val() + "&&secondAddress=" + secondAddress.val();
        }else{
            var thirdAddress = $('#' + thirdAddr );
            var urlStr = url + "?firstAddress=" + firstAddress.val() + "&&secondAddress=" + secondAddress.val() + "&&thirdAddress=" + thirdAddress.val();
        }
        $.getJSON(urlStr, function (data) {
            $.each(data, function (i, item) {
                if(item.levelCode == 2){ // 地区，直接带出地址code
                    secondAddress.append("<option value='" + item.code + "'>" + item.name + "</option>");
                }else{
                    secondAddress.append("<option value='" + item.name + "'>" + item.name + "</option>");
                }
            });
        });

    }

    // 银行select
    function bankSelect(addressId){
        var address = $('#'+addressId);
        var option = $('#'+addressId +" option");
        var url = getUrl()+"/findAllBanks";
        if(option.length == 1){
            url = url + "?bankno=" + option.val();
        }else if(option.length > 1){
            return;
        }
        $.getJSON(url, function(data){
            $.each(data, function (i, item) {
                address.append("<option value='" + item.bankno + "'>" + item.bankname + "</option>");
            });
        })
    }

    var str = window.location.href; //取得整个地址栏
    var num = str.indexOf("?");
    var url = str.substring(0, num);

    /** 个人信息-家庭地址--start */
    AddrSelect('familyAddress .select2-province', url + '/findAllProvince', '1');

    $("#familyAddress .select2-province").on("change", function () {
        var province = $("#familyAddress .select2-province").val();
        // 省份变了，清空county区域
        $("#familyAddress .select2-county").empty();
        AddrSelect("familyAddress .select2-city", url + '/findAllCityByProvince?province='+ province, '0');
    });

    $("#familyAddress .select2-city").on("change", function () {
        var city = $("#familyAddress .select2-city").val();
        // AddrSelect("familyAddress .select2-county", url + '/findAllNameByCity?city='+ city, '0');
        $("#familyAddress .select2-county").empty();
        defaultCitySelect('familyAddress .select2-city', 'familyAddress .select2-county', url + '/findDefaultCountyByCityAndCounty', 'familyAddress .select2-province');
    });

    // 个人信息默认地址集合初始化
    $(".ibox-tools .personalInfoEdit").on("click", function(){
        // 默认省份的市集合
        if($('#familyAddress .select2-city option').length == 1){// 仅有数据库默认值，带出全部集合
            defaultCitySelect('familyAddress .select2-province', 'familyAddress .select2-city', url + '/findDefaultCityByProvinceAndCity', '');
        }
        // 默认市的区集合
        if($('#familyAddress .select2-county option').length == 1){// 仅有数据库默认值，带出全部集合
            defaultCitySelect('familyAddress .select2-city', 'familyAddress .select2-county', url + '/findDefaultCountyByCityAndCounty', 'familyAddress .select2-province');
        }
    });

    /** 个人信息-家庭地址--end */



    /** 职业和财富信息-单位地址--start */
    AddrSelect('jobAddress .select2-province', url + '/findAllProvince', '1');

    $("#jobAddress .select2-province").on("change", function () {
        var province = $("#jobAddress .select2-province").val();
        // 省份变了，清空county区域
        $("#jobAddress .select2-county").empty();
        AddrSelect("jobAddress .select2-city", url + '/findAllCityByProvince?province='+ province, '0');
    });

    $("#jobAddress .select2-city").on("change", function () {
        var areaCode = $("#jobAddress .select2-city").val();
        // AddrSelect("jobAddress .select2-county", url + '/findAllNameByAreaCode?areaCode='+ areaCode, '0');
        $("#jobAddress .select2-county").empty();
        defaultCitySelect('jobAddress .select2-city', 'jobAddress .select2-county', url + '/findDefaultCountyByCityAndCounty', 'jobAddress .select2-province');

    });

    // 职业和财富信息-默认地址-初始化
    $(".ibox-tools .professionalInfoEdit").on("click", function(){
        // 银行select
        bankSelect('salBank_update');
        // 默认省份的市集合
        if($('#jobAddress .select2-city option').length == 1) {// 仅有数据库默认值，带出全部集合
            defaultCitySelect('jobAddress .select2-province', 'jobAddress .select2-city', url + '/findDefaultCityByProvinceAndCity', '');
        }
        // 默认市的区集合
        if($('#jobAddress .select2-county option').length == 1) {// 仅有数据库默认值，带出全部集合
            defaultCitySelect('jobAddress .select2-city', 'jobAddress .select2-county', url + '/findDefaultCountyByCityAndCounty', 'jobAddress .select2-province');
        }
    });
    /** 职业和财富信息-单位地址--end */


    /** 房产信息-房产地址--start */

    // 添加--地址初始化、银行初始化
    $(".ibox-tools .housingInfoAdd").on("click", function () {
        AddrSelect('houseAddress .select2-province', url + '/findAllProvince', '0');
        bankSelect('fundBank_add');
    });

    $("#houseAddress .select2-province").on("change", function () {
        var province = $("#houseAddress .select2-province").val();
        // 省份变了，清空county区域
        $("#houseAddress .select2-county").empty();
        AddrSelect("houseAddress .select2-city", url + '/findAllCityByProvince?province='+ province, '0');
    });

    $("#houseAddress .select2-city").on("change", function () {
        $("#houseAddress .select2-county").empty();
        defaultCitySelect('houseAddress .select2-city', 'houseAddress .select2-county', url + '/findDefaultCountyByCityAndCounty', 'houseAddress .select2-province');
    });

    // 编辑--地址初始化
    AddrSelect('housingAddressEdit .select2-province', url + '/findAllProvince', '0');
    // 动态变化
    $("#housingAddressEdit .select2-province").on("change", function () {
        var province = $("#housingAddressEdit .select2-province").val();
        // 带入银行select
        bankSelect('fundBank_update');
        // 省份变了，清空county区域
        $("#housingAddressEdit .select2-county").empty();
        AddrSelect("housingAddressEdit .select2-city", url + '/findAllCityByProvince?province='+ province, '0');
    });

    $("#housingAddressEdit .select2-city").on("change", function () {
        $("#housingAddressEdit .select2-county").empty();
        defaultCitySelect('housingAddressEdit .select2-city', 'housingAddressEdit .select2-county', url + '/findDefaultCountyByCityAndCounty', 'housingAddressEdit .select2-province');
    });


    /** 房产信息-房产地址--end */
});