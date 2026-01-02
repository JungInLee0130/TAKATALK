$(document).ready(function () {
    // 이미지 올릴때
    document.getElementById("groupProfileInput").addEventListener('change', function (event) {
        const file = event.target.files[0];
        const preview = document.getElementById('imagePreview');
        const uploadContent = document.querySelector('.upload-content');
        const plusBadge = document.querySelector('.plus-badge');

        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
                preview.style.display = 'block';
                uploadContent.style.visibility = 'hidden';
                plusBadge.style.display = "none";
            }
            reader.readAsDataURL(file);
        }
    })

    // 그룹 삭제 메뉴 클릭시
    document.getElementById("deleteGroupMenu").onclick = () => {
        deleteGroup();
    }
});

// 그룹 삭제
function deleteGroup() {
    if(!selectedGroupId) return;
    
    if (confirm("정말로 이 그룹을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) {
        $.ajax({
            type: 'DELETE',
            url: `/group/${selectedGroupId}`,
            success : function () {
                alert('그룹이 삭제되었습니다.');
                location.href = '/main';
            },
            error : function (jqXHR) {
                alert(jqXHR.responseJSON.message || '삭제 권한이 없습니다.');
            }
        })
    }
}

// 클립보드 복사기능
function copyInviteCode() {
    const codeInput = document.getElementById("displayInviteCode");
    navigator.clipboard.writeText(codeInput.select()) // 해당 요소의 모든 내용을 선택합니다.
}

// 코드 초기화
function resetInviteCode() {
    $.ajax({
        type: 'POST',
        url: `/group/${currentGroupId}/invite-code/reset`,
        success : function (newCode) {
            document.getElementById("displayInviteCode").value = newCode;
            document.getElementById("inviteCodeErrorMsg").style.display = "none";
        },
        error : function (xhr) {
            document.getElementById("inviteCodeErrorMsg").style.display = "block";
            document.getElementById("inviteCodeErrorMsg").textContent = xhr.responseJSON.message;
        }
    });
}

// 그룹 생성 모달
function openCreateGroupModal() {
    // 1. 초기화
    document.getElementById("modalTitle").textContent = "서버 생성하기";
    // 1-2. 그룹 이름
    document.getElementById("groupNameInput").value = "";
    // 1-3. 그룹 프로필
    document.getElementById("imagePreview").style.display = "none";
    document.querySelector('.upload-content').style.visibility = "visible";

    // 2. 버튼 이벤트 바인딩
    const createGroupBtn = document.getElementById("groupSubmitBtn");
    createGroupBtn.textContent = "만들기";

    // 3. 생성버튼 클릭
    createGroupBtn.onclick = function() {
        submitGroupForm("POST", "/group/create");
    };

    openModal('groupFormModal');
}

// 그룹 편집 모달
function openEditGroupModal() {
    // 그룹 조회
    $.ajax({
        type: "GET",
        url: `/group/info/${selectedGroupId}`,
        success : function (response) {
            // 1. 초기화
            // 1-1. h2
            document.getElementById("modalTitle").textContent = "서버 수정하기";
            // 1-2. 그룹 이름
            document.getElementById("groupNameInput").value = response.name;

            // 1-3. 프로필
            document.getElementById("imagePreview").style.display = "block";
            document.getElementById("imagePreview").src = response.profile;
            document.querySelector('.upload-content').style.visibility = "hidden";
            document.querySelector('.plus-badge').style.display = "none";

            // 2. 버튼 이벤트 바인딩
            const groupUpdateBtn = document.getElementById("groupSubmitBtn");
            groupUpdateBtn.textContent = "수정하기";

            // 3. 생성버튼 클릭
            groupUpdateBtn.onclick = function() {
                submitGroupForm("PATCH", `/group/${selectedGroupId}`);
            };

            // 4. groupFormModal 열기
            openModal('groupFormModal');
        },
        error : function (jqXHR) {
            if (jqXHR.responseJSON) {
                const errorMsg = xhr.responseJSON.message || "그룹 단일 조회 실패";
                console.log(errorMsg);
            } else {
                console.log(jqXHR);
            }

        }
    })
}

function submitGroupForm(method, url) {
    const groupName = document.getElementById("groupNameInput").value;
    const fileInput = document.getElementById("groupProfileInput");

    if (!groupName.trim()) {
        alert('서버 이름을 입력해주세요.')
        return;
    }

    console.log(groupName, fileInput.files[0])

    const formData = new FormData();
    formData.append("name", groupName);

    if (fileInput.files.length > 0) {
        formData.append("profile", fileInput.files[0]);
    }

    // 생성, 수정 둘다
    $.ajax({
        type: method,
        url: url,
        data: formData,
        processData: false,
        contentType: false,
        statusCode: {
            201: function (data, textStatus, jqXHR) {
                console.log("그룹이 성공적으로 생성되었습니다.");

                const newLocation = jqXHR.getResponseHeader('Location');

                if (newLocation) {
                    window.location.href = newLocation;
                } else {
                    console.log("Location 헤더를 찾을수없습니다.")
                }

                closeModal('groupFormModal');
            }
        },
        success: function () {
            console.log("그룹이 성공적으로 수정되었습니다.");
            closeModal('groupFormModal');
        },
        error: function (jqXHR) {
            if (jqXHR.status) {
                console.log(jqXHR.status);
            }
            if (jqXHR.responseJSON) {
                const errorMsg = xhr.responseJSON.message || "요청 처리 중 에러가 발생했습니다.";
                console.log(errorMsg);
            } else {
                console.log(error);
            }
        }
    });
}