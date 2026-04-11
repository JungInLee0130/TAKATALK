import {CategoryService} from "./category-service.js";

export const CategoryController = {
    init() {
        const submitBtn = document.getElementById("submitCategoryBtn");

        if (submitBtn) {
            submitBtn.addEventListener('click', async () => {
                await createCategory(currentGroupId);
            })
        }
    }
}

/* 카테고리 생성 */
export const createCategory = async (groupId) => {
    const categoryName = document.getElementById("categoryNameInput").value;
    const isSecret = document.getElementById("isSecretCategoryToggle");

    if (!categoryName) {
        alert("비어있는 항목이 있습니다!");
        return;
    }

    const createCategoryRequest = {
        categoryName : categoryName,
        isSecret : isSecret ? isSecret.checked : false
    }

    try {
        const response = await CategoryService.createCategory(groupId, createCategoryRequest);
        console.log(response);
        if (response?.status === 201) {
            const newLocation = response?.headers['location'];
            if (newLocation) {
                window.location.href = newLocation;
            } else {
                alert("location is null");
            }
        }
        console.log("category create success");
    } catch (error) {
        console.log(error?.response?.status);
        console.log(error);
    }
}