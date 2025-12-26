let selectedGroupId = null;

$(document).ready(function () {
    // 사용자 정의 마우스 우클릭 이벤트
    document.addEventListener('contextmenu', function(event) {
        const target = event.target.closest('.group-icon-wrapper');

        if (target) {
            event.preventDefault();

            const menu = document.getElementById('groupContextMenu');
            const x = event.clientX;
            const y = event.clientY;

            menu.style.display = 'block';
            menu.style.left = x + 'px';
            menu.style.top = y + 'px';


            selectedGroupId = target.getAttribute('data-group-id');
            console.log("selectedGroupId:", selectedGroupId);
        }
    });

    document.addEventListener('click', function () {
        const menu = document.getElementById('groupContextMenu');
        if (menu) menu.style.display = 'none';
    })
})
