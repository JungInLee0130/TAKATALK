import {
    deleteGroup,
    openCreateGroupModal,
    openEditGroupModal,
    openInviteGroupModal,
    openInviteManageModal
} from "../group/group-controller.js";

export const UiController = {
    init() {
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

        /** 채널 드롭다운 클릭시 보이게 **/
        const channelDropdowns = document.querySelectorAll(".categorized-channel-dropdown");
        //onclick="toggleDropdown(event, this)"
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

        /* context-menu 클릭시 */
        /* groupContextMenu 클릭시 */
        document.getElementById("editGroupMenu").addEventListener('click', openEditGroupModal);
        document.getElementById("deleteGroupMenu").addEventListener('click', deleteGroup);
        document.getElementById("createGroupMenu").addEventListener('click', openCreateGroupModal);
        document.getElementById("joinGroupMenu").addEventListener('click', function () {
            openModal("inviteJoinModal");
        });

        /* channelListContextMenu 클릭시 */
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
export function openModal(element) {
    const modal = element.closest('.modal');
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
export function closeAllModals() {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        modal.style.display = 'none';
    })
}

