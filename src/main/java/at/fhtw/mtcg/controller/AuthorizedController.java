package at.fhtw.mtcg.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.service.UserService;

public class AuthorizedController extends Controller implements Service {

    private final UserService userService;
    private final Service service;
    public AuthorizedController(Service service) {
        userService = new UserService();
        this.service = service;
    }

    @Override
    public Response handleRequest(Request request) {
        HeaderMap headerMap = request.getHeaderMap();
        String authorizationHeader = headerMap.getHeader("Authorization");
        if(authorizationHeader == null || authorizationHeader.isEmpty()){
            return createUnauthorizedResponse();
        }
        if(!authorizationHeader.startsWith("Bearer ")) {
            return createUnauthorizedResponse();
        }
        String authorizationHeaderToken = authorizationHeader.substring(7);
        boolean isTokenValid = userService.isValidToken(authorizationHeaderToken);
        if(!isTokenValid){
            return createUnauthorizedResponse();
        }
        return service.handleRequest(request);

    }
    private Response createUnauthorizedResponse(){
        return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "Unauthorized");
    }
}
