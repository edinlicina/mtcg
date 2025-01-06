package at.fhtw.mtcg.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.dto.CardDto;
import at.fhtw.mtcg.exceptions.DuplicatedCardIdException;
import at.fhtw.mtcg.exceptions.NegativeDamageException;
import at.fhtw.mtcg.exceptions.NotFiveCardsException;
import at.fhtw.mtcg.service.PackageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class PackageController extends Controller implements Service {

    private final PackageService packageService;

    public PackageController() {
        packageService = new PackageService();
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
        List<CardDto> dto;
        try {

            dto = this.getObjectMapper().readValue(request.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try {


            packageService.createPackage(dto);
        }catch (NotFiveCardsException e){
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "Not five cards"
            );
        }catch (DuplicatedCardIdException e){
            return new Response(
                    HttpStatus.CONFLICT,
                    ContentType.JSON,
                    "Card already exists"

            );
        } catch (NegativeDamageException e) {
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "Negative damage not allowed"
            );
        }
        return new Response(
                HttpStatus.CREATED,
                ContentType.JSON,
                "Package created"
        );


    }

}
