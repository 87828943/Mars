$(function() {
    validate();
});

function validate(){
    $("#newPasswordForm").bootstrapValidator({
        message: '数据验证失败',
        excluded: [':hidden'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            newPassword: {
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

function newPassword1(){
    $("#newPasswordForm").data('bootstrapValidator').destroy();
    validate();
    $("#newPasswordForm").bootstrapValidator('validate');
    if (!$("#newPasswordForm").data('bootstrapValidator').isValid()) {
        $("#errorMsg").html("数据不规范！");
        $("#errorMsg").show();
        return false;
    }

    var newPassword = $("#newPassword").val();
    var email = queryReqString("email");
    var key = queryReqString("key");

    $.ajax({
        url: "/user/setNewPassword",
        type: "POST",
        data: {
            newPassword:newPassword,
            email: email,
            key:key
        },
        success: function(res) {
            if(res.resCode == "00000"){
                alert("密码更改成功！");
                window.location.href="/login";
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

function queryReqString(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}