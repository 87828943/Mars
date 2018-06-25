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

//$(document).ready(function() {  
//	$("#btn").click(function () {  
//		if(confirm("确认上传？")){  
//			var imagePath = $("#uploadimage").val();    
//			if (imagePath == "") {    
//				alert("please upload image file",2);  
//				return false;    
//			}    
//			var strExtension = imagePath.substr(imagePath.lastIndexOf('.') + 1);    
//			if (strExtension!='jpg') {   
//				if (strExtension!='bmp') {   
//					if (strExtension!='png') {   
//						alert("please upload file that is a image",2);    
//						return false;   
//					}  
//				}  
//			}
//			$("#upload").ajaxSubmit({
//				type : 'POST',    
//				url : '/uploadFile',
//				success : function(result) {
//					if(result.state=='0'){  
//						$("#imgDiv").empty();  
//						$("#imgDiv").html('<img src="'+result.data+'" style="width:137px;height:127px;"/>');  
//						$("#imgDiv").show();  
//						$("#uploadimage").val("");  
//					}else{  
//						alert(result.msg+":"+result.data);  
//					}  
//				},    
//				error : function() {    
//					alert("上传失败，请检查网络后重试,上传文件太大");    
//				}    
//			});  
//		}  
//	});
//});

function uploadLogo(){
	if(confirm("确认上传？")){  
        var imagePath = $("#uploadimage").val();    
        if (imagePath == "") {    
            alert("please upload image file",2);  
            return false;    
        }    
        var strExtension = imagePath.substr(imagePath.lastIndexOf('.') + 1);    
        if (strExtension!='jpg') {   
            if (strExtension!='bmp') {
                if (strExtension!='png') {
                    alert("please upload file that is a image",2);
                    return false;   
                }  
            }  
        }  
        $("#upload").ajaxSubmit({
            type : 'POST',    
            url : '/user/logout',
            success : function(result) {
                if(result.state=='0'){  
                    $("#imgDiv").empty();
                    $("#imgDiv").html('<img src="'+result.data+'" style="width:137px;height:127px;"/>');
                    $("#imgDiv").show();  
                    $("#uploadimage").val("");  
                }else{  
                    alert(result.msg+":"+result.data);  
                }  
            },    
            error : function() {    
                alert("上传失败，请检查网络后重试,上传文件太大");    
            }    
        });  
    }  
}
