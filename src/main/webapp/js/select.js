function changeSql() {
    var value = $("#plainSql").val();
    if(isEmpty(value)){
        alert("请输入sql！");
        return;
    }
    $.ajax({
        url:"/select/sql/rewrite",
        type:"POST",
        dataType:"json",
        data: {"sql": value},
        success: function (data) {
            if(data.status){
                $("#encodeSql").val(data.data);
            }
        }
    });
}

function insertSql() {
    var sql = $("#plainSql").val();
    var user = $("#user").val();
    if(isEmpty(sql)){
        alert("请输入sql！");
        return;
    }
    $.ajax({
        url:"/select/sql/select",
        type:"POST",
        dataType:"json",
        data: {"sql": sql,"user":user},
        success: function (data) {
            if(data.status){
                $("#result").val(data.data);
            }else {
                alert(data.data);
            }
        }
    });
}

function isEmpty(obj) {
    if (typeof obj == "undefined" || obj == null || obj == "") {
        return true;
    } else {
        return false;
    }
}