package com.example;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.List;

public class Iniciar {
    private static MovieDAO movieDAO = new MovieDAO();
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        // Configuração de CORS
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "*");
            response.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        });

        if (movieDAO.conectar()) {
            System.out.println("Conectado ao banco de dados.");

            // Rota para adicionar um filme (POST)
            post("/movies", (req, res) -> {
                Movie movie = gson.fromJson(req.body(), Movie.class);
                if (movieDAO.insertMovie(movie)) {
                    res.status(201); // Created
                    return "Filme adicionado com sucesso!";
                } else {
                    res.status(500); // Internal Server Error
                    return "Erro ao adicionar o filme.";
                }
            });

            // Rota para listar filmes (GET)
            get("/movies", (req, res) -> {
                List<Movie> movies = movieDAO.getMovies();
                res.type("application/json");
                return gson.toJson(movies);
            });

            // Rota para atualizar um filme (PUT)
            put("/movies/:id", (req, res) -> {
                int id = Integer.parseInt(req.params(":id"));
                Movie movie = movieDAO.getMovie(id);
                if (movie == null) {
                    res.status(404); // Not Found
                    return "Filme não encontrado!";
                }

                Movie updatedMovie = gson.fromJson(req.body(), Movie.class);

                if (updatedMovie.getTitle() != null && !updatedMovie.getTitle().isEmpty()) {
                    movie.setTitle(updatedMovie.getTitle());
                }
                if (updatedMovie.getReleaseYear() != 0) {
                    movie.setReleaseYear(updatedMovie.getReleaseYear());
                }
                if (updatedMovie.getGenre() != null && !updatedMovie.getGenre().isEmpty()) {
                    movie.setGenre(updatedMovie.getGenre());
                }
                if (updatedMovie.getDirector() != null && !updatedMovie.getDirector().isEmpty()) {
                    movie.setDirector(updatedMovie.getDirector());
                }

                if (movieDAO.updateMovie(movie)) {
                    return "Filme atualizado com sucesso!";
                } else {
                    res.status(500); // Internal Server Error
                    return "Erro ao atualizar o filme.";
                }
            });

            // Rota para deletar um filme (DELETE)
            delete("/movies/:id", (req, res) -> {
                int id = Integer.parseInt(req.params(":id"));
                if (movieDAO.deleteMovie(id)) {
                    return "Filme deletado com sucesso!";
                } else {
                    res.status(500); // Internal Server Error
                    return "Erro ao deletar o filme.";
                }
            });

            // Rota para buscar um filme por ID (GET)
            get("/movies/:id", (req, res) -> {
                int id = Integer.parseInt(req.params(":id"));
                Movie movie = movieDAO.getMovie(id);
                if (movie == null) {
                    res.status(404); // Not Found
                    return "Filme não encontrado!";
                }
                res.type("application/json");
                return gson.toJson(movie);
            });

        } else {
            System.out.println("Não foi possível conectar ao banco de dados.");
        }

        // Parar o servidor ao final (opcional)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            movieDAO.close();
            stop();
        }));
    }
}