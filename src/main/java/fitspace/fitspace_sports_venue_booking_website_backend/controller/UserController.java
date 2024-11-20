package fitspace.fitspace_sports_venue_booking_website_backend.controller;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.model.RegisterUserRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.model.UserDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.model.WebResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserDataResponse> register(@RequestBody RegisterUserRequest request) {
        UserDataResponse userDataResponse = userService.register(request);
        return WebResponse.<UserDataResponse>builder()
                .data(userDataResponse)
                .code(200).status("OK")
                .build();
    }
}
