$(function(){
    $("#file").fileinput({
        uploadUrl:"/user/editUserLogo",
        dropZoneEnabled: false,
        showPreview :true, //是否显示预览
        maxFileCount:1, //表示允许同时上传的最大文件个数
        autoReplace:true,
        minFileSize:1,
        maxFileSize:3072,
        allowedFileExtensions: ['jpg','png']
    });
    $('#file').on('fileuploaded', function(event, data, previewId, index) {
        console.log(data);
        if(data.response.resCode == "00000"){
            $('#updateSuccessModal').modal('show');
        }else{
            $("#errorMsg").html(data.response.resMsg);
            $('#updateFailedModal').modal('show');
        }
    });
});