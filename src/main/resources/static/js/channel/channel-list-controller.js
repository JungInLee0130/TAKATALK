$(document).ready(function () {
    let typeItem = document.querySelectorAll('.type-item');

    typeItem.forEach(item => {
        item.addEventListener('click', (e) => {
            typeItem.forEach(i => i.classList.remove('selected'));
            e.currentTarget.classList.add('selected');
        })
    })
});

function createCategoryModal() {
    // 1.
}