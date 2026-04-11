import axios from "axios";

export const InviteService = {
    async joinGroup(groupId, inviteCode) {
        const response = await axios.post(`/group-member/join`, {
            inviteCode: inviteCode
        });
        return response.data;
    },
    async resetInviteCode(groupId) {
        const response = await axios.post(`/group/${groupId}/invite-code/reset`);
        return response.data;
    },
}