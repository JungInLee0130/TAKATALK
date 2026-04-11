import axios from "axios";

export const ChatService = {
    async getOldMessage(channelId, firstMessageId) {
        const response = await axios.get(`/api/v1/channel/${channelId}/chatmessage/history`, {
            params : {
                lastMessageId : firstMessageId
            }
        });
        return response.data;
    },
}