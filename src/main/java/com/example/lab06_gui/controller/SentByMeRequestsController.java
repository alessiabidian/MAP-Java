package com.example.lab06_gui.controller;
import com.example.lab06_gui.domain.Prietenie;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.ValidationException;
import com.example.lab06_gui.service.Service;
import com.example.lab06_gui.utils.events.PrietenieEntityChangeEvent;
import com.example.lab06_gui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;

public class SentByMeRequestsController implements Observer<PrietenieEntityChangeEvent> {

    Service service;
    private ObservableList<Utilizator> modelRequests = FXCollections.observableArrayList();

    @FXML
    public Button buttonCancel;
    @FXML
    TableView<Utilizator> tableRequestsView;
    @FXML
    TableColumn<Utilizator,String> tableColumnLastName;
    @FXML
    TableColumn<Utilizator,String>  tableColumnFirstName;
    @FXML
    TableColumn<Utilizator, LocalDateTime> tableColumnDate;

    private Utilizator userCurent;
    private UtilizatorController controller;

    public void setController(UtilizatorController controller) {
        this.controller = controller;
    }

    @FXML
    public void setUserCurent(Utilizator userCurent) {
        this.userCurent = userCurent;
    }

    public void setUtilizatorService(Service service) {
        this.service = service;
        service.addObserver(this);
        initModel();
        initialize();
    }

    //@FXML
    public void initialize() {
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("nume"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("prenume"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<Utilizator, LocalDateTime>("date"));
        tableRequestsView.setItems(modelRequests);
    }

    private void initModel() {
        /*Iterable<Utilizator> messages = service.getAll();
        List<Utilizator> users = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);*/
        modelRequests.setAll();
        modelRequests.addAll(service.getSentByMeRequestsUserList(userCurent));
    }

    @Override
    public void update(PrietenieEntityChangeEvent utilizatorEntityChangeEvent) {
        initModel();
        initialize();
    }

    public void handleCancelRequest(ActionEvent actionEvent) {
        Utilizator user = tableRequestsView.getSelectionModel().getSelectedItem();
        Prietenie prietenie = new Prietenie(userCurent.getID(), user.getID(), LocalDateTime.now());
        service.deleteFriendship(prietenie.getID());
    }
}
