import {selectedGroupId} from "../common/main.js";
import {closeModal, openModal} from "../common/ui-controller.js";
import {GroupService} from "./group-service.js";

const groupFormModal = document.getElementById('groupFormModal');

export const GroupController = {
    init() {
        // 이미지 올릴때
        document.getElementById("groupProfileInput").addEventListener('change', (event) => {
            uploadImage(event);
        })
    }
}

const uploadImage = (event) => {
    const file = event.target.files[0];
    const preview = document.getElementById('imagePreview');
    const uploadContent = document.querySelector('.upload-content');
    const plusBadge = document.querySelector('.plus-badge');

    if (!file) return;

    const imageUrl = URL.createObjectURL(file);

    preview.src = imageUrl;
    preview.style.display = 'block';
    uploadContent.style.visibility = 'hidden';
    plusBadge.style.display = 'none';

    // 메모리 해제
    preview.onload = () => {
        URL.revokeObjectURL(imageUrl);
    }
}

// 그룹 삭제
export async function deleteGroup() {
    if(!selectedGroupId) return;
    
    if (confirm("정말로 이 그룹을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) {
        try {
            await GroupService.deleteGroup(selectedGroupId);
            alert('그룹이 삭제되었습니다.');
            window.location.href = '/main';
        } catch (error) {
            alert(error?.response?.data?.message || '삭제 권한이 없습니다.');
        }
    }
}

/** 그룹 생성, 편집 **/
export const handleGroup = {
    createFormData() {
        // 그룹 FormData 생성 : 그룹 이름, 프로필 이미지 request
        const groupName = document.getElementById("groupNameInput").value;
        const fileInput = document.getElementById("groupProfileInput");
        if (groupName.trim()) {
            alert('서버 이름을 입력해주세요.')
            return;
        }
        console.log(groupName, fileInput.files[0])

        const formData = new FormData();    // name, profile
        formData.append("name", groupName);
        if (fileInput.files.length > 0) {
            formData.append("profile", fileInput.files[0]);
        }

        return formData;
    },
    async createGroup() {
        // 그룹 생성
        try {
            const response = await GroupService.createGroup(this.createFormData());
            this.handleSuccess(response, "그룹이 생성되었습니다");
        } catch (error) {
            this.handleError(error, "요청 처리 중 에러가 발생했습니다.");
        }
    },
    async editGroup(groupId) {
        // 그룹 편집
        try {
            const response = await GroupService.editGroup(groupId, this.createFormData());
            this.handleSuccess(response, "그룹이 수정되었습니다");
        } catch (error) {
            this.handleError(error, "요청 처리 중 에러가 발생했습니다.");
        }
    },
    handleSuccess(response, successMsg) {
        if (response?.status === 201) {
            const newLocation = response.headers['location'];
            if (newLocation) {
                window.location.href = newLocation;
            } else {
                console.log("Location 헤더를 찾을수없습니다.")
            }
            closeModal(groupFormModal);
        }
        console.log(successMsg);
    },
    handleError(error, errorMsg) {
        console.error(error);
        console.error(error?.response?.data?.message || errorMsg);
    }
}

/* 관리용 모달 열기2 */
export async function openInviteManageModal() {
    if (!currentGroupId) {
        alert('서버를 먼저 선택해주세요.');
        return;
    }

    try {
        const code = await GroupService.getInviteCode(currentGroupId);
        console.log(code);
        document.getElementById("inviteCode").value = code;
        openModal('inviteManageModal');
    } catch (error) {
        alert(error?.response?.data?.message || "초대 코드를 fetch 오류!");
    }
}

/* 관리용 모달 열기 */
export async function openInviteGroupModal() {
    if (!currentGroupId) {
        alert('서버를 먼저 선택해주세요.');
        return;
    }

    try {
        const code = await GroupService.getInviteCode(currentGroupId);
        console.log(code);
        document.getElementById("inviteCode").value = code;
        openModal('inviteGroupModal');
    } catch (error) {
        alert(error?.response?.data?.message || "초대 코드를 fetch 오류!");
    }
}