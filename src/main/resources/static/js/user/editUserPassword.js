$(function(){
    validate();
});

function validate(){
    $("#editUserPasswordForm").bootstrapValidator({
        message: '数据验证失败',
        excluded: [':hidden'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            oldPassword: {
                validators: {
                    notEmpty: {
                        message: '请填写旧密码！'
                    },
                    regexp: {
                        regexp: /^(?![0-9]+$)(?![a-zA-Z]+$)(?![^0-9a-zA-Z]+$)\S{6,20}$/,
                        message: '请输入6-20位字母数字组合！'
                    }
                }
            },
            newPassword: {
                validators: {
                    notEmpty: {
                        message: '请填写新密码！'
                    },
                    regexp: {
                        regexp: /^(?![0-9]+$)(?![a-zA-Z]+$)(?![^0-9a-zA-Z]+$)\S{6,20}$/,
                        message: '请输入6-20位字母数字组合！'
                    }
                }
            },
            newPassword2:{
                validators: {
                    identical: {//相同
                        field: 'newPassword', //需要进行比较的input name值
                        message: '两次密码不一致'
                    }
                    /*different: {//不能和用户名相同
                        field: 'username',//需要进行比较的input name值
                        message: '不能和用户名相同'
                    },*/
                }
            }
        }
    })
}

function editUserPassword(){
    $("#editUserPasswordForm").data('bootstrapValidator').destroy();
    validate();
    $("#editUserPasswordForm").bootstrapValidator('validate');
    if (!$("#editUserPasswordForm").data('bootstrapValidator').isValid()) {
        return false;
    }

    var newPassword = $("#newPassword").val();
    var oldPassword = $("#oldPassword").val();

    $.ajax({
        url: "/user/editUserPassword",
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