$("#queryCategoryLevel1").change(function () {
    var queryCategoryLevel1 =  $("#queryCategoryLevel1").val();
    if(queryCategoryLevel1!=null && queryCategoryLevel1!=''){
        $.post("/mag/info/category","parentId="+queryCategoryLevel1,callback,"json");
        $("#queryCategoryLevel2").html("");
        var options = "<option value=\"\">--请选择--</option>";
        function callback(data) {
            for(var i = 0;i<data.length;i++){
                options= options+ "<option value=\""+data[i].id+"\">"+data[i].categoryName+"</option>";
            }
            $("#queryCategoryLevel2").html(options);
        }
    }else{
        $("#queryCategoryLevel2").html("");
        var options = "<option value=\"\">--请选择--</option>";
        $("#queryCategoryLevel2").html(options);
    }
})

$("#queryCategoryLevel2").change(function () {
    var queryCategoryLevel2 =  $("#queryCategoryLevel2").val();
    if(queryCategoryLevel2!=null && queryCategoryLevel2!=''){
        $.post("/mag/info/category","parentId="+queryCategoryLevel2,callback,"json");
        $("#queryCategoryLevel3").html("");
        var options = "<option value=\"\">--请选择--</option>";
        function callback(data) {
            for(var i = 0;i<data.length;i++){
                options= options+ "<option value=\""+data[i].id+"\">"+data[i].categoryName+"</option>";
            }
            $("#queryCategoryLevel3").html(options);
        }
    }else{
        $("#queryCategoryLevel3").html("");
        var options = "<option value=\"\">--请选择--</option>";
        $("#queryCategoryLevel3").html(options);
    }
})

$(".checkApp").on("click",function(){
    var obj = $(this);
    var status = obj.attr("status");
    var vid = obj.attr("versionid");
    if(status == "1" && vid != "" && vid != null){//待审核状态下才可以进行审核操作
        window.location.href="check?aid="+ obj.attr("appinfoid") + "&vid="+ obj.attr("versionid");
    }else if(vid != "" || vid != null){
        alert("该APP应用没有上传最新版本,不能进行审核操作！");
    }else if(status != "1"){
        alert("该APP应用的状态为：【"+obj.attr("statusname")+"】,不能进行审核操作！");
    }
});
