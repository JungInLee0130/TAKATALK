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

    async joinGroup(groupId, inviteCode) {
        const response = axios.post(`/group-member/join`, {
            inviteCode: inviteCode
        });
        return response.data;
    }
}