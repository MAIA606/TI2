package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO extends DAO {

    public MovieDAO() {
        super();
    }

    public boolean insertMovie(Movie movie) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "INSERT INTO movies (title, release_year, genre, director) "
                    + "VALUES ('" + movie.getTitle() + "', " + movie.getReleaseYear() + ", '"
                    + movie.getGenre() + "', '" + movie.getDirector() + "');";
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean updateMovie(Movie movie) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "UPDATE movies SET title = '" + movie.getTitle() + "', release_year = "
                    + movie.getReleaseYear() + ", genre = '" + movie.getGenre() + "', "
                    + "director = '" + movie.getDirector() + "' WHERE id = " + movie.getId() + ";";
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean deleteMovie(int id) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "DELETE FROM movies WHERE id = " + id + ";";
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public List<Movie> getMovies() {
        List<Movie> movies = new ArrayList<Movie>();

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery ("SELECT * FROM movies");
            while (rs.next()) {
                Movie movie = new Movie(rs.getInt("id"), rs.getString("title"),
                        rs.getInt("release_year"), rs.getString("genre"),
                        rs.getString("director"));
                movies.add(movie);
            }
            st.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return movies;
    }

    public Movie getMovie(int id) {
        Movie movie = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM movies WHERE id = " + id);
            if (rs.next()) {
                movie = new Movie(rs.getInt("id"), rs.getString("title"),
                        rs.getInt("release_year"), rs.getString("genre"),
                        rs.getString("director"));
            }
            st.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return movie;
    }
}