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
