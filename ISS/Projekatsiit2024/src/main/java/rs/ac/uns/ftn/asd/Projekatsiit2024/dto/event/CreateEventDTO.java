package rs.ac.uns.ftn.asd.Projekatsiit2024.dto.event;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEventDTO {
    private String name;
    private String description;
    private int numOfAttendees;
    private Boolean isPrivate;
    private String place;
    private Double latitude;
    private Double longitude;
    private LocalDateTime dateOfEvent;
    private LocalDateTime endOfEvent;
    private String picture;
    private Integer price;
    private Integer organizerId;
    private List<Integer> eventTypesId;
}
