package com.example.chat.visitor.service;

import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.visitor.entity.Visitors;
import com.example.chat.visitor.repository.VisitorsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VisitorsService {
    private VisitorsRepository visitorsRepository;

    /*public void enterChannel(Long visitorsId, Long channelId) {
        Visitors visitor = visitorsRepository.findById(visitorsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        visitor.getChannels().getId().equals(channelId);
    }*/
}
