package Appointment;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public interface TimeComoBoxFormatter {
    LocalTime timeFormatConversation (ZonedDateTime zonedDateTime);
}
