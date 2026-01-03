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

