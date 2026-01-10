import {ChatService} from "./chat-service.js";

let stompClient = null;
let chatBox = null;   // 초기값은 null로 설정
let isFetching = false; // 채팅 내역 조회 상태 저장
let firstMessageId = null;  // 채팅 상단 아이디 상태 저장
let currentChannelId = null;    // 채널입장시 파일 전역변수 저장

/* 채널 접속시 channelId 할당*/
// 이건 기본적으로 채널 접속시 실행되어야함.
export const ChatController = {
    // 1. 처음 DOM 생성시 : 한번만 실행
    init() {
        /* 채팅 메시지 전송 */
        const mainChatWrapper = document.getElementById("mainChatWrapper");
        mainChatWrapper.addEventListener('click', event => {
            /* 1. 전송버튼 클릭시 */
            if (event.target.closest("#sendMsgBtn")) {
                sendMessage(currentChannelId);  // 클릭되는 시점에 추가가능
            }
        })
        mainChatWrapper.addEventListener('keydown', event => {
            if (event.target.closest("#chatInput")) {
                /* 2. chatInput 엔터 클릭시 */
                if (event.key === "Enter") {
                    sendMessage(currentChannelId);
                }
            }
        })
        /** 스크롤 이벤트 : init에 한번만 등록 **/
        mainChatWrapper.addEventListener('scroll', event => {
            if (event.target.id === 'chatBox') {
                handleScroll(currentChannelId);
            }
        }, true);
    },
    // 2. 채널 접속시
    enterChannel(channelId) {
        ChatController.resetChatState(channelId);
        chatBox = document.getElementById('chatBox');
        if (chatBox) {
            chatBox.scrollTop = chatBox.scrollHeight; // 스크롤 맨 아래로
        }

        // 소켓 연결
        if (channelId) {
            console.log("채널에 접속하여 소켓을 연결합니다. ID : ", channelId);
            connect(channelId);
        } else {
            console.log("현재 선택된 채널이 없습니다.");
        }
    },
    resetChatState(channelId) {
        currentChannelId = channelId;
        isFetching = false;
        firstMessageId = null;  // 채널 접속시 상단 ID 초기화
        console.log("채팅 상태 초기화 완료");
    }
}

/* 무한 스크롤 로직 */
const handleScroll = function (currentChannelId) {
    // scroll이 위쪽 끝에서 10px 아래에 도달했을때
    if (chatBox.scrollTop <= 10) {
        console.log("현재 스크롤 위치 : ", chatBox.scrollTop);
        fetchOldMessages(currentChannelId); // message 가져오기
    }
}

/* 이전 메시지 가져오기 */
async function fetchOldMessages(currentChannelId) {
    console.log("isFetching : ", isFetching);
    console.log("currentChannelId : ", currentChannelId);
    console.log("상단 ID: ", firstMessageId);
    if (isFetching) {
        // 비동기 : 이미가져오는 중일때 return
        return;
    }
    isFetching = true; // 가져오는 중
    /* 1. 첫번째 메시지 위치 기록 */
    if (firstMessageId == null) {
        // querySelector : 가장 첫번째만 가져옴
        const firstMessage = document.querySelector('.chatmessage-wrapper');
        if (firstMessage) {
            // 최상단 아이디 저장
            firstMessageId = firstMessage.getAttribute('data-chatmessage-id');
        }
    }

    // 1. 데이터를 부르기전, 현재 스크롤 높이 저장
    const oldScrollHeight = chatBox.scrollHeight;

    try {
        // 2. oldMessage 가져오기
        const chatHistory = await ChatService.getOldMessage(currentChannelId, firstMessageId);

        if (chatHistory) {
            console.log(chatHistory);
            // 1. 이전 채팅 메시지가 존재하면
           if (chatHistory.length > 0) {
               // 2. 받아온 데이터를 반복문을 돌려서 HTML 생성
               chatHistory.slice().reverse().forEach((msg) => {
                   renderOldMessages(msg);
               });
               // 3. 환영메시지 출력 여부
               if (chatHistory.length < 20) {
                   visibleWelcomeMessage();
               }
               // 4. [핵심] 스크롤 위치 보정
               // (새로 늘어난 높이 - 아까 저장한 높이) 만큼 스크롤을 아래로 내린다.
               chatBox.scrollTop = chatBox.scrollHeight - oldScrollHeight;

               // 5. 가장 위의 메시지 아이디 갱신
               firstMessageId = chatHistory[0].chatMessageId;
           }
           else {
               // 채팅메시지 끝 도달 : 환영메시지까지 출력
               visibleWelcomeMessage();
               console.log("더이상 과거 메시지가 없습니다.");
           }
        }
    } catch (error) {
        // 에러 : 상단 메시지 초기화(다시 출력)
        firstMessageId = null;
        console.error("채팅 목록 로딩 에러 : ", error);
    } finally {
        // 6. 가져오기 완료
        isFetching = false;
    }
}

function renderOldMessages(data) {
    /* type: TALK + ENTER*/
    let oldMessage;

    if (data.type == 'TALK') {
        oldMessage = `
    <div class="chatmessage-talk-wrapper" data-chatmessage-id="${data.chatMessageId}">
        <div class="chatmessage-profile-wrapper">
          <img class="chatmessage-profile" src="${data.profile}">
        </div>
        <div class="chatmessage-second-wrapper">
          <div class="chatmessage-upper-wrapper">
            <div class="chatmessage-nickname-wrapper">
              <p class="chatmessage-nickname">${data.nickname}</p>
            </div>
            <div class="chatmessage-createdat-wrapper">
              <p class="chatmessage-createdat">${data.createdAt}</p>
            </div>
          </div>
          <div class="chatmessage-content-wrapper">
            <p class="chatmessage-content">${data.content}</p>
          </div>
        </div>
      </div>
    `;
    } else if (msg.type == 'ENTER'){
        oldMessage = `
      <div className="system-message-wrapper">
          <p>-> <span style="font-weight: bold"> ${data.nickname} </span><span>${data.content}</span></p>
      </div>
    `;
    }
    // 3. chatBox의 가장 윗부분(prepend)에 갖다 붙임.
    $("#chatBoxBody").prepend(oldMessage);
}

/* 소켓 연결 함수 */
function connect(currentChannelId) {
    const socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // 1. 구독
        stompClient.subscribe('/sub/channel/' + currentChannelId, function (response) {
            const message = JSON.parse(response.body);
            if (message.type === "ENTER") {
                renderSystemMessage(message);
            } else if (message.type === "TALK"){
                renderMessage(message);
            } else if (message.type === "EXIT") {
                //renderSystemMessage(message);
            }
        });

        // 2. [추가] 접속자 목록 구독
        stompClient.subscribe('/sub/channel/' + currentChannelId + '/visitors', function (response) {
            const visitors = JSON.parse(response.body);
            renderVisitorList(visitors);
            updateVisitorCount(visitors.length);
        })
    }, function (error) {
        console.log('Error: ' + error);
        // status == 401 : window.location.href = "/login"
    });
}

function updateVisitorCount(count) {
    $("#onlineCount").text(`온라인-${count}`);
}

function renderVisitorList(visitors) {
    const listHtml = visitors.map(user => {
        return `
      <div class="visitor-item" data-user-id="${user.siteUserId}">
          <div class="visitor-image-wrapper">
              <img src="${user.profile}" class="visitor-profile">
              <div class="green-dot"></div>
          </div>
          <p>${user.nickname}</p>
      </div>
  `
    }).join('');

    $(".online-visitorlist-wrapper").html(listHtml);
}

function renderSystemMessage(data) {
    if (data.type === "ENTER") {
        const html = `
    <div class="system-message-wrapper">
        <p>-><span style="font-weight: bold">${data.nickname}</span> ${data.content}</p>
    </div>
    `;

        $("#chatBoxBody").append(html);
        chatBox.scrollTop = chatBox.scrollHeight;
    } else if (data.type === "EXIT"){

    }
}

/*
* response rendering 함수
* */
function renderMessage(data) {
    const inputHtml =`
       <div class="inputmessage-wrapper" style="display:flex;">
        <div class="chatmessage-profile-wrapper">
          <img class="chatmessage-profile" src="${data.profile}">
        </div>
        <div class="chatmessage-second-wrapper">
          <div class="chatmessage-upper-wrapper">
            <div class="chatmessage-nickname-wrapper">
              <p class="chatmessage-nickname">${data.nickname}</p>
            </div>
            <div class="chatmessage-createdat-wrapper">
              <p class="chatmessage-createdat">${data.createdAt}</p>
            </div>
          </div>
          <div class="chatmessage-content-wrapper">
            <p class="chatmessage-content">${data.content}</p>
          </div>
        </div>
      </div>
    `;

    /*아래서부터 위로 append*/
    $("#chatBoxBody").append(inputHtml);

    /*스크롤 맨아래로*/
    chatBox.scrollTop = chatBox.scrollHeight;
}

/* sendMessage 함수 */
function sendMessage(currentChannelId) {
    const inputBox = document.getElementById("chatInput");
    if (!inputBox.value || !stompClient) return;

    // payload
    const chatRequest = {
        channelId : currentChannelId,
        content : inputBox.value,
    }
    stompClient.send("/pub/chatmessage/save", {}, JSON.stringify(chatRequest));
    /*inputBox 초기화*/
    inputBox.value = '';
}

/* 파일 업로드 함수 */
function uploadFile() {
    alert('upload!')
}
/* 사용자 : 서버 참여*/
async function joinServer() {
    const inviteCode = document.getElementById("inviteCodeInput").value.trim();
    //const inviteCode = $("#inviteCodeInput").val().trim();

    if (!inviteCode) {
        alert("초대코드를 입력하세요.");
        return;
    }

    try {
        const response = await ChatService.joinGroup(inviteCode);
        // created URI
        alert(response?.message);
        location.reload();  // 성공시 새로고침하여 왼쪽바에 새 서버 아이콘 표시
    } catch (error) {
        const status = error?.response?.status;
        const errorMsg = error?.response?.data?.message || "가입에 실패했습니다.";
        console.error("서버 참여 에러 : ", status, errorMsg);
        alert(errorMsg);
    }
}

/* 카테고리 생성 */
export function createCategory(groupId) {
    const categoryName = document.getElementById("categoryNameInput").value;
    const isSecret = document.getElementById("isSecretCategoryToggle");

    if (!categoryName) {
        alert("비어있는 항목이 있습니다!");
        return;
    }

    $.ajax({
        type: 'POST',
        url: `/category/create`,
        contentType: 'application/json',
        data: JSON.stringify({
            groupId : groupId,
            categoryName : categoryName,
            isSecret : isSecret ? isSecret.checked : false
        }),
        statusCode: {
            201 : function (data,textStatus,jqXHR) {
                console.log(jqXHR);
                const newLocation = jqXHR.getResponseHeader('Location');
                if (newLocation) {
                    window.location.href = newLocation;
                } else {
                    alert("location is null");
                }
            },
        },
        success : function () {
            console.log("category create success");
        },
        error : function (jqXHR) {
            console.log(jqXHR.status);
            console.log(jqXHR);
        }
    })
}

/*채널 생성*/
export function createChannel(groupId, categoryId) {
    const channelName = document.getElementById("channelNameInput").value;
    const channelType = document.querySelector('input[name = "channelType"]:checked').value;
    const isSecret = document.getElementById("isSecretToggle");

    if (!channelName || !channelType) { // isSecret은 default = false
        alert("비어있는 항목이 있습니다!");
        return;
    }
    /* requestbody */
    const requestBodyData = {
        groupId : groupId,
        categoryId : categoryId,    // currentCategoryId ? categories.id : null
        channelName : channelName,
        channelType : channelType,
        isSecret : isSecret ? isSecret.checked : false
    }

    $.ajax({
        type: "POST",
        url: "/channel/create",
        contentType: "application/Json",
        data: JSON.stringify(requestBodyData),
        statusCode : {
            201 : function (data,textStatus,jqXHR) {
                /*let newLocation = jqXHR.getResponseHeader('Location');
                if (newLocation) {
                    console.log(newLocation);
                    window.location.href = newLocation;
                } else {
                    console.log("Location 헤더를 찾을수 없습니다.")
                }*/
                window.location.reload();
            }
        },
        success: function (response) {
            if (response === "SUCCESS") {
                console.log(response);
            }
        },
        error : function (request, error) {
            // parameter id == null 일때 예외처리
            console.log(request);
        }
    });
}

/* 환영(초기) 메시지를 보이게하는함수 */
function visibleWelcomeMessage() {
    chatBox.removeEventListener('scroll', handleScroll);
    document.querySelector('.chatbox-welcome-wrapper').style.display = "flex";
}

/* 채널 접속시 처음 채팅 메시지 출력 */
export function initChatArea(htmlResponse) {
    // fragment의 부모에 붙이기
    const mainChatWrapper = document.getElementById("mainChatWrapper");

    if (mainChatWrapper) {
        mainChatWrapper.innerHTML = htmlResponse;
        const chatBoxBody = document.getElementById("chatBoxBody");

        if (chatBoxBody) {
            chatBoxBody.scrollTop = chatBoxBody.scrollHeight;   // 스크롤 맨 아래로
        }

        const chatMessages = document.getElementsByClassName('chatmessage-wrapper');
        if (chatMessages.length < 20) {
            visibleWelcomeMessage(chatMessages);
        }
    }
}