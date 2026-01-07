import axios from "axios";
import {CategoryController} from "../category/category-controller.js";
import {ChannelListController} from "../channel/channel-list-controller.js";
import {ChatController} from "../chat/chat-controller.js";
import {ContextMenu} from "./context-menu.js";
import {ApiErrorHandler} from "./service.js";
import {UiController} from "./ui-controller.js";
import {GroupController} from "../group/group-controller.js";
import {InviteController} from "../invite/invite-controller.js";

// [1] : 전역 변수
export let selectedGroupId = null;

document.addEventListener('DOMContentLoaded', () => {
    CategoryController.init();
    ChannelListController.init();
    ContextMenu.init();
    UiController.init();
    GroupController.init();
    InviteController.init();
    ChatController.init();
});

// axios 기본 설정 (CSRF 토큰 자동 포함)
axios.defaults.headers.common[csrfHeader] = csrfToken;
axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

// 인터셉터 설정 : 전역 에러 처리
axios.interceptors.response.use(
    response => response,
    error => {
        const status = error?.response?.status;

        switch (status) {
            case 401:
                alert('세션이 만료되었습니다. 다시 로그인 해주세요.');
                window.location.href = "/login";
                return;
            case 403:
                alert("해당 작업에 대한 권한이 없습니다.");
                break;
            default:
                const errorMsg = error.response?.data?.message || "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
                alert(errorMsg);
                break;
        }
        return Promise.reject(error);
    }
);

/* ajax 요청시 자동으로 header에 csrf 토큰 삽입 */
$(function () {
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(csrfHeader, csrfToken);
    });
})

// 개별 AJAX 요청에서 error 콜백을 따로 작성하지 않으면 호출
$(document).ajaxError(function (event, jqXHR, ajaxSettings, thrownError) {
    ApiErrorHandler.handle(jqXHR);
})