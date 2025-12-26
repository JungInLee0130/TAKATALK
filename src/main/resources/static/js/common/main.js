/* ajax 요청시 자동으로 header에 csrf 토큰 삽입 */
$(function () {
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(csrfHeader, csrfToken);
    });
})

$(document).ajaxError(function (event, jqXHR, ajaxSettings, thrownError) {
    if (jqXHR.status === 401) {
        alert("세션이 만료되었습니다. 다시 로그인해주세요.");
        location.href = "/login";
    }
})