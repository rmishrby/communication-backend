package com.example.distribution.service;

import com.example.distribution.dto.MeetingDto;
import com.example.distribution.dto.MeetingResponseDto;
import com.example.distribution.entity.ActionItem;
import com.example.distribution.entity.Meeting;
import com.example.distribution.exception.NotFoundException;
import com.example.distribution.repository.MeetingRepository;
import com.example.distribution.util.MeetingResponseMapper;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public MeetingResponseDto createMeeting(MeetingDto dto) {
        Meeting meeting = new Meeting();
        meeting.setTitle(dto.getTitle());
        meeting.setCreatedAt(LocalDateTime.now());
        meeting.setNotes(dto.getNotes());

        List<ActionItem> items = dto.getActionItems().stream().map(itemDto -> {
            ActionItem item = new ActionItem();
            item.setText(itemDto.getText());
            item.setDone(itemDto.isDone());
            item.setMeeting(meeting);
            return item;
        }).toList();

        meeting.setActionItems(items);
        return MeetingResponseMapper.toResponseDto(meetingRepository.save(meeting));
    }

    public MeetingResponseDto updateMeeting(Long id, MeetingDto dto) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Meeting not found with id: ", id));

        meeting.setTitle(dto.getTitle());
        meeting.setCreatedAt(LocalDateTime.now());
        meeting.setNotes(dto.getNotes());

        meeting.getActionItems().clear();
        List<ActionItem> items = dto.getActionItems().stream().map(itemDto -> {
            ActionItem item = new ActionItem();
            item.setText(itemDto.getText());
            item.setDone(itemDto.isDone());
            item.setMeeting(meeting);
            return item;
        }).toList();

        meeting.getActionItems().addAll(items);
        return MeetingResponseMapper.toResponseDto(meetingRepository.save(meeting));
    }

    public void deleteMeeting(Long id) {
        if (!meetingRepository.existsById(id)) {
            throw new NotFoundException("Meeting not found with id: ", id);
        }
        meetingRepository.deleteById(id);
    }

    public List<MeetingResponseDto> getAllMeetings() {
        return meetingRepository.findAll().stream().map(MeetingResponseMapper::toResponseDto).toList();
    }


}

