package com.example.electionsystem;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class voterController {

    @FXML
    private CheckBox votusr;
    @FXML
    private CheckBox votaur;
    @FXML
    private CheckBox votpsd;
    @FXML
    private CheckBox votpnl;
    @FXML
    private Label timeLabel;

    private String regionCode;
    private boolean closing = false;

    public void getRegion(String regionCode) {
        this.regionCode = regionCode;
    }

    public void pickBox(ActionEvent e) {
        CheckBox selectedBox = (CheckBox) e.getSource();
        CheckBox[] checkBoxes = {votusr, votaur, votpsd, votpnl};

        for (CheckBox box : checkBoxes) {
            if (box != selectedBox) {
                box.setSelected(false);
            }
        }
    }

    public void submitVote(MouseEvent e) throws SQLException {
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();

        CheckBox candidate = checkSelection();
        if(!closing && candidate != null) {
            updateDatabase(candidate.getId());
            closeWindow(stage);
        } else if (!closing) {
            timeLabel.setText("Please select a candidate");
        }
    }

    public void updateDatabase(String candidate) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/electionsystem", "postgres", "cristofor12");
        String query = "update votes set nr_of_votes = nr_of_votes + 1 where candidate_id = ? and county_code = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(candidate));
        ps.setString(2, regionCode);
        ps.executeUpdate();

        query = "update candidates set nr_of_votes = nr_of_votes + 1 where id = ?";
        ps = connection.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(candidate));
        ps.executeUpdate();
    }

    public CheckBox checkSelection() {
        CheckBox[] checkBoxes = {votusr, votaur, votpsd, votpnl};
        for (CheckBox box : checkBoxes){
            if (box.isSelected()) {
                return box;
            }
        }
        return null;
    }

    public void closeWindow(Stage stage) {
        Timeline timeline = new Timeline();
        closing = true;
        int seconds = 5;
        for (int i = seconds; i >= 0; i--) {
            int timeLeft = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(seconds - i), event -> {
                if (timeLeft > 0) {
                    timeLabel.setText("Vote submitted! The window will close in " + timeLeft + " seconds");
                } else {
                    stage.close();
                }
            }));
        }
        timeline.play();
    }
}
