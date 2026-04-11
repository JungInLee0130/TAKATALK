import {ChannelService} from "./channel-service.js";
import {ChatController, initChatArea} from "../chat/chat-controller.js";

export const ChannelListController = {
    init() {
        /*채널 유형 선택시*/
        let typeItem = document.querySelectorAll('.type-item');
        typeItem.forEach(item => {
            item.addEventListener('click', (e) => {
                typeItem.forEach(i => i.classList.remove('selected'));
                e.currentTarget.classList.add('selected');
            })
        });

        /* 채널 생성 버튼 클릭 시 */
        document.getElementById("submitChannelBtn").addEventListener('click', async () => {
            await createChannel(currentGroupId, currentCategoryId);
        });

        /* 채널 아이템 클릭 시 : 채널입장 */
        document.addEventListener('click', async function (event) {
            const channelTarget = event.target.closest(".channel-item");
            if (!channelTarget) return;

            const clickedId = channelTarget.getAttribute('data-channel-id');
            try {
                const html = await ChannelService.enterChannel(currentGroupId, clickedId);
                initChatArea(html);
                ChatController.enterChannel(clickedId);
            } catch (error) {
                console.error("채팅 내역 로딩중 에러 : ", error);
            }
        })
    }
}

/*채널 생성*/
export const createChannel = async (groupId, categoryId) => {
    const channelName = document.getElementById("channelNameInput").value;
    const channelType = document.querySelector('input[name = "channelType"]:checked').value;
    const isSecret = document.getElementById("isSecretToggle");

    if (!channelName || !channelType) { // isSecret은 default = false
        alert("비어있는 항목이 있습니다!");
        return;
    }

    const channelData = {
        groupId : groupId,
        categoryId : categoryId,    // currentCategoryId ? categories.id : null
        channelName : channelName,
        channelType : channelType,
        isSecret : isSecret ? isSecret.checked : false
    }

    try {
        await ChannelService.createChannel(groupId, channelData);
        window.location.reload();
    } catch(error) {
        console.error(error);
    }
}