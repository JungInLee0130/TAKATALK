/* 채널 접속시 channelId 할당*/

let stompClient = null;
const chatBox = document.getElementById('chatBox');
let isFetching = false; // 중복 요청 방지
let firstMessageId;
chatBox.scrollTop = chatBox.scrollHeight;

/*
* 돔 렌더링 모두 완료되고 실행
* */
$(document).ready(function () {
    /* 무한 스크롤 로직 */
    const handleScroll = function () {
        // scroll이 맨위에 닿았을때
        // 스크롤이 한 중간쯤 왔을때?
        // maxScrollHeight = scrollHeight - clientHeight
        if (chatBox.scrollTop <= 10) {
            console.log("현재 스크롤 위치 : ", chatBox.scrollTop);
            fetchOldMessages();
        }
    }

    /* 스크롤 이벤트 등록 */
    chatBox.addEventListener('scroll', handleScroll);

    /* 이전 메시지 가져오기 */
    function fetchOldMessages() {
        console.log("isFetching : ", isFetching);
        if (isFetching) {
            return;
        }
        isFetching = true;
        /* 초기 데이터 가장 top data 아이디 기억 */
        if (firstMessageId == null) {
            const topMessage = $(".chatmessage-wrapper").first();
            if (topMessage.length > 0) {
                firstMessageId = topMessage.data("id");
            }
        }

        console.log("상단 ID: ", firstMessageId);

        // 1. 데이터를 부르기전, 현재 스크롤 높이 저장
        const oldScrollHeight = chatBox.scrollHeight;
    }

    $.ajax({
        url: `/chatmessage/history/${currentChannelId}`,
        data: {
            lastMessageId: firstMessageId
        },
        success : function (data) {
            if (data.length > 0) {
                // 2. 받아온 데이터를 반복문을 돌려서 HTML 생성
                // 3. chatBox의 가장 윗부분(prepend)에 갖다 붙임.
                data.slice().reverse().forEach((msg) => {
                    renderOldMessages(msg);
                });
                
                // 3-1. 환영메시지 출력 여부
                if (data.length < 20) {
                    visibleWelcomeMessage(data);
                }

                // 4. [핵심] 스크롤 위치 보정
                // (새로 늘어난 높이 - 아까 저장한 높이) 만큼 스크롤을 아래로 내린다.
                chatBox.scrollTop = chatBox.scrollHeight - oldScrollHeight;

                // 5. 가장 위의 메시지 아이디 갱신
                firstMessageId = data[0].chatMessageId;
                isFetching = false;
            } else {
                console.log("더이상 과거 메시지가 없습니다.");
                visibleWelcomeMessage();
                firstMessageId = null;
            }
        }
    });

    function renderOldMessages(data) {
        /* type: TALK + ENTER*/
        let oldMessage;

        if (data.type == 'TALK') {
            oldMessage = `
        <div class="chatmessage-talk-wrapper" data-id="${data.chatMessageId}">
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
        $("#chatBoxBody").prepend(oldMessage);
    }



    /* chatInput 엔터 클릭시 */
    document.getElementById("chatInput").addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            sendMessage();
        }
    });

    /* 전송버튼 클릭시 */
    document.getElementById("sendMsgBtn").onclick = () => {
        sendMessage();
    }

    /* 소켓연결 */
    if (currentChannelId) {
        console.log("채널에 접속하여 소켓을 연결합니다. ID : ", currentChannelId);
        connect();
    } else {
        console.log("현재 선택된 채널이 없습니다.");
    }

    /* 소켓 연결 함수 */
    function connect() {
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
            // status == 401 : location.href = "/login"
        });
    }

    function updateVisitorCount(count) {
        $("#onlineCount").text(`온라인-${count}`);
    }

    function renderVisitorList(visitors) {
        const listHtml = visitors.map(user => {
            return `
          <div class="visitor-item" data-id="${user.siteUserId}">
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
    function sendMessage() {
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
    /**/
    function joinServer() {
        const inviteCode = $("#inviteCodeInput").val().trim();

        if (!inviteCode) {
            alert("초대코드를 입력하세요.");
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/group/join',
            contentType: 'application/json',
            data: JSON.stringify({
                inviteCode: inviteCode
            }),
            success: function (response) {
                alert(response.message);
                location.reload();  // 성공시 새로고침하여 왼쪽바에 새 서버 아이콘 표시
            },
            error : function (xhr) {
                const errorMsg = xhr.responseJSON ? xhr.responseJSON.message : "가입에 실패했습니다.";
                alert(errorMsg);
            }
        });
    }
});

/* 카테고리 생성 */
function createCategory(groupId) {
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
                    location.href = newLocation;
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
function createChannel(groupId, categoryId) {
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
                let newLocation = jqXHR.getResponseHeader('Location');
                if (newLocation) {
                    console.log(newLocation);
                    window.location.href = newLocation;
                } else {
                    console.log("Location 헤더를 찾을수 없습니다.")
                }
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

/* 채널 접속시 채팅 메시지 출력 */
function initChatArea(htmlResponse) {
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