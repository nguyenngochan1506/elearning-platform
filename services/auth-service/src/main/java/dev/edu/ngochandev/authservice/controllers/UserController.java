package dev.edu.ngochandev.authservice.controllers;

import dev.edu.ngochandev.authservice.dtos.req.FilterRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @PostMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public Object listUsers(@RequestBody  FilterRequestDto filter) {
        return filter;
    }
}
