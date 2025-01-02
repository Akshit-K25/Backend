// EventRepository.java
package com.example.eventsproj.repository;

import com.example.eventsproj.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}