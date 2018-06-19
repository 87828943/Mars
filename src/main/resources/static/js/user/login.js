$(function() {
    validate();
});

function validate(){
    $("#loginForm").bootstrapValidator({
        message: '数据验证失败',
        excluded: [':hidden'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            nameOrEmail: {
                validators: {
                    notEmpty: {
                        message: '请填写登录名！'
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
            }
        }
    })
}

function login(){
    $("#loginForm").data('bootstrapValidator').destroy();
    validate();
    $("#loginForm").bootstrapValidator('validate');
    if (!$("#loginForm").data('bootstrapValidator').isValid()) {
        $("#errorMsg").html("数据不规范！");
        $("#errorMsg").show();
        return false;
    }

    var nameOrEmail = $("#nameOrEmail").val();
    var password = $("#password").val();

    $.ajax({
        url: "/user/login",
        type: "POST",
        data: {
            name: nameOrEmail,
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