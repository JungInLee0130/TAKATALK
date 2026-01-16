package com.example.chat.channel.controller;

import com.example.chat.channel.dto.ChannelCreateResponse;
import com.example.chat.channel.dto.ChannelEditRequest;
import com.example.chat.channel.dto.ChannelEditResponse;
import com.example.chat.channel.dto.CreateChannelRequest;
import com.example.chat.channel.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Channel Api", description = "(그룹-(카테고리)-채널)로 이어지는 채널 Api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group/{groupId}/channel")
public class ApiChannelController {
    private final ChannelService channelService;

    
    @Operation(summary = "채널 생성", description = "채널을 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<ChannelCreateResponse> createChannel(@PathVariable(name = "groupId") Long groupId,
                                              @PathVariable(name = "categoryId", required = false) Long categoryId,
                                              @Valid @RequestBody CreateChannelRequest request){
        ChannelCreateResponse response = channelService.createChannel(groupId, categoryId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(summary = "채널 수정", description = "채널을 수정합니다.")
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelEditResponse> editChannel(@PathVariable(name = "groupId") Long groupId,
                                              @PathVariable(name = "categoryId", required = false) Long categoryId, // 채널의 카테고리를 옮길때
                                              @PathVariable(name = "channelId") Long channelId,
                                              @Valid @RequestBody ChannelEditRequest request){
        ChannelEditResponse response = channelService.editChannel(groupId, categoryId, channelId, request);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "채널 삭제", description = "채널을 삭제합니다.")
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable(name = "groupId") Long groupId,
                                              @PathVariable(name = "channelId") Long channelId){
        channelService.deleteChannel(groupId, channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
