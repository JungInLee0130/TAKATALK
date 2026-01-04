let selectedChannelId = null;

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

    /* 채널 아이템 클릭 시 : 채널입장 */
    document.addEventListener('click', function (event) {
        const channelTarget = event.target.closest(".channel-item");
        if (!channelTarget) return;

        selectedChannelId = channelTarget.getAttribute('data-channel-id');
        console.log(selectedChannelId);

        ChannelService.getOldMessage(selectedChannelId, initChatArea, handleAjaxError);
    })
});