var j = setInterval("myInterval()",1000);//1000为1秒钟
var i = 1;
function myInterval()
{
    $.ajax({
        type:"get",//请求类型
        url:"/api/v1/wechat/map",//请求的url
        dataType:"json",//ajax接口（请求url）返回的数据类型
        success:function(data){//data：返回数据（json对象）
            if (data!=null){
                location.href="/api/v1/wechat/userinfo"
            }else{
                i++
                if(i==20){
                    xiaohui();
                }
            }
        }
    });

}
function  xiaohui() {
    clearInterval(j)
}