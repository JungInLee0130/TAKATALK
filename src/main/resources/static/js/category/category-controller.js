import {createCategory} from "../chat/chat-controller.js";

export const CategoryController = {
    init() {
        const submitBtn = document.getElementById("submitCategoryBtn");

        if (submitBtn) {
            submitBtn.addEventListener('click', () => {
                createCategory(currentGroupId);
            })
        }
    }
}

