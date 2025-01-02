// EventDTO.java
package com.example.eventsproj.dto;

import com.example.eventsproj.model.LocationType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EventDTO {
    private String eventName;
    private boolean unlimitedTickets;
    private Integer numberOfTickets;
    private String organizerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocationType location;
    private String category;
}