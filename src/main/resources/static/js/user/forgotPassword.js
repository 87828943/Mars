$(function() {
    validate();
});

function validate(){
    $("#forgotPasswordForm").bootstrapValidator({
        message: '数据验证失败',
        excluded: [':hidden'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            email: {
                validators: {
                    notEmpty: {
                        message: '请填写邮箱！'
                    },
                    regexp: {
                        regexp: /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/,
                        message: '请输入正确的邮箱！'
                    }
                }
            }
        }
    })
}

function forgotPassword(){
    $("#forgotPasswordForm").data('bootstrapValidator').destroy();
    validate();
    $("#forgotPasswordForm").bootstrapValidator('validate');
    if (!$("#forgotPasswordForm").data('bootstrapValidator').isValid()) {
        $("#errorMsg").html("数据不规范！");
        $("#errorMsg").show();
        return false;
    }

    $("#emailbutton").attr("disabled",true);

    var email = $("#email").val();

    $.ajax({
        url: "/user/forgotPassword",
        type: "POST",
        data: {
            email: email
        },
        success: function(res) {
            if(res.resCode == "00000"){
                $("#errorMsg").html("");
                $("#errorMsg").html("发送成功！请点击邮件链接重置密码！");
                $("#errorMsg").show();
            }else{
                $("#emailbutton").attr("disabled",false);
                $("#errorMsg").html("");
                $("#errorMsg").html(res.resMsg);
                $("#errorMsg").show();
            }
        },
        error: function() {
        }
    });
}