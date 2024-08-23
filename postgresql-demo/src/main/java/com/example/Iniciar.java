package com.example;

import java.util.List;
import java.util.Scanner;

public class Iniciar {
  private static MovieDAO movieDAO = new MovieDAO();
  private static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    if (movieDAO.conectar()) {
      System.out.println("Conectado ao banco de dados.");
      int option;
      do {
        option = showMenu();
        switch (option) {
          case 1:
            addMovie();
            break;
          case 2:
            listMovies();
            break;
          case 3:
            updateMovie();
            break;
          case 4:
            deleteMovie();
            break;
          case 5:
            System.out.println("Saindo...");
            break;
          default:
            System.out.println("Opção inválida. Tente novamente.");
        }
      } while (option != 5);

      movieDAO.close();
    } else {
      System.out.println("Não foi possível conectar ao banco de dados.");
    }
  }

  private static int showMenu() {
    System.out.println("\n--- MENU ---");
    System.out.println("1. Adicionar Filme");
    System.out.println("2. Listar Filmes");
    System.out.println("3. Atualizar Filme");
    System.out.println("4. Deletar Filme");
    System.out.println("5. Sair");
    System.out.print("Escolha uma opção: ");
    return scanner.nextInt();
  }

  private static void addMovie() {
    System.out.print("Digite o título do filme: ");
    scanner.nextLine(); // Consumir a nova linha
    String title = scanner.nextLine();
    System.out.print("Digite o ano de lançamento: ");
    int releaseYear = scanner.nextInt();
    System.out.print("Digite o gênero do filme: ");
    scanner.nextLine(); // Consumir a nova linha
    String genre = scanner.nextLine();
    System.out.print("Digite o diretor do filme: ");
    String director = scanner.nextLine();

    Movie movie = new Movie(0, title, releaseYear, genre, director);
    if (movieDAO.insertMovie(movie)) {
      System.out.println("Filme adicionado com sucesso!");
    } else {
      System.out.println("Erro ao adicionar o filme.");
    }
  }

  private static void listMovies() {
    List<Movie> movies = movieDAO.getMovies();
    System.out.println("\n--- Lista de Filmes ---");
    for (Movie movie : movies) {
      System.out.println("ID: " + movie.getId());
      System.out.println("Título: " + movie.getTitle());
      System.out.println("Ano de Lançamento: " + movie.getReleaseYear());
      System.out.println("Gênero: " + movie.getGenre());
      System.out.println("Diretor: " + movie.getDirector());
      System.out.println("--------------------------");
    }
  }

  private static void updateMovie() {
    System.out.print("Digite o ID do filme a ser atualizado: ");
    int id = scanner.nextInt();
    Movie movie = movieDAO.getMovie(id);
    if (movie == null) {
      System.out.println("Filme não encontrado!");
      return;
    }

    System.out.print("Digite o novo título do filme (ou pressione Enter para manter o atual): ");
    scanner.nextLine(); // Consumir a nova linha
    String title = scanner.nextLine();
    if (!title.isEmpty()) {
      movie.setTitle(title);
    }

    System.out.print("Digite o novo ano de lançamento (ou 0 para manter o atual): ");
    int releaseYear = scanner.nextInt();
    if (releaseYear != 0) {
      movie.setReleaseYear(releaseYear);
    }

    System.out.print("Digite o novo gênero (ou pressione Enter para manter o atual): ");
    scanner.nextLine(); // Consumir a nova linha
    String genre = scanner.nextLine();
    if (!genre.isEmpty()) {
      movie.setGenre(genre);
    }

    System.out.print("Digite o novo diretor (ou pressione Enter para manter o atual): ");
    String director = scanner.nextLine();
    if (!director.isEmpty()) {
      movie.setDirector(director);
    }

    if (movieDAO.updateMovie(movie)) {
      System.out.println("Filme atualizado com sucesso!");
    } else {
      System.out.println("Erro ao atualizar o filme.");
    }
  }

  private static void deleteMovie() {
    System.out.print("Digite o ID do filme a ser deletado: ");
    int id = scanner.nextInt();
    if (movieDAO.deleteMovie(id)) {
      System.out.println("Filme deletado com sucesso!");
    } else {
      System.out.println("Erro ao deletar o filme.");
    }
  }
}