package fitspace.fitspace_sports_venue_booking_website_backend.controller;

import fitspace.fitspace_sports_venue_booking_website_backend.dto.WebResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleAddRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.schedule.ScheduleDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping(
            path = "/api/{fieldId}/schedule",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ScheduleDataResponse> addSchedule(@PathVariable("fieldId")Integer fieldId, ScheduleAddRequest request){
        ScheduleDataResponse scheduleDataResponse = scheduleService.add(fieldId, request);
        return WebResponse.<ScheduleDataResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(scheduleDataResponse)
                .build();
    }

    @DeleteMapping(
            path = "/api/{fieldId}/schedule/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteSchedule(@PathVariable("fieldId")Integer fieldId, @PathVariable("id") Integer id){
        scheduleService.delete(fieldId, id);
        return WebResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data("Schedule deleted successfully")
                .build();
    }
}
