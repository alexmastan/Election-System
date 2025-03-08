package com.example.electionsystem;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class entryController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField cnp;
    @FXML
    private TextField seria;
    @FXML
    private TextField nr;
    @FXML
    private TextField judet;
    @FXML
    private Label validation;

    public Connection databseConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/electionsystem","postgres","cristofor12");
        return connection;
    }

    public void submitEntry(MouseEvent e) throws SQLException, IOException {
        if(cnp.getText().isEmpty() || seria.getText().isEmpty() || nr.getText().isEmpty() || judet.getText().isEmpty()) {
            validation.setText("Please complete all fields");
        }
        else if(verifyJudet()) {
            validation.setText("Invalid Judet");
        }
        else if(!nr.getText().matches("^[0-9]{6}$")) {
            validation.setText("Invalid Numar");
        }
        else if(verifySeria()) {
            validation.setText("Invalid Seria");
        }
        else if(verifyCNP()) {
            validation.setText("Invalid CNP");
        }
        else if (checkDuplicate()) {
            validation.setText("Already Voted!");
        }
        else {
            validation.setText("Voter Window Opened");
            databaseVoter();
            openVoterInterface(e);}
    }

    public boolean verifySeria() throws SQLException {
        Connection connection = databseConnection();
        String query = "select serii from judete where region = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, judet.getText());
        ResultSet rs = ps.executeQuery();
        String serii = "";
        if(rs.next()) {
            serii = rs.getString("serii");
        }
        if(serii.contains(seria.getText()) && seria.getText().length()==2) {
            return false;
        }
        return true;
    }

    public boolean verifyJudet()  {
        try {
            Connection connection = databseConnection();
            String code = "select code from judete where region = ?";
            PreparedStatement ps = connection.prepareStatement(code);
            ps.setString(1, judet.getText());
            ResultSet rs = ps.executeQuery();
            return !rs.next();
        } catch(SQLException e) {
            return true;
        }
    }

    public boolean verifyCNP() throws SQLException {

        String text = cnp.getText();

        if (text.length() != 13) {
            System.out.println("length");
            return true;
        }
        if (!text.matches("^[0-9]+$")) //not digits
        {
            System.out.println("not digits");
            return true;
        }
        if (text.charAt(0) - '0' < 1 || text.charAt(0) - '0' > 8 || text.charAt(0) == '3' || text.charAt(0) == '4') {
            System.out.println("first");
            return true;
        }
        if (!isValidDate(text.substring(1, 7))) //invalid date
        {
            System.out.println("invalid date");
            return true;
        }
        if (cnpJudetMismatch(text.substring(7, 9))) //county code mismatching code in CNP
        {
            System.out.println("judet mismatch");
            return true;
        }
        return false;
    }

    public boolean cnpJudetMismatch(String text) throws SQLException {
        Connection connection = databseConnection();
        String query = "select code from judete where region = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, judet.getText());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String code = rs.getString("code");
            return !text.equals(code);
        }
        return true;
    }

    public static boolean isValidDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");

        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean checkDuplicate() throws SQLException {
        Connection connection = databseConnection();
        String query = "select * from voters where cnp = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, cnp.getText());
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return true;
        }
        return false;
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

    public void switchToMap(MouseEvent e) throws IOException{
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

    public void switchToLogs(MouseEvent e) throws IOException{
        root = FXMLLoader.load(getClass().getResource("dash_logs.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();

        //String css = this.getClass().getResource("admin_registration.css").toExternalForm();
        //scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.show();
    }

    public void databaseVoter() throws SQLException {
        Connection connection = databseConnection();
        String query = "insert into voters (cnp, seria, numar, judet) values(?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, cnp.getText());
        ps.setString(2, seria.getText());
        ps.setString(3, nr.getText());
        ps.setString(4, cnp.getText().substring(7, 9));
        ps.executeUpdate();

        connection.close();
        ps.close();
    }

    public void openVoterInterface(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("voter_interface.fxml"));
        root = loader.load();
        stage = new Stage();
        stage.setTitle("Voter Interface");
        scene = new Scene(root);

        voterController voterController = loader.getController();
        voterController.getRegion(cnp.getText().substring(7, 9));

        stage.setScene(scene);
        stage.show();
    }

}