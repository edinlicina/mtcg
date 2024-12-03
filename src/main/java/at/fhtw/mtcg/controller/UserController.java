package at.fhtw.mtcg.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.exceptions.NotUniqueException;
import at.fhtw.mtcg.dto.CreateUserDto;
import at.fhtw.mtcg.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class UserController extends Controller implements Service {

    private final UserService userService;

    public UserController() {
        userService = new UserService();
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST) {
            return handlePost(request);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response handlePost(Request request) {
        CreateUserDto dto;
        try {
            dto = this.getObjectMapper().readValue(request.getBody(), CreateUserDto.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try {
            userService.registerUser(dto);
        } catch (NotUniqueException e) {
            return new Response(
                    HttpStatus.CONFLICT,
                    ContentType.JSON,
                    "User already exists"
            );
        }
        return new Response(
                HttpStatus.CREATED,
                ContentType.JSON,
                "User registered!"
        );
    }

}
