import axios from "axios";

export const ChatService = {
    async getOldMessage(channelId, firstMessageId) {
        const response = await axios.get(`/chatmessage/history/${channelId}`, {
            params : {
                lastMessageId : firstMessageId
            }
        });
        return response.data;
    },

    async joinGroup(inviteCode) {
        const response = axios.post(`/group/join`, {
            inviteCode: inviteCode
        });
        return response.data;
    }
}