import {openModal} from "../common/ui-controller.js";
import {MyProfileService} from "./my-profile-service.js";

export const MyProfile = {
    init() {
        document.getElementById('myProfileWrapper').addEventListener('click', (event) => {
            try {
                const response = MyProfileService.fetchSiteUserProfileInfo();

            } catch (error) {

            }
            openModal('myProfileModal');
        })
    }
}