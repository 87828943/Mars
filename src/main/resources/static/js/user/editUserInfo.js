$(function(){
	
});

function editUserInfo(){
	alert("aa");
	var sex = $("input[name='sex']:checked").val();
	$.ajax({
		url:"user/updateUserInfo"
		type:"POST"
		data:{
			sex:sex
		},
		success:function(res){
			if(res.resCode == "00000"){
				window.location.href="/";
			}else{
				$("#errorMsg").html("");
                $("#errorMsg").html(res.resMsg);
                $("#errorMsg").show();
			}
		},
		error:function(){
			
		}
	})
}