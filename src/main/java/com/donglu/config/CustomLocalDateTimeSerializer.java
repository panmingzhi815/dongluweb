package com.donglu.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by panmingzhi on 2016/12/28 0028.
 */
public class CustomLocalDateTimeSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object dateTime, JsonGenerator generator, SerializerProvider sp)
            throws IOException {
        if (dateTime instanceof LocalDate) {
            String formattedDateTime = ((LocalDate)dateTime).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            generator.writeString(formattedDateTime);
        }

        if (dateTime instanceof LocalDateTime) {
            String formattedDateTime = ((LocalDateTime)dateTime).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            generator.writeString(formattedDateTime);
        }

    }

}
