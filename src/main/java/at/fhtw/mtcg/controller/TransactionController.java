package at.fhtw.mtcg.controller;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.exceptions.NoMoneyException;
import at.fhtw.mtcg.exceptions.NoPackagesException;
import at.fhtw.mtcg.service.TransactionService;

import java.util.List;

public class TransactionController extends Controller implements Service {


    private final TransactionService transactionService;

    public TransactionController() {
        transactionService = new TransactionService();
    }

    @Override
    public Response handleRequest(Request request) {
        List<String> pathParts = request.getPathParts();
        boolean isTransactionPackagePath = pathParts.size() == 2 && pathParts.contains("packages");
        if (request.getMethod() == Method.POST && isTransactionPackagePath) {
            return handlePost(request);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }

    private Response handlePost(Request request) {

        HeaderMap headerMap = request.getHeaderMap();
        String authorizationHeader = headerMap.getHeader("Authorization");
        String authorizationHeaderToken = authorizationHeader.substring(7);

        try {
            transactionService.acquirePackage(authorizationHeaderToken);
        } catch (NoPackagesException e) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "No packages available"
            );
        } catch (NoMoneyException e) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "Not enough money"
            );
        }
        return new Response(
                HttpStatus.CREATED,
                ContentType.JSON,
                "Acquired Package"
        );

    }
}
