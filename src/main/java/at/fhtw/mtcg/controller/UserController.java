package at.fhtw.mtcg.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.dto.CreateUserDto;
import at.fhtw.mtcg.dto.UpdateUserDto;
import at.fhtw.mtcg.exceptions.NotUniqueException;
import at.fhtw.mtcg.exceptions.UserNotAuthorizedException;
import at.fhtw.mtcg.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

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
        if (request.getMethod() == Method.PUT) {
            return handlePut(request);
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

    private Response handlePut(Request request) {
        UpdateUserDto dto;

        try {
            dto = this.getObjectMapper().readValue(request.getBody(), UpdateUserDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (request.getPathParts().size() != 2) {
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "URL not valid"
            );
        }
        HeaderMap headerMap = request.getHeaderMap();
        String authorizationHeader = headerMap.getHeader("Authorization");
        String authorizationHeaderToken = authorizationHeader.substring(7);
        String userToUpdate = request.getPathParts().get(1);


        try {
            userService.updateUser(dto, userToUpdate, authorizationHeaderToken);
        } catch (UserNotAuthorizedException e) {
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    "Cannot update other Users"
            );
        }

        return new Response(
                HttpStatus.ACCEPTED,
                ContentType.JSON,
                "User has been updated"
        );
    }

}
