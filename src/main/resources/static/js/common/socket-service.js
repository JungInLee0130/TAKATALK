/** 네트워크 통신만 담당합니다! **/
export const SocketService = {
    stompClient : null, // export시 싱글톤으로 작동

    initConnection() {
        if (this.stompClient && this.stompClient.connected) return;
        const socket = new SockJS('/ws-stomp'); // SockJS 철자주의
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ', frame);
        }), (error) => {
            callback.onError?.(error);
        };
    },
}