package com.example.electionsystem;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;

public class Login {

    @FXML
    private TextField user;
    @FXML
    private PasswordField pass;
    @FXML
    private Label validation;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void checkLogin(MouseEvent event) throws IOException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/electionsystem","postgres","cristofor12");
        String check = "select id from registrations where username = ? and password = ?";
        PreparedStatement ps = connection.prepareStatement(check);
        ps.setString(1, user.getText());
        ps.setString(2, pass.getText());
        ResultSet rs = ps.executeQuery();

        if (user.getText().isEmpty() || pass.getText().isEmpty()) {
            validation.setText("Please enter your credentials");
        }
        else if (rs.next()) {
            validation.setText("Login Successful");
            int id = rs.getInt("id");
            databaseLog(connection, id);
            switchSceneToMainWindow(event);
        }
        else {
            validation.setText("Invalid username or password");
        }
    }

    public void databaseLog(Connection connection, int id) throws SQLException, UnknownHostException {
        String check = "insert into login_logs (admin_id, ip_address) values (?,?)";
        PreparedStatement ps = connection.prepareStatement(check);
        ps.setInt(1, id);

        InetAddress ip = InetAddress.getLocalHost();
        ps.setString(2, ip.getHostAddress());

        ps.executeUpdate();
    }

    public void switchSceneToMainWindow(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("dash_entry.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        //String css = this.getClass().getResource("dash_entry.css").toExternalForm();
        //scene.getStylesheets().add(css);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2 - 120);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2 - 30);

        stage.setScene(scene);
        stage.show();
    }

    public void hoverSVG(MouseEvent e) {
        SVGPath svg = (SVGPath) e.getSource();
        svg.setOpacity(0.5);
    }

    public void hoverSVGRevert(MouseEvent e) {
        SVGPath svg = (SVGPath) e.getSource();
        svg.setOpacity(1);
    }
}
