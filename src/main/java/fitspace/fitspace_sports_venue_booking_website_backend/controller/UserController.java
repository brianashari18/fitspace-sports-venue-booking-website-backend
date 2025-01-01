package fitspace.fitspace_sports_venue_booking_website_backend.controller;

import fitspace.fitspace_sports_venue_booking_website_backend.entity.User;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.user.UserRegisterRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.user.UserDataResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.user.UserUpdateRequest;
import fitspace.fitspace_sports_venue_booking_website_backend.dto.WebResponse;
import fitspace.fitspace_sports_venue_booking_website_backend.helper.DtoToWebMapper;
import fitspace.fitspace_sports_venue_booking_website_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserDataResponse> register(@RequestBody UserRegisterRequest request) {
        UserDataResponse userDataResponse = userService.register(request);
        return DtoToWebMapper.toWebResponse(userDataResponse);
    }

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserDataResponse> get(User user) {
        UserDataResponse userDataResponse = userService.get(user);
        return DtoToWebMapper.toWebResponse(userDataResponse);
    }

    @PatchMapping(
            path = "/api/users/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserDataResponse> update(User user, @RequestBody UserUpdateRequest request) {
        UserDataResponse userDataResponse = userService.update(user, request);
        return DtoToWebMapper.toWebResponse(userDataResponse);
    }

    @DeleteMapping(
            path = "/api/{userId}/delete",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(@PathVariable("userId") Long userId) {
        userService.delete(userId);
        return DtoToWebMapper.toWebResponse("Delete Success");
    }

    @GetMapping(
            path = "/api/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<UserDataResponse>> getAll() {
        List<UserDataResponse> userDataResponses = userService.getAll();
        return DtoToWebMapper.toWebResponse(userDataResponses);
    }
}
