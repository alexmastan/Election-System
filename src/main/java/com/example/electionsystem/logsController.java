package com.example.electionsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class logsController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<LogEntry> table;
    @FXML
    private TableColumn<LogEntry, Integer> idColumn;
    @FXML
    private TableColumn<LogEntry, String> timeColumn;
    @FXML
    private TableColumn<LogEntry, String> ipColumn;
    @FXML
    private SVGPath clearLogs;

    ObservableList<LogEntry> logEntries = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("adminId"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));

        table.setItems(getLogEntries());
    }

    private ObservableList<LogEntry> getLogEntries() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/electionsystem", "postgres", "cristofor12");
        String query = "SELECT * FROM login_logs";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int adminId = rs.getInt("admin_id");
            String loginTime = rs.getTimestamp("login_time").toString();
            String ipAddress = rs.getString("ip_address");

            logEntries.add(new LogEntry(adminId, loginTime, ipAddress));
        }

        return logEntries;
    }

    public void clearLogs() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/electionsystem", "postgres", "cristofor12");
        String query = "DELETE FROM login_logs;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.executeUpdate();

        logEntries.clear();
        table.setItems(logEntries);
    }

    public void hoverSVG(MouseEvent e) {
        SVGPath svg = (SVGPath) e.getSource();
        svg.setOpacity(0.5);
    }

    public void hoverSVGRevert(MouseEvent e) {
        SVGPath svg = (SVGPath) e.getSource();
        svg.setOpacity(1);
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

    public void switchToChart(MouseEvent e) throws IOException{
        root = FXMLLoader.load(getClass().getResource("dash_chart.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();

        //String css = this.getClass().getResource("admin_registration.css").toExternalForm();
        //scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.show();
    }

}
