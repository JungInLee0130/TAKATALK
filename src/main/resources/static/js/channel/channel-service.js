import axios from "axios";
export const ChannelService = {
    /* 채팅 리스트 불러오기 */
    async enterChannel(groupId, channelId) {
        const response = await axios.get(`/group/${groupId}/channel/${channelId}`);
        return response.data; // axios는 .data에 실제 응답 내용이 들어있음.
    },

    async createChannel(groupId, channelData) {
        const response = await axios.post(`/group/${groupId}/channel/create`, channelData);
        return response.data;
    },
}