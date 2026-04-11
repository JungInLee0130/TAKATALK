import {InviteService} from "./invite-service.js";

export const InviteController = {
    init() {
        document.getElementById("inviteJoinBtn").addEventListener('click', async () => {
            await joinServer();
        })
        document.getElementById("copyInviteCodeBtn").addEventListener('click', async () => {
            await copyInviteCode();
        })
        document.getElementById("resetInviteCodeBtn").addEventListener('click', async () => {
            await resetInviteCode();
        })
    }
}

// 코드 초기화
function resetInviteCode() {
    try {
        const newCode = InviteService.resetInviteCode(currentGroupId);
        document.getElementById("displayInviteCode").value = newCode;
        document.getElementById("inviteCodeErrorMsg").style.display = "none";
    } catch(error) {
        document.getElementById("inviteCodeErrorMsg").style.display = "block";
        document.getElementById("inviteCodeErrorMsg").textContent = error?.response?.data?.message || "코드 초기화 실패!";
    }
}

// 클립보드 복사기능
const copyInviteCode = async () => {
    const codeInput = document.getElementById("displayInviteCode");
    await navigator.clipboard.writeText(codeInput.select()) // 해당 요소의 모든 내용을 선택합니다.
}

/* 사용자 : 서버 참여*/
const joinServer = async () => {
    const inviteCode = document.getElementById("inviteCodeInput").value.trim();
    if (!inviteCode) {
        alert("초대코드를 입력하세요.");
        return;
    }

    try {
        const response = await InviteService.joinGroup(inviteCode);
        // created URI
        alert(response?.message);
        location.reload();  // 성공시 새로고침하여 왼쪽바에 새 서버 아이콘 표시
    } catch (error) {
        const status = error?.response?.status;
        const errorMsg = error?.response?.data?.message || "가입에 실패했습니다.";
        console.error("서버 참여 에러 : ", status, errorMsg);
        alert(errorMsg);
    }
}