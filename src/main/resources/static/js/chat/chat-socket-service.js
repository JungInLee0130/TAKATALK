/** 구독, 채팅 send **/
import {SocketService} from "../common/socket-service.js";

export const ChatSocketService = {
    // 소켓 연결 및 구독 로직
    subscribeChannel(channelId, callback) {
        // 1. 채팅 메시지 구독
        SocketService.stompClient.subscribe(`/sub/channel/${channelId}`, (response) => {
            const message = JSON.parse(response.body);
            callback.onMessageReceived?.(message); // callback함수가 onMessageReceived일때 : 실행
        });

        // 2. 접속자 목록 구독
        SocketService.stompClient.subscribe(`/sub/channel/${channelId}/visitors`, (response) => {
            const visitors = JSON.parse(response.body);
            callback.onVisitorsUpdated?.(visitors);
        });
    },

    sendMessage(channelId, content) {
        if (!SocketService.stompClient || !SocketService.stompClient.connected) {
            console.error("소켓이 연결되지않았습니다.");
            return;
        }
        if (!content || content.trim() === '') return;

        // payload
        const chatRequest = {
            channelId : channelId,
            content : content,
        }
        SocketService.stompClient.send(`/pub/chatmessage/save`, {}, JSON.stringify(chatRequest));
    }
}