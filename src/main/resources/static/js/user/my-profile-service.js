import axios from "axios";

export const MyProfileService = {
    async fetchSiteUserProfileInfo() {
        const response = await axios.get(`/user/profile`);
        return response.data;
    }
}