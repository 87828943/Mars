$(function() {
    locationUrl('main','home');
});

function logout(){
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
		}
	});
}
function reloadHtml(){
    window.location.reload();
}