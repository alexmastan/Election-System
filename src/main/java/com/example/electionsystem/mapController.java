package com.example.electionsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;

public class mapController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane mapPane;

    public void initialize() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/electionsystem", "postgres", "cristofor12");

        String query = "SELECT judete.region, candidates.party " +
                "FROM judete " +
                "JOIN votes ON judete.code = votes.county_code " +
                "JOIN candidates ON votes.candidate_id = candidates.id " +
                "WHERE votes.nr_of_votes > 0 " +
                "AND votes.nr_of_votes = ( " +
                "    SELECT MAX(votes.nr_of_votes) " +
                "    FROM votes " +
                "    WHERE votes.county_code = judete.code " +
                ")";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        Color PSD = Color.rgb(239, 51, 64, 1);
        Color PNL = Color.rgb(0,150,255, 0.8);
        Color USR = Color.rgb(0,42,89, 1);
        Color AUR = Color.rgb(252,194,36, 1);

        for (Node node : mapPane.getChildren()) {
            if (node instanceof SVGPath) {
                SVGPath county = (SVGPath) node;
                county.setFill(Color.LIGHTGRAY);
            }
        }

        while (rs.next()) {
            String region = rs.getString("region");
            String party = rs.getString("party");
            boolean colored = false;
            for (Node node : mapPane.getChildren()) {
                if (node instanceof SVGPath) {
                    SVGPath county = (SVGPath) node;
                    if(county.getId().matches(region)) {
                        if ("PSD".equals(party)) {
                            county.setFill(PSD);
                        } else if ("PNL".equals(party)) {
                            county.setFill(PNL);
                        } else if ("USR".equals(party)) {
                            county.setFill(USR);
                        } else if ("AUR".equals(party)) {
                            county.setFill(AUR);
                        }
                    }
                }
            }
        }

        connection.close();
        ps.close();
        rs.close();
    }

    public void hoverLabel(MouseEvent e) {
        Label label = (Label) e.getSource();
        label.setOpacity(0.5);
    }

    public void hoverLabelRevert(MouseEvent e) {
        Label label = (Label) e.getSource();
        label.setOpacity(1);
    }

    public void switchToEntry(MouseEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("dash_entry.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public void switchToChart(MouseEvent e) throws IOException{
        root = FXMLLoader.load(getClass().getResource("dash_chart.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public void switchToLogs(MouseEvent e) throws IOException{
        root = FXMLLoader.load(getClass().getResource("dash_logs.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public void hoverCounty(MouseEvent e) throws SQLException {
        SVGPath county = (SVGPath) e.getSource();
        String id = county.getId();
        String code = "";

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/electionsystem", "postgres", "cristofor12");
        String query = "select code from judete where region = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            code = rs.getString("code");
        }

        query = "select candidate_id, nr_of_votes from votes where county_code = ?";
        ps = connection.prepareStatement(query);
        ps.setString(1, code);
        rs = ps.executeQuery();

        int sum = 0, max = 0, candidate_id = 0;

        while (rs.next()) {
            int value = rs.getInt("nr_of_votes");

            sum += value;
            if(max < value) {
            max = rs.getInt("nr_of_votes");
            candidate_id = rs.getInt("candidate_id");
            }
        }

        double percentage = 0;
        if(sum != 0 && max != 0) {
            percentage = (double) max/sum * 100;
        }
        String formattedPercentage = String.format("%.2f", percentage);

        query = "select name, party from candidates where id = ?";
        ps = connection.prepareStatement(query);
        ps.setInt(1, candidate_id);
        rs = ps.executeQuery();

        String name = "-";
        String party = "-";
        if (rs.next()) {
            name = rs.getString("name");
            party = rs.getString("party");
        }

        Tooltip tooltip = new Tooltip( "County: " + id + "\n" +
                "Leading candidate: " + name + "\n" +
                "Affiliate party: " + party + "\n" +
                "Leading Percentage: " + formattedPercentage + "%");

        tooltip.setStyle("-fx-font-size: 14px; -fx-background-color: black;");
        tooltip.setShowDelay(Duration.millis(500));
        tooltip.setHideDelay(Duration.millis(200));
        tooltip.setOpacity(0.8);

        Tooltip.install(county, tooltip);

        connection.close();
        ps.close();
        rs.close();
    }

}
