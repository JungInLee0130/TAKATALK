import {ChannelService} from "./channel-service.js";
import {ChatController, createChannel, initChatArea} from "../chat/chat-controller.js";

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
        document.getElementById("submitChannelBtn").onclick = () => {
            createChannel(currentGroupId, currentCategoryId);
        }

        /* 채널 아이템 클릭 시 : 채널입장 */
        document.addEventListener('click', async function (event) {
            const channelTarget = event.target.closest(".channel-item");
            if (!channelTarget) return;

            const clickedId = channelTarget.getAttribute('data-channel-id');
            try {
                const html = await ChannelService.getChatList(clickedId);
                initChatArea(html);
                ChatController.enterChannel(clickedId);
            } catch (error) {
                console.error("채팅 내역 로딩중 에러 : ", error);
            }
        })
    }
}