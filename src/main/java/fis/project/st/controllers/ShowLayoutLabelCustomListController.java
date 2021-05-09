package fis.project.st.controllers;

import fis.project.st.model.Show;
import fis.project.st.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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
        System.out.println("The show " + show.getName() + " was added to " + getCustomListName());
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
    }
}
