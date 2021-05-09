package fis.project.st.controllers;

import fis.project.st.model.Show;
import fis.project.st.requests.requests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ArrayList;

public class SimilarButtonController {
    private static ArrayList<Show> similarShows;

    @FXML
    public void handleGetSimilar(ActionEvent event) throws IOException {
        if(HomepageController.getSelectedShow().getType().equals("tv")) {
            requests req = new requests();
            String responseBody = req.getData("/tv/" + HomepageController.getSelectedShow().getId() + "/similar?", "");
            similarShows = req.getBaseData(responseBody, "tv");
        } else {
            requests req = new requests();
            String responseBody = req.getData("/movie/" + HomepageController.getSelectedShow().getId() + "/similar?", "");
            similarShows = req.getBaseData(responseBody, "movie");
        }
    }
}
