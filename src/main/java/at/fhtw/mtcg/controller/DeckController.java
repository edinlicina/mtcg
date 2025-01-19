package at.fhtw.mtcg.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.dto.CardDto;
import at.fhtw.mtcg.exceptions.CardNotFoundException;
import at.fhtw.mtcg.exceptions.NotFourCardsException;
import at.fhtw.mtcg.exceptions.UserNotAuthorizedException;
import at.fhtw.mtcg.service.DeckService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

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
        if (request.getMethod() == Method.PUT) {
            return handlePut(request);
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

    private Response handlePut(Request request) {
        List<String> cardsToPutInDeck;
        HeaderMap headerMap = request.getHeaderMap();
        String authorizationHeader = headerMap.getHeader("Authorization");
        String authorizationHeaderToken = authorizationHeader.substring(7);
        try {

            cardsToPutInDeck = this.getObjectMapper().readValue(request.getBody(), new TypeReference<>() {
            });

        } catch (JsonProcessingException e) {
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "Error while parsing the request"
            );
        }
        try {
            deckService.upsertDeck(cardsToPutInDeck, authorizationHeaderToken);
        } catch (NotFourCardsException e) {
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "Not four Cards"
            );
        } catch (CardNotFoundException e) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "Card with Id " + e.getMessage() + " couldn't be found"

            );
        } catch (UserNotAuthorizedException e) {
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    "User not authorized for this Card"
            );
        }
        return new Response(
                HttpStatus.ACCEPTED,
                ContentType.JSON,
                cardsToPutInDeck.toString()
        );
    }
}

