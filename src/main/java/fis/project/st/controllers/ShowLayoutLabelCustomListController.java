package fis.project.st.controllers;

import fis.project.st.model.Show;
import fis.project.st.services.UserService;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;

import static fis.project.st.controllers.CustomListPageController.getCustomListName;
import static fis.project.st.controllers.LoginController.getCurrentUser;

public class ShowLayoutLabelCustomListController {
    @FXML
    private Text showTitle;
    @FXML
    private Text voteAverage;
    @FXML
    private ImageView showPoster;
    @FXML
    private Text type;

    private Show show;

    private ClickListener clickListener;

    @FXML
    public void click(MouseEvent event) throws IOException {
        clickListener.onClickListener(show);
        UserService.addToUserACustomList(getCurrentUser().getUsername(), getCustomListName(), show);
    }

    public void setData(Show show, ClickListener clickListener) {
        this.show = show;
        this.clickListener = clickListener;
        showTitle.setText(show.getName());
        showTitle.setWrappingWidth(120);
        voteAverage.setText(String.valueOf(show.getVote_average()));
        Image image = new Image("https://image.tmdb.org/t/p/w500" + show.getPoster_path());
        showPoster.setImage(image);
        type.setText(show.getType().toUpperCase());
        DropShadow ds = new DropShadow();
        ds.setColor(Color.web("#F6AE2D"));
        ds.setOffsetY(3);
        ds.setSpread(0);
        ds.setRadius(0);
        showPoster.setOnMouseClicked((MouseEvent event) -> showPoster.requestFocus());
        showPoster.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                showPoster.setEffect(ds);
            } else {
                showPoster.setEffect(null);
            }
        });

    }
}
