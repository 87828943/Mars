$(function(){
	
});

function editUserInfo(){
	var sex = $("input[name='sex']:checked").val();
	var description = $("#description").val();
	$.ajax({
		url:"user/updateUserInfo",
		type:"POST",
		data:{
			sex:sex,
			description:description
		},
		success:function(res){
			console.log(res);
			if(res.resCode == "00000"){
				alert();
				$('#updateSuccessModal').modal('show');
            }else{
                $("#errorMsg").html(res.resMsg);
                $('#updateFailedModal').modal('show');
            }
		},
		error:function(){
			
		}
	})
}