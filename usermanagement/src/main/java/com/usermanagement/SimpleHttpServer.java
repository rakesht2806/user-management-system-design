package main.java.com.usermanagement;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import  java.net.URI;
import java.util.List;

public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;

        // Create a server that listens on the given port
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/users", exchange -> {
            URI uri = exchange.getRequestURI();
            String query = uri.getQuery(); // example: "id=1"

            String response;

            // If query has id=..., return one user
            if (query != null && query.startsWith("id=")) {
                String idValue = query.substring(3);
                int id;

                try {
                    id = Integer.parseInt(idValue);
                } catch (NumberFormatException e) {
                    response = "Invalid id. Please use a number. Example: /users?id=1";
                    exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
                    exchange.sendResponseHeaders(400, response.getBytes().length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }

                User user = UserStore.getUserById(id);

                if (user == null) {
                    response = "User not found for id=" + id;
                    exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
                    exchange.sendResponseHeaders(404, response.getBytes().length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    return;
                }

                response =
                        "User Details:\n" +
                                "ID: " + user.getId() + "\n" +
                                "Name: " + user.getName() + "\n" +
                                "Email: " + user.getEmail();

            } else {
                // No id passed, return all users
                List<User> users = UserStore.getAllUsers();

                StringBuilder sb = new StringBuilder();
                sb.append("All Users:\n");
                for (User u : users) {
                    sb.append("ID: ").append(u.getId())
                            .append(", Name: ").append(u.getName())
                            .append(", Email: ").append(u.getEmail())
                            .append("\n");
                }
                response = sb.toString();
            }

            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });

        // Start the server
        server.start();

        System.out.println("  http://localhost:" + port + "/users");
    }
}