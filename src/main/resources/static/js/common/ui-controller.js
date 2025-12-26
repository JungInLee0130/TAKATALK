/* 모달 열고 닫기 */
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) modal.style.display = "flex";
}
function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) modal.style.display = "none";
}

/* 관리용 모달 열기 */
function openInviteManageModal() {
    if (!currentGroupId) {
        alert('서버를 먼저 선택해주세요.');
        return;
    }

    $.ajax({
        type: 'GET',
        url: `/group/${currentGroupId}/invite-code`,
        success: function (code) {
            document.getElementById("displayInviteCode").value = code;
            openModal("inviteManageModal");
            //document.getElementById("inviteManageModal").style.display = "block";
        },
        error : function (xhr) {
            alert(xhr.responseJSON.message);
        }
    });
}

/* 모달 제어 함수 */
function openInviteGroupModal() {
    openModal('inviteGroupModal');
}
function closeInviteGroupModal() {
    closeModal('inviteGroupModal');
}

/* 초대버튼 클릭시 */
/*document.getElementById("inviteChannelBtn").onclick = () => {
  inviteChannelModal.style.display = 'block';
}*/
/**/
function closeGroupFormModal() {
    closeModal('groupFormModal');
}
/* 모달 제어 함수 */
function closeManageModal() {
    document.getElementById('inviteModal').style.display = 'none';
}

// ESC 닫기 전역 이벤트
$(document).keydown(function(e) {
    if (e.keyCode === 27) {
        $(".modal").hide();
    }
})