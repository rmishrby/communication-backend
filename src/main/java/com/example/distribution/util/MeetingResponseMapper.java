package com.example.distribution.util;

import com.example.distribution.dto.MeetingResponseDto;
import com.example.distribution.entity.Meeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MeetingResponseMapper {

    private static final Logger logger = LoggerFactory.getLogger(MeetingResponseMapper.class);

    public static MeetingResponseDto toResponseDto(Meeting meeting) {
        if (meeting == null) {
            logger.warn("Attempted to map null Meeting entity to DTO.");
            return null;
        }

        MeetingResponseDto dto = new MeetingResponseDto();
        dto.setId(meeting.getId());
        dto.setTitle(meeting.getTitle());
        dto.setCreatedAt(meeting.getCreatedAt());
        dto.setNotes(meeting.getNotes());
        dto.setCreatedBy(meeting.getCreatedBy());

        List<MeetingResponseDto.ActionItemDto> itemDtos = meeting.getActionItems().stream().map(item -> {
            MeetingResponseDto.ActionItemDto aid = new MeetingResponseDto.ActionItemDto();
            aid.setText(item.getText());
            aid.setDone(item.isDone());
            return aid;
        }).toList();

        dto.setActionItems(itemDtos);

        logger.debug("Mapped Meeting entity with ID {} to MeetingResponseDto.", meeting.getId());
        return dto;
    }
}
