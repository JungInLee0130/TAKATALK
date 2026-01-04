const channelCreateModal = document.getElementById('channelCreateModal');
const categoryCreateModal = document.getElementById('categoryCreateModal');
// ESC 닫기 전역 이벤트
document.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
        closeAllModals();
    }
})

document.addEventListener('click', function (event) {
    if (event.target.classList.contains('modal')) {
        event.target.style.display = 'none';
    }
})

/* 채널 드롭다운 클릭시 */
document.addEventListener('click', function (event) {
    const dropdown = document.querySelector(".category-wrapper");

    if (dropdown && !dropdown.contains(event.target)) {
        dropdown.classList.remove('active');
    }
})

function toggleDropdown(event, element) {
    if (event && event.target.closest('.category-plus-icon')) {
        return; // 카테고리 '+' 아이콘 눌렀을경우 실행 X
    }
    const wrapper = element.closest('.category-dropdown');
    wrapper.classList.toggle('active');
}


/* 초대버튼 클릭시 */
/*document.getElementById("inviteChannelBtn").onclick = () => {
  inviteChannelModal.style.display = 'block';
}*/
/**/

/* 모달 제어 함수 */
function openModal(element) {
    const modal = element.closest('.modal');
    if (modal) {
        modal.style.display = "flex";
    }
}
function openChannelCreateModal() {
    openModal(channelCreateModal);
}
function openCategoryCreateModal() {
    openModal(categoryCreateModal);
}
function closeModal(element) {
    const modal = element.closest('.modal');
    if (modal) {
        modal.style.display = "none";
    }
}
function closeAllModals() {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        modal.style.display = 'none';
    })
}

