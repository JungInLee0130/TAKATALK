import axios from "axios";

export const CategoryService = {
    async createCategory(groupId, createCategoryRequest) {
        const response = await axios.post(`/group/${groupId}/category/create`, createCategoryRequest);
        return response.data;
    },
}