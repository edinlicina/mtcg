package at.fhtw;

import at.fhtw.httpserver.server.Server;
import at.fhtw.httpserver.utils.Router;
import at.fhtw.mtcg.controller.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(10001, configureRouter());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Router configureRouter()
    {
        Router router = new Router();
        router.addService("/sessions", new SessionController());
        router.addService("/users", new UserController());
        router.addService("/packages", new AuthorizedController(new PackageController()));
        router.addService("/transactions", new AuthorizedController(new TransactionController()));
        router.addService("/cards", new AuthorizedController(new CardController()));
        return router;
    }
}
