function changeSql() {
    var value = $("#plainSql").val();
    if(isEmpty(value)){
        alert("请输入sql！");
        return;
    }
    $.ajax({
        url:"/insert/sql/rewrite",
        type:"POST",
        dataType:"json",
        data: {"data": value},
        success: function (data) {
            if(data.status){
                $("#encodeSql").html(data.data);
            }
        }
    });
}

function insertSql() {
    var value = $("#encodeSql").val();
    if(isEmpty(value)){
        alert("请输入sql！");
        return;
    }
    $.ajax({
        url:"/insert/sql/insert",
        type:"POST",
        dataType:"json",
        data: {"data": value},
        success: function (data) {
            if(data.status){

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