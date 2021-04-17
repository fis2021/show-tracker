package fis.project.st.controllers;

import fis.project.st.model.Show;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;


public class ShowLayoutController {
    @FXML
    private Text showTitle;
    @FXML
    private Text voteAverage;
    @FXML
    private ImageView showPoster;

    private Show show;

    public void setData(Show show) {
        this.show = show;
        showTitle.setText(show.getName());
        showTitle.setWrappingWidth(120);
        voteAverage.setText(String.valueOf(show.getVote_average()));
        Image image = new Image("https://image.tmdb.org/t/p/w500" + show.getPoster_path());
        showPoster.setImage(image);
    }
}
