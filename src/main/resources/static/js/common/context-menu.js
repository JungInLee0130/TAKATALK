import {selectedGroupId} from "./main.js";

export const ContextMenu = {
    init() {
        // [2] : 이벤트 리스너
        // 사용자 정의 마우스 우클릭 이벤트
        document.addEventListener('contextmenu', function(event) {
            // 1. 그룹 사이드 바 영역
            handleCustomContextMenu(event, '.left-bar', 'groupContextMenu', (clickedEl) => {
                // 클릭한 지점이 그룹 아이콘일경우
                const groupTarget = clickedEl.closest('.group-icon-wrapper');
                console.log(groupTarget);

                if (groupTarget) {
                    selectedGroupId = groupTarget.getAttribute('data-group-id');
                    console.log("selectedGroupId : ", selectedGroupId);
                    toggleGroupMenu(true);
                } else {
                    selectedGroupId = null;
                    toggleGroupMenu(false);
                }
            })

            // 2. 채널 리스트 영역
            handleCustomContextMenu(event, '.left', 'channelListContextMenu');
        });
        document.addEventListener('click', closeAllContextMenus);
        // ESC 닫기 전역 이벤트
        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape') {
                closeAllContextMenus();
            }
        })

        //[3] : 기능 함수
        function toggleGroupMenu(isGroupIconClick) {
            const editItem = document.querySelector('.menu-edit-group');
            const deleteItem = document.querySelector('.menu-delete-group');

            if (editItem && deleteItem) {
                const display = isGroupIconClick ? 'block' : 'none';
                editItem.style.display = display;
                deleteItem.style.display = display;
            }
        }

        /* contextmenu 불러오기 */
        function handleCustomContextMenu(event, targetSelector, menuId, callback) {
            const target = event.target.closest(targetSelector);
            if (!target) return;

            // 1. 브라우저 기본 메뉴 방지
            event.preventDefault();

            // 2. 이전에 열려있는 context-menu 모두 닫는 함수
            closeAllContextMenus();

            const menu = document.getElementById(menuId);
            if (!menu) return;
            const x = event.clientX;
            const y = event.clientY;

            menu.style.display = 'block';
            menu.style.left = x + 'px';
            menu.style.top = y + 'px';

            if (callback) callback(event.target);
        }
    }
}

/** 이전에 열려있는 context-menu 모두 닫는 함수 **/
function closeAllContextMenus() {
    let contextMenus = document.querySelectorAll('.context-menu');
    contextMenus.forEach(menu => {
        if (menu.style.display === 'block') {
            menu.style.display = 'none';
        }
    })
}