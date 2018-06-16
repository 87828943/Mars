function register(){
    var name = $("#name").val();
    var email = $("#email").val();
    var password = $("#password").val();

    $.ajax({
        url: "/user/register",
        type: "POST",
        data: {
            name: name,
            email: email,
            password: password
        },
        success: function(res) {
            if(res.resCode == "00000"){
                window.location.href="/";
            }
        },
        error: function() {
        }
    });
}