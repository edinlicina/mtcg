package at.fhtw.mtcg.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.dto.CardDto;
import at.fhtw.mtcg.service.DeckService;

import java.util.List;

public class DeckController extends Controller implements Service {

    private final DeckService deckService;

    public DeckController() {
        deckService = new DeckService();
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET) {
            return handleGet(request);
        }
        return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "Error");


    }


    private Response handleGet(Request request) {
        HeaderMap headerMap = request.getHeaderMap();
        String authorizationHeader = headerMap.getHeader("Authorization");
        String authorizationHeaderToken = authorizationHeader.substring(7);

        List<CardDto> deckService1 = deckService.getDeckForUser(authorizationHeaderToken);

        return new Response(HttpStatus.OK, ContentType.JSON, deckService1.toString());
    }
}
