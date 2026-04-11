import axios from "axios";

export const GroupService = {
    async deleteGroup(groupId) {
        const response = await axios.delete(`/group/${groupId}`);
        return response.data;
    },
    async getGroupInfo(groupId) {
        const response = await axios.get(`/group/info/${groupId}`);
        return response.data;
    },
    async createGroup(request) {
        const response = await axios.post(`/group/create`, request);
        return response;
    },
    async editGroup(groupId, request) {
        const response = await axios.patch(`/group/${groupId}`, request);
        return response.data;
    },
    async getInviteCode(groupId) {
        const response = await axios.get(`/group/${groupId}/invite-code`);
        return response.data;
    }
};