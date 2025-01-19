package at.fhtw.mtcg.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.exceptions.*;
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
        List<String> deck;
        try {
            deck = deckService.getDeckForUser(authorizationHeaderToken);

        } catch (DeckNotFoundException e) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "Deck not found"
            );
        }

        return new Response(HttpStatus.OK, ContentType.JSON, deck.toString());
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
        } catch (DuplicatedCardIdException e) {
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "Card with same Id cannot be played in the Deck"
            );
        } catch (UserNotAuthorizedException e) {
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    e.getMessage()
            );
        }
        return new Response(
                HttpStatus.ACCEPTED,
                ContentType.JSON,
                cardsToPutInDeck.toString()
        );
    }
}

