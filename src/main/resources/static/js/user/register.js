$(function() {
    validate();
});
function validate(){
    $("#registerForm").bootstrapValidator({
        message: '数据验证失败',
        excluded: [':hidden'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: '请填写昵称！'
                    },
                    stringLength: {
                        max: 8,
                        message: '不能超过8个字~'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: '请填写密码！'
                    },
                    regexp: {
                        regexp: /^(?![0-9]+$)(?![a-zA-Z]+$)(?![^0-9a-zA-Z]+$)\S{6,20}$/,
                            message: '请输入6-20位字母数字组合！'
                    }
                }
            },
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
function register(){
    $("#registerForm").data('bootstrapValidator').destroy();
    validate();
    $("#registerForm").bootstrapValidator('validate');
    if (!$("#registerForm").data('bootstrapValidator').isValid()) {
        $("#errorMsg").html("数据不规范！");
        $("#errorMsg").show();
        return false;
    }

    var name = $("#name").val();
    var email = $("#email").val();
    var password = $("#password").val();

    $.ajax({
        url: "/user/register",
        type: "POST",
        data: {
            name: name,
            email: email,
            password: password
        },
        success: function(res) {
            if(res.resCode == "00000"){
                window.location.href="/";
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