package fis.project.st.requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fis.project.st.model.Movie;
import fis.project.st.model.Show;
import fis.project.st.model.ShowUtil.Genre;
import fis.project.st.model.TV;
import fis.project.st.model.TVUtil.Episode;
import fis.project.st.model.TVUtil.Season;
import org.json.*;

public class requests {
    private static HttpURLConnection connection;
    final String key = "52be3e7fef4340778b2a9b84547e29db";
    final String baseURL = "https://api.themoviedb.org/3";

    public ArrayList<Show> getBaseData(String responseBody, String type) {
        responseBody = responseBody.substring(responseBody.indexOf('['));
        responseBody += ']';
        ArrayList<Show> shows = new ArrayList<>();
        JSONArray movies = new JSONArray(responseBody);
        for (int i = 0; i < movies.length(); i++) {
            JSONObject movie = movies.getJSONObject(i);
            if(movie.isNull("poster_path")) continue;
            int id = movie.getInt("id");
            String poster_path = movie.isNull("poster_path") ? "" : movie.getString("poster_path");;
            String m_type = movie.isNull("media_type") ? ( type.equals("movies") ? "movie" : "tv") : movie.getString("media_type");
            String title = m_type.equals("movie") ? movie.getString("title") : movie.getString("name");
            float vote_average = movie.isNull("vote_average") ? 0 : movie.getFloat("vote_average");
            shows.add(new Show(id, poster_path, "", "", vote_average, null, "", title, m_type));
        }
        return shows;
    }

    public Show getMovieById(String responseBody) {
        Show show;
        JSONObject movie = new JSONObject(responseBody);
            int id = movie.getInt("id");
            String poster_path = movie.isNull("poster_path") ? "" : movie.getString("poster_path");
            String backdrop_path = movie.isNull("backdrop_path") ? "" : movie.getString("backdrop_path");
            String overview = movie.isNull("overview") ? "" : movie.getString("overview");
            float vote_average = movie.isNull("vote_average") ? 0 : movie.getFloat("vote_average");
            JSONArray genres_arrTmp = movie.getJSONArray("genres");
            ArrayList<Genre> genres = new ArrayList<>();
            for (int j = 0; j < genres_arrTmp.length(); j++) {
                JSONObject genre_tmp = genres_arrTmp.getJSONObject(j);
                Genre genre = new Genre(genre_tmp.getInt("id"), genre_tmp.getString("name"));
                genres.add(genre);
            }
            String status = movie.isNull("status") ? "" : movie.getString("status");
            String title = movie.isNull("title")? "" : movie.getString("title");
            String release_date = movie.isNull("release_date") ? "" : movie.getString("release_date");
            int runtime = movie.isNull("runtime") ? 0 : movie.getInt("runtime");
            boolean video = !movie.isNull("video") && movie.getBoolean("video");
            show = new Movie(id, poster_path, backdrop_path, overview, vote_average, genres, status, title, release_date, runtime, video, "movie");
        return show;
    }

    public Show getTVById(String responseBody) {
        Show show;
        JSONObject tv = new JSONObject(responseBody);
        int id = tv.getInt("id");
        String poster_path = tv.isNull("poster_path") ? "" : tv.getString("poster_path");
        String backdrop_path = tv.isNull("backdrop_path") ? "" : tv.getString("backdrop_path");
        String overview = tv.isNull("overview") ? "" : tv.getString("overview");
        float vote_average = tv.isNull("vote_average") ? 0 : tv.getFloat("vote_average");
        JSONArray genres_arrTmp = tv.getJSONArray("genres");
        ArrayList<Genre> genres = new ArrayList<>();
        for (int j = 0; j < genres_arrTmp.length(); j++) {
            JSONObject genre_tmp = genres_arrTmp.getJSONObject(j);
            Genre genre = new Genre(genre_tmp.getInt("id"), genre_tmp.getString("name"));
            genres.add(genre);
        }
        String status = tv.isNull("status") ? "" : tv.getString("status");
        String title = tv.isNull("name") ? "" : tv.getString("name");
        String first_air_date = tv.isNull("first_air_date") ? "" : tv.getString("first_air_date");
        String last_air_date = tv.isNull("last_air_date") ? "" : tv.getString("last_air_date");
        JSONObject last_episode = tv.isNull("last_episode_to_air") ? null : tv.getJSONObject("last_episode_to_air");
        Episode last_episode_to_air = null;
        if (last_episode != null) {
            last_episode_to_air = new Episode(last_episode.isNull("air_date") ? "" : last_episode.getString("air_date"), last_episode.getInt("episode_number"), last_episode.getInt("id"), last_episode.getString("name"), last_episode.getString("overview"), last_episode.getInt("season_number"), last_episode.isNull("still_path") ? "" : last_episode.getString("still_path"));
        }
        int number_of_episodes = tv.getInt("number_of_episodes");
        int number_of_seasons = tv.getInt("number_of_seasons");
        JSONArray seasons_arrTmp = tv.getJSONArray("seasons");
        ArrayList<Season> seasons = new ArrayList<>();
        for (int j = 0; j < seasons_arrTmp.length(); j++) {
            JSONObject season_tmp = seasons_arrTmp.getJSONObject(j);
            Season season = new Season(season_tmp.isNull("air_date") ? "" : season_tmp.getString("air_date"), season_tmp.isNull("episode_count") ? 0 : season_tmp.getInt("episode_count"), season_tmp.getInt("id"), season_tmp.getString("name"), season_tmp.isNull("overview") ? "" : season_tmp.getString("overview"), season_tmp.isNull("poster_path") ? "" : season_tmp.getString("poster_path"), season_tmp.getInt("season_number"));
            seasons.add(season);
        }
        show = new TV(id, poster_path, backdrop_path, overview, vote_average, genres, status, title, first_air_date, last_air_date, last_episode_to_air,null, number_of_episodes, number_of_seasons, seasons, "tv");
        return show;
    }

    public String getData(String partUrlBefore, String partUrlAfter) {
        BufferedReader reader;
        String line;
        StringBuilder responseContent = new StringBuilder();
        try {
            URL url = new URL(baseURL + partUrlBefore + "api_key=" + key + partUrlAfter);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status = connection.getResponseCode();
            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
            }
            return responseContent.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return responseContent.toString();
    }

}
