package com.example.distribution.service;

import com.example.distribution.dto.MeetingDto;
import com.example.distribution.dto.MeetingResponseDto;
import com.example.distribution.entity.ActionItem;
import com.example.distribution.entity.Meeting;
import com.example.distribution.exception.NotFoundException;
import com.example.distribution.repository.MeetingRepository;
import com.example.distribution.util.MeetingResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingService {

    private static final Logger logger = LoggerFactory.getLogger(MeetingService.class);

    private final MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public MeetingResponseDto createMeeting(MeetingDto dto) {
        logger.info("Creating new meeting with title: {}", dto.getTitle());

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
        Meeting saved = meetingRepository.save(meeting);

        logger.info("Meeting created with ID: {}", saved.getId());
        return MeetingResponseMapper.toResponseDto(saved);
    }

    public MeetingResponseDto updateMeeting(Long id, MeetingDto dto) {
        logger.info("Updating meeting with ID: {}", id);

        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Meeting not found with ID: {}", id);
                    return new NotFoundException("Meeting not found with id: ", id);
                });

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
        Meeting saved = meetingRepository.save(meeting);

        logger.info("Meeting updated with ID: {}", saved.getId());
        return MeetingResponseMapper.toResponseDto(saved);
    }

    public void deleteMeeting(Long id) {
        logger.info("Attempting to delete meeting with ID: {}", id);

        if (!meetingRepository.existsById(id)) {
            logger.warn("Meeting not found for deletion with ID: {}", id);
            throw new NotFoundException("Meeting not found with id: ", id);
        }

        meetingRepository.deleteById(id);
        logger.info("Meeting deleted with ID: {}", id);
    }

    public List<MeetingResponseDto> getAllMeetings() {
        logger.info("Fetching all meetings");
        List<MeetingResponseDto> meetings = meetingRepository.findAll()
                .stream()
                .map(MeetingResponseMapper::toResponseDto)
                .toList();
        logger.info("Total meetings found: {}", meetings.size());
        return meetings;
    }
}
