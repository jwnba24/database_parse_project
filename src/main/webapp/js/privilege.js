function grant() {
    var user = $("#user").val();
    var columns = [];
    $.each($('input:checkbox'),function(){
        if(this.checked){
            columns.push($(this).val());
        }
    });
    var columnsString = columns.join(",");
    if(isEmpty(user)||isEmpty(columnsString)){
        alert("请选择用户和数据列！");
        return;
    }
    $.ajax({
        url:"/privilege/generate",
        type:"POST",
        dataType:"json",
        data: {"columns": columnsString,"user":user},
        success: function (data) {
            if(data.status){
                $("#encodeSql").val(data.data);
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