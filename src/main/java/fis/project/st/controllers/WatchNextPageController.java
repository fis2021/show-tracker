package fis.project.st.controllers;

import fis.project.st.model.Movie;
import fis.project.st.model.Show;
import fis.project.st.model.TV;
import fis.project.st.requests.requests;
import fis.project.st.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WatchNextPageController implements Initializable {
    ClickListener clickListener;
    @FXML
    private GridPane watchGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int column = 1, row = 1;
        watchGrid.setVgap(10);
        watchGrid.setHgap(10);
        for (Show show : Objects.requireNonNull(UserService.getUserShowsAddedToWatchlist(LoginController.getCurrentUser().getUsername()))) {
            if (UserService.checkIfShowIsInWatchlist(LoginController.getCurrentUser().getUsername(), show)) {
                if (show.getType().equals("movie")) {
                    requests req = new requests();
                    String res = req.getData("/movie/" + show.getId() + "?", "");
                    Movie m = (Movie) req.getMovieById(res);
                    String date = m.getRelease_date();
                    if (getDateDiff(date) <= 30 && getDateDiff(date) >= 0) {
                        clickListener = new ClickListener() {
                            @Override
                            public void onClickListener(Show show) {
                            }

                            @Override
                            public void onClickListener(TV tv) {
                            }

                            @Override
                            public void onClickListener(Movie movie) {
                                HomepageController.setSelectedShow(HomepageController.getShow(movie));
                            }
                        };
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/movieLayout.fxml"));
                        AnchorPane anchorPane = null;
                        try {
                            anchorPane = fxmlLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (column == 4) {
                            column = 1;
                            row++;
                        }


                        watchGrid.add(anchorPane, column++, row);

                        gridSettings();
                        MovieLayoutController movieLayoutController = fxmlLoader.getController();
                        movieLayoutController.setData(m, clickListener);
                    }
                } else {
                    requests req = new requests();
                    String res = req.getData("/tv/" + show.getId() + "?", "");
                    TV t = (TV) req.getTVById(res);
                    String date = t.getLast_episode_to_air().getAir_date();
                    if (getDateDiff(date) <= 7 && getDateDiff(date) >= 0) {
                        clickListener = new ClickListener() {
                            @Override
                            public void onClickListener(Show show) {
                            }

                            @Override
                            public void onClickListener(TV tv) {
                                HomepageController.setSelectedShow(HomepageController.getShow(tv));
                            }

                            @Override
                            public void onClickListener(Movie movie) {
                            }
                        };
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/episodeLayout.fxml"));
                        AnchorPane anchorPane = null;
                        try {
                            anchorPane = fxmlLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (column == 4) {
                            column = 1;
                            row++;
                        }
                        watchGrid.add(anchorPane, column++, row);
                        gridSettings();
                        EpisodeLayoutController episodeLayoutController = fxmlLoader.getController();
                        episodeLayoutController.setData(t, clickListener);
                    }
                }
            }
        }
    }

    public int getDateDiff(String date) {
        LocalDate d1 = LocalDate.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd"));
        LocalDate d2 = LocalDate.now();
        return Math.abs(Days.daysBetween(d1, d2).getDays());
    }

    public void gridSettings() {
        watchGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
        watchGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
        watchGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
        watchGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
        watchGrid.setMaxHeight(Region.USE_PREF_SIZE);
        watchGrid.setMaxWidth(Region.USE_PREF_SIZE);
    }
}