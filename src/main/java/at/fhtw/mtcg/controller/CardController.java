package at.fhtw.mtcg.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.entity.CardEntity;
import at.fhtw.mtcg.service.CardService;

import java.util.List;

public class CardController extends Controller implements Service {

    private final CardService cardService;

    public CardController() {
        cardService = new CardService();
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET) {
            return handleGet(request);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response handleGet(Request request) {
        HeaderMap headerMap = request.getHeaderMap();
        String authorizationHeader = headerMap.getHeader("Authorization");
        String authorizationHeaderToken = authorizationHeader.substring(7);

        List<CardEntity> listOfCards = cardService.getCardsForUser(authorizationHeaderToken);
        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                listOfCards.toString()
        );
    }


}
