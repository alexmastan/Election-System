package com.example.electionsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class chartController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PieChart chart;

    public void initialize() throws SQLException {
        setChart();
    }

    public void setChart() throws SQLException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/electionsystem","postgres","cristofor12");
        String query = "select name, nr_of_votes from candidates";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
                String candidateName = rs.getString("name");
                int nrOfVotes = rs.getInt("nr_of_votes");

                PieChart.Data data = new PieChart.Data(candidateName, nrOfVotes);
                pieChartData.add(data);
        }
        chart.setData(pieChartData);
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

        //String css = this.getClass().getResource("admin_registration.css").toExternalForm();
        //scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.show();
    }

    public void switchToMap(MouseEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("dash_map.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();

        //String css = this.getClass().getResource("admin_registration.css").toExternalForm();
        //scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.show();
    }

    public void switchToLogs(MouseEvent e) throws IOException{
        root = FXMLLoader.load(getClass().getResource("dash_logs.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();

        //String css = this.getClass().getResource("admin_registration.css").toExternalForm();
        //scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.show();
    }
}
