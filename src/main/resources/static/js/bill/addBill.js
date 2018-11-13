$(function(){
    init();
    validate();
});

function init(){
    $.ajax({
        url: "/bill/getBillTypeName",
        type: "GET",
        success: function(res) {
            if(res.resCode == "00000"){
                var array = res.data;
                for (var i =0;i<array.length;i++){
                    $("#type_name_list").append("<li><a href='#' id="+array[i].id+" class='s1'>"+array[i].name+"</a></li>");
                }
            }else{
                $("#errorMsg").html("");
                $("#errorMsg").html(res.resMsg);
                $("#errorMsg").show();
            }
        },
        error: function() {
        }
    });
}

function validate(){
    $("#editUserInfoForm").bootstrapValidator({
        message: '数据验证失败',
        excluded: [':hidden'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            description: {
                validators: {
                    notEmpty: {
                        message: '请填写描述！'
                    },
					stringLength: {
                        max: 15,
                        message: '不能超过15个字~'
                    }
                }
            }
        }
    })
}
