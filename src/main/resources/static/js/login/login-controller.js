document.addEventListener('DOMContentLoaded', (event) => {
    /*
    * 로그인 버튼 클릭시
    * */

    /*
    * 이메일 검증
    * */
    function validateEmail(email) {
        var emailRegex = new RegExp("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

        if (email == '' || email == null) {
            document.getElementById("emailErrorMsg").innerText = "필수 입력칸이에요.";
            return false;
        } else if (!email.match(emailRegex)) {
            // 이메일 정규표현식 만족못하면
            console.log("이메일 형식을 다시 작성해주세요.")
            document.getElementById("emailErrorMsg").innerText = "이메일 형식을 다시 작성해주세요.";
            return false;
        }
        return true;
    }

    /*
    * 비밀번호 재설정 메일 버튼 클릭시
    * */
    document.getElementById("sendChangePasswordBtn").onclick = () => {
        var email = document.getElementById("email").value;
        console.log(email);

        if(!validateEmail(email)) return;

        /*
        * 유효성 검증 성공시 modal창 활성화
        * */
        modal.style.display = "block";

        $.ajax({
            type: 'POST',
            url: '/login/send-change-password',
            contentType: 'application/json',
            data: JSON.stringify({
                mail : email
            }),
            success: function () {
                document.getElementById("emailErrorMsg").innerText = "";
                document.getElementById("passwordErrorMsg").innerText = "";
                /*if (response === "SUCCESS") {
                    document.getElementById("emailErrorMsg").innerText = "";
                    document.getElementById("passwordErrorMsg").innerText = "";
                } else {
                    alert(response);
                }*/
            },
            error : function (request, error) {
                let errorCode = request.responseJSON.code;
                let errorMsg = request.responseJSON.message;
                console.log(errorCode + ": " + errorMsg);

                switch (errorCode) {
                    case "COMMON_001" :
                        document.getElementById("emailErrorMsg").innerText = "유효하지않는 아이디 또는 비밀번호 입니다.";
                        break;
                    case "MEMBER_001":
                        document.getElementById("emailErrorMsg").innerText = "존재하지 않는 이메일이에요.";
                        document.getElementById("passwordErrorMsg").innerText = "";
                        break;
                    default :
                        // SERVER_ERROR
                        alert("임시 비밀번호 발송에 실패했습니다. 다시 시도해주세요.");
                        document.getElementById("emailErrorMsg").innerText = "임시 비밀번호 발송에 실패했습니다. 다시 시도해주세요.";
                        break;
                }
            }
        });
    };
});