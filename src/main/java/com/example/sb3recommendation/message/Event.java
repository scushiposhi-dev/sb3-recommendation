package com.example.sb3recommendation.message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.*;

import java.time.ZonedDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class Event<K,D>{
    public enum Type{ CREATE,DELETE }

    private final Type eventType;
    private final K key;
    private final D data;
    private final ZonedDateTime eventCreatedAt=ZonedDateTime.now();

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    public ZonedDateTime getEventCreatedAt(){
        return eventCreatedAt;
    }
}
