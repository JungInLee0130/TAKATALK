import axios from "axios";

export const GroupService = {
    async deleteGroup(groupId) {
        const response = await axios.delete(`/group/${groupId}`);
        return response.data;
    },
    async resetInviteCode(groupId) {
        const response = await axios.post(`/group/${groupId}/invite-code/reset`);
        return response.data;
    },
    async getGroupInfo(groupId) {
        const response = await axios.get(`/group/info/${groupId}`);
        return response.data;
    },
    //async createGroup(group)
    //async updateGroup()
    async getInviteCode(groupId) {
        const response = await axios.get(`/group/${groupId}/invite-code`);
        return response.data;
    }
};