$(function () {
    $.ajax({
        type:"get",//请求类型
        url:"/api/v1/wechat/createQ",//请求的url
        dataType:"html",//ajax接口（请求url）返回的数据类型
        success:function(data){//data：返回数据（json对象）
            $("p").html(data)
        }
    });
})