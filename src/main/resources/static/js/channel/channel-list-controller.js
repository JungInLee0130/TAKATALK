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

        if (channelTarget) {
            selectedChannelId = channelTarget.getAttribute('data-channel-id');
            console.log(selectedChannelId);

            $.ajax({
                type: 'GET',
                url: `/channel/${selectedChannelId}`,
                success: function (response) {
                    const chatBoxBody = document.getElementById("chatBoxBody");

                    if (chatBoxBody) {
                        chatBoxBody.innerHTML = response;
                        chatBoxBody.scrollTop = chatBoxBody.scrollHeight;
                    }
                },
                error: function (jqXHR) {
                    console.log(jqXHR.status, jqXHR);
                }

            });
        }
    })
});