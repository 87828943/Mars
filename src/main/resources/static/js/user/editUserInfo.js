$(function(){
    validate();
});

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
                    },
                }
            }
        }
    })
}

function editUserInfo(){

    $("#editUserInfoForm").data('bootstrapValidator').destroy();
    validate();
    $("#editUserInfoForm").bootstrapValidator('validate');
    if (!$("#editUserInfoForm").data('bootstrapValidator').isValid()) {
        return false;
    }

	var sex = $("input[name='sex']:checked").val();
	var description = $("#description").val();

    $.ajax({
        url: "/user/updateUserInfo",
        type: "POST",
        data:{
            sex:sex,
            description:description
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