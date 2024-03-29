package fis.project.st.controllers;

import fis.project.st.model.TV;
import fis.project.st.model.TVUtil.Episode;
import fis.project.st.model.TVUtil.Season;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import fis.project.st.services.UserService;
import static fis.project.st.controllers.LoginController.getCurrentUser;
import org.controlsfx.control.Rating;


public class TVPageController implements Initializable {
    @FXML
    private Text title;

    @FXML
    private Text overview;

    @FXML
    private Text first_air_date;

    @FXML
    private Text last_air_date;

    @FXML
    private Text number_of_episodes;

    @FXML
    private Text number_of_seasons;

    @FXML
    private Text status;

    @FXML
    private ImageView showBackdrop;

    @FXML
    private Text vote_average;

    @FXML
    private VBox seasonGrid;

    @FXML
    private Text episodeName;

    @FXML
    private Text air_date;

    @FXML
    private Text episode_number;

    @FXML
    private Text season_number;

    @FXML
    private Text overviewEp;

    @FXML
    private ImageView episodeback;

    @FXML
    private Rating user_vote_field;

    @FXML
    private TextField comment_field;

    @FXML
    private TextArea users_comments_area;

    @FXML
    private Text added_comm_message;

    private TV tv;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tv = (TV) HomepageController.getSelectedShow();
        title.setText(tv.getName());
        title.setWrappingWidth(600);
        overview.setText(tv.getOverview());
        overview.setWrappingWidth(600);
        first_air_date.setText(tv.getFirst_air_date());
        last_air_date.setText(tv.getLast_air_date());
        number_of_episodes.setText(String.valueOf(tv.getNumber_of_episodes()));
        number_of_seasons.setText(String.valueOf(tv.getNumber_of_seasons()));
        status.setText(tv.getStatus());
        Image image = new Image("https://image.tmdb.org/t/p/w500" + tv.getBackdrop_path());
        showBackdrop.setImage(image);
        vote_average.setText(String.valueOf(tv.getVote_average()));

        ArrayList<Season> seasons = tv.getSeasons();
        for(Season season : seasons) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/seasonLayout.fxml"));
            try {
                AnchorPane anchorPane = fxmlLoader.load();
                SeasonLayoutController seasonLayoutController = fxmlLoader.getController();
                seasonLayoutController.setData(season);
                seasonGrid.getChildren().add(anchorPane);
                seasonGrid.setSpacing(10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Episode episode = tv.getLast_episode_to_air();
        episodeName.setText(episode.getName());
        air_date.setText(episode.getAir_date());
        episode_number.setText(String.valueOf(episode.getEpisode_number()));
        season_number.setText(String.valueOf(episode.getSeason_number()));
        overviewEp.setText(episode.getOverview());
        image = new Image("https://image.tmdb.org/t/p/w500" + episode.getStill_path());
        episodeback.setImage(image);
        //database rating
        ArrayList<String> tvs = UserService.getTvs(getCurrentUser().getUsername());
        int index = tvs.indexOf(tv.getName());
        ArrayList<String> tvsRates = UserService.getTvsRates(getCurrentUser().getUsername());
        user_vote_field.setRating(Double.parseDouble(tvsRates.get(index)));
        //database comment
        ArrayList<String> tvUserCommentsPerTv = UserService.getUsersCommentsPerTv(tv.getName());
        String usersComments = "";
        for(String s : tvUserCommentsPerTv){
            usersComments = usersComments + s;
        }
        users_comments_area.setText(usersComments);

        user_vote_field.ratingProperty().addListener(new ChangeListener<Number>() { //action event
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(UserService.checkIfShowIsInWatchlist(getCurrentUser().getUsername(), tv)) {
                    UserService.addTvsUserVote(getCurrentUser().getUsername(), t1.toString(), tv.getName());
                }
                else{
                    added_comm_message.setText("You must follow the show!");
                    user_vote_field.setRating(0.0);
                }
            }
        });
    }

    public void addTvComment() {
        if (UserService.checkIfShowIsInWatchlist(getCurrentUser().getUsername(), tv)) {
            UserService.addTvUserComment(getCurrentUser().getUsername(), tv.getName(), comment_field.getText());
            added_comm_message.setText("Your comment was added!");
            comment_field.setText("");
            ArrayList<String> tvUserCommentsPerTv = UserService.getUsersCommentsPerTv(tv.getName());
            String usersComments = "";
            for (String s : tvUserCommentsPerTv) {
                usersComments = usersComments + s;
            }
            users_comments_area.setText(usersComments);
        }
        else{
            added_comm_message.setText("You must follow the show!");
        }
    }
}
