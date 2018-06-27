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
                    },
                    different: {
                        field: 'oldPassword',
                        message: '新密码不能和旧密码相同'
                    },
                }
            },
            newPassword2:{
                validators: {
                    notEmpty: {
                        message: '请再次填写新密码！'
                    },
                    identical: {//相同
                        field: 'newPassword',
                        message: '两次密码不一致'
                    }
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
            newPassword: newPassword,
            oldPassword: oldPassword
        },
        success: function(res) {
            if(res.resCode == "00000"){
                $('#updateSuccessModal').modal('show');
            }else{
                $("#errorMsg").html(res.resMsg);
                $('#updateFailedModal').modal('show');
            }
        },
        error: function() {
        }
    });
}