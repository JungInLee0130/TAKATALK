const ChannelService = {
    /* 채팅 리스트 불러오기 */
    getOldMessage : function(channelId, successCallback, errorCallback) {
        $.ajax({
            type: 'GET',
            url: `/channel/${channelId}`,
            success: successCallback,
            error: errorCallback
        });
    }
}