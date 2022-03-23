package hu.petrik.konyvtarasztali;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {

    @FXML
    private TableColumn<Konyv, String> cimCol;
    @FXML
    private TableColumn<Konyv, String> szerzoCol;
    @FXML
    private TableColumn<Konyv, Integer> kiadasEveCol;
    @FXML
    private TableColumn<Konyv, Integer> oldalszamCol;
    @FXML
    private TableView<Konyv> konyvTableView;

    private KonyvDb db;

    public void initialize() {
        cimCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        szerzoCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        kiadasEveCol.setCellValueFactory(new PropertyValueFactory<>("publish_year"));
        oldalszamCol.setCellValueFactory(new PropertyValueFactory<>("page_count"));

        listaFeltoltes();
    }

    @FXML
    public void onTorlesClick(ActionEvent actionEvent) {
        int selectedIndex = konyvTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("Jelöljön ki egy elemet");
            return;
        }
        Konyv torlendoKonyv = konyvTableView.getSelectionModel().getSelectedItem();
        if (!confimration("Biztosan törölni szeretné")) {
            return;
        }
        try {
            db.deleteKonyv(torlendoKonyv.getId());
            listaFeltoltes();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void listaFeltoltes() {
        try {
            db = new KonyvDb();
            List<Konyv> konyvList = db.getKonyvek();
            konyvTableView.getItems().clear();
            for (Konyv konyv : konyvList) {
                konyvTableView.getItems().add(konyv);
            }
        } catch (SQLException e) {
            if(error(e)) {
                System.exit(0);
            }
        }
    }

    public void alert(String uzenet) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText(uzenet);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.show();
    }

    public boolean confimration(String uzenet) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText(uzenet);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public boolean error(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(e.getMessage());
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
}