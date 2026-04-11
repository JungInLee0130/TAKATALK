import {
    deleteGroup, handleGroup,
    openInviteGroupModal,
    openInviteManageModal
} from "../group/group-controller.js";
import {GroupService} from "../group/group-service.js";
import {selectedGroupId} from "./main.js";

export const UiController = {
    init() {
        /** 채널 드롭다운 클릭시 보이게 **/
        const channelDropdowns = document.querySelectorAll(".categorized-channel-dropdown");
        if (channelDropdowns) {
            channelDropdowns.forEach(dropdown => {
                dropdown.addEventListener('click', function(event) {
                    if (!dropdown.contains(event.target)) {
                        dropdown.classList.remove('active');
                        return;
                    }
                    if (event && event.target.closest('.category-plus-icon')) {
                        // 카테고리 '+' 아이콘 눌렀을경우 : 채널 생성 모달 염
                        openModal('channelCreateModal');
                        return;
                    }
                    const wrapper = event.target.closest('.category-dropdown');
                    wrapper.classList.toggle('active');
                })
            })
        }

        /** 그룹 생성 + 아이콘 클릭시 **/
        document.getElementById("GroupCreateBtn").addEventListener('click', async (event) => {
            openModal("groupCreateModal");
        })

        /** Modal 공통처리 **/
        /** openModal **/
        document.addEventListener('click', (event) => {
            const openBtn = event.target.closest('[data-open="modal"]');
            if (openBtn) {
                openModal(openBtn);
            }
        })
        /** close **/
        document.addEventListener('click', (event) => {
            const closeBtn = event.target.closest('[data-close="modal"]');
            if (closeBtn) {
                closeModal(closeBtn);
            }
        })

        /** ESC 버튼 클릭시 모달 전체 닫기 **/
        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape') {
                closeAllModals();
            }
        })

        /** 모달 바깥 클릭시 닫기 **/
        document.addEventListener('click', function (event) {
            if (event.target.classList.contains('modal')) {
                event.target.style.display = 'none';
            }
        })

        /** context-menu 클릭시 **/
        /** groupContextMenu내 메뉴 클릭시 **/
        document.getElementById("editGroupMenu").addEventListener('click', async () => {
            await openEditGroupModal();
        });
        document.getElementById("deleteGroupMenu").addEventListener('click', deleteGroup);
        document.getElementById("createGroupMenu").addEventListener('click', async () => {
            await openCreateGroupModal();
        })
        document.getElementById("joinGroupMenu").addEventListener('click', function () {
            openModal("inviteJoinModal");
        });

        /** channelListContextMenu내 메뉴 클릭시 **/
        document.getElementById("createCategoryMenu").addEventListener('click', function () {
            openModal('categoryCreateModal');
        });
        document.getElementById("createChannelMenu").addEventListener('click', function () {
            openModal('channelCreateModal');
        });
        document.getElementById("inviteGroupMenu").addEventListener('click', openInviteGroupModal);
        document.getElementById("inviteManageMenu").addEventListener('click', openInviteManageModal);
    }
}

/* 모달 제어 함수 */
export function openModal(idOrElement) {
    let modal;

    if (typeof idOrElement === 'string') {
        // ID 값
        modal = document.getElementById(idOrElement);
    } else {
        // HTML 요소
        modal = idOrElement.closest('.modal');
    }
    if (modal) {
        modal.style.display = "flex";
    }
}
export function closeModal(element) {
    const modal = element.closest('.modal');
    if (modal) {
        modal.style.display = "none";
    }
}
export const closeAllModals = () => {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        modal.style.display = 'none';
    })
}

export const setCreateOrEditGroupEvent = (addEvent, removeEvent) => {
    const createGroupBtn = document.getElementById("groupSubmitBtn");
    // 그룹 생성
    createGroupBtn.removeEventListener('click', removeEvent);
    createGroupBtn.addEventListener('click', addEvent);
}

export const openCreateGroupModal = () => {
    setCreateOrEditGroupEvent(handleGroup.createGroup, handleGroup.editGroup);
    openModal("groupCreateModal");
}

// 그룹 편집 모달 UI
export const openEditGroupModal = async () => {
    try {
        // 그룹 조회
        const response = await GroupService.getGroupInfo(selectedGroupId);
        setEditGroupUI(response, selectedGroupId);
        openModal("groupFormModal"); // groupFormModal 열기
    } catch (error) {
        handleGroup.handleError(error, "그룹 단일 조회 실패")
    }
}

const setEditGroupUI = (response, groupId) => {
    // 1. 초기화
    // 1-1. h2
    document.getElementById("modalTitle").textContent = "서버 수정하기";
    // 1-1. 그룹 이름
    document.getElementById("groupNameInput").value = response.name;
    // 1-3. 프로필
    document.getElementById("imagePreview").style.display = "block";
    document.getElementById("imagePreview").src = response.profile;
    document.querySelector('.upload-content').style.visibility = "hidden";
    document.querySelector('.plus-badge').style.display = "none";
    const groupEditBtn = document.getElementById("groupSubmitBtn");
    groupEditBtn.textContent = "수정하기";
    setCreateOrEditGroupEvent(handleGroup.editGroup(groupId), handleGroup.createGroup);
}