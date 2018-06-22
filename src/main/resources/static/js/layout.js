$(function() {
    locationUrl('test/test1','home');
});

function logout(){
	if(confirm("确认注销吗？")){
		$.ajax({
			url:"/user/logout",
			type:"POST",
			data:{},
			success:function(res){
				if(res.resCode == "00000"){
					window.location.href="/index";
				}
			},
			error:function(){
				cofirm
			}
		});
	}
}

function editUser(){
	
}
