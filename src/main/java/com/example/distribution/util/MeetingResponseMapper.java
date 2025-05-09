package com.example.distribution.util;

import com.example.distribution.dto.MeetingResponseDto;
import com.example.distribution.entity.Meeting;

import java.util.List;

public class MeetingResponseMapper {
    public  static MeetingResponseDto toResponseDto(Meeting meeting) {
        if(meeting == null) {
            return null;
        }
        MeetingResponseDto dto = new MeetingResponseDto();
        dto.setId(meeting.getId());
        dto.setTitle(meeting.getTitle());
        dto.setCreatedAt(meeting.getCreatedAt());
        dto.setNotes(meeting.getNotes());

        List<MeetingResponseDto.ActionItemDto> itemDtos = meeting.getActionItems().stream().map(item -> {
            MeetingResponseDto.ActionItemDto aid = new MeetingResponseDto.ActionItemDto();
            aid.setText(item.getText());
            aid.setDone(item.isDone());
            return aid;
        }).toList();

        dto.setActionItems(itemDtos);
        return dto;
    }
}
