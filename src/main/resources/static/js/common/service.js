export const ApiErrorHandler = {
    handle: function (xhr) {
        const errorResponse = xhr.responseJSON;
        let message = "알 수 없는 오류가 발생했습니다.";

        // 서버에서 온 에러메시지가 있으면
        if (errorResponse && errorResponse.message) {
            message = errorResponse.message;
        }

        switch (xhr.status) {
            case 401:
                alert("세션이 만료 되었습니다. 다시 로그인 해주세요.");
                window.location.href = "/login";
                return;
            case 403:
                alert("해당 작업에 대한 권한이 없습니다.");
                break;
            case 500:
                alert("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                break;
            default:
                alert(message); // 비즈니스 로직 에러 // 400
        }

        console.error(`[API Error] Status: ${xhr.status}, Message: ${message}`);
    }
}

function handleAjaxError(jqXHR) {
    console.log(jqXHR.status, jqXHR);
}

