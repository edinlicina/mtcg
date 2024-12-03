package at.fhtw.mtcg.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.dto.LoginUserDto;
import at.fhtw.mtcg.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.security.NoSuchAlgorithmException;

public class SessionController extends Controller implements Service {
    private final UserService userService;

    public SessionController(){
     userService = new UserService();
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST ) {
            return handlePost(request);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response handlePost(Request request) {
        LoginUserDto dto;
        try {

             dto = this.getObjectMapper().readValue(request.getBody(), LoginUserDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String token;
        try {
             token = userService.loginUser(dto);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                token
        );

    }
}
