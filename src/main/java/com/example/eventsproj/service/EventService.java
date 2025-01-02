// EventService.java
package com.example.eventsproj.service;

import com.example.eventsproj.dto.EventDTO;
import com.example.eventsproj.model.Event;
import com.example.eventsproj.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    public Event createEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setEventName(eventDTO.getEventName());
        event.setUnlimitedTickets(eventDTO.isUnlimitedTickets());
        event.setNumberOfTickets(eventDTO.getNumberOfTickets());
        event.setOrganizerName(eventDTO.getOrganizerName());
        event.setStartDate(eventDTO.getStartDate());
        event.setEndDate(eventDTO.getEndDate());
        event.setLocation(eventDTO.getLocation());
        event.setCategory(eventDTO.getCategory());
        
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }
}