$(document).ready(function () {
    /*채널 유형 선택시*/
    let typeItem = document.querySelectorAll('.type-item');
    typeItem.forEach(item => {
        item.addEventListener('click', (e) => {
            typeItem.forEach(i => i.classList.remove('selected'));
            e.currentTarget.classList.add('selected');
        })
    });

    document.getElementById("submitChannelBtn").onclick = () => {
        createChannel(currentGroupId, currentCategoryId);
    }
});