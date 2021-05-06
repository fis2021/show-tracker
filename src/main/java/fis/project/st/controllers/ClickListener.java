package fis.project.st.controllers;

import fis.project.st.model.*;

public interface ClickListener {
    void onClickListener(Show show);
    void onClickListener(TV tv);
    void onClickListener(Movie tv);
}
