package com.example.lab06_gui.controller;

import com.example.lab06_gui.HelloApplication;
import com.example.lab06_gui.domain.MessageTask;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.ValidationException;
import com.example.lab06_gui.service.Service;
import com.example.lab06_gui.utils.events.MessageTaskChangeEvent;
import com.example.lab06_gui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

public class MessageTaskController implements Observer<MessageTaskChangeEvent> {
    public Button seeMessageDetailsbutton;
    Service service;
    private ObservableList<MessageTask> model = FXCollections.observableArrayList();

    @FXML
    TableView<MessageTask> tableView;
    /*@FXML
    TableColumn<MessageTask,String> tableColumnDescription;*/
    @FXML
    TableColumn<MessageTask,String> tableColumnMessage;
    @FXML
    TableColumn<MessageTask,Long> tableColumnFrom;
    @FXML
    TableColumn<MessageTask,Long> tableColumnTo;
    @FXML
    TableColumn<MessageTask, LocalDateTime> tableColumnData;
    @FXML
    public TextField toTextField;
    @FXML
    public TextField descriptionTextField;
    @FXML
    public TextArea messageTextField;
    @FXML
    public TextField searchMessagesField;
    @FXML
    public Button sendMessageButton;
    @FXML
    public Button deleteMessageButton;


    private Utilizator userCurent;

    public void setUserCurent(Utilizator userCurent) {
        this.userCurent = userCurent;
    }
    public void setUtilizatorService(Service service) {
        this.service = service;
        //service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnMessage.setCellValueFactory(new PropertyValueFactory<MessageTask, String>("message"));
        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<MessageTask, Long>("from"));
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<MessageTask, Long>("to"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory<MessageTask, LocalDateTime>("date"));
        tableView.setItems(model);
    }

    private void initModel() {
        /*Iterable<MessageTask> messages = service.getAllMessages();
        List<MessageTask> messageTaskList = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(messageTaskList);*/
        model.setAll();
        model.addAll(service.getAllMessagesToFromUser(userCurent));
    }


    public void handleDeleteMessage(ActionEvent actionEvent) {
        MessageTask selected = (MessageTask) tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            MessageTask deleted = service.deleteMessageTask(selected);
            if (null != deleted)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Mesajul a fost sters cu succes!");
        }
        else MessageAlert.showErrorMessage(null, "Nu ati selectat nici un mesaj!");
        update(null);
    }

    @Override
    public void update(MessageTaskChangeEvent messageTaskChangeEvent) {

        initModel();
    }

    public void handleClickSearch(MouseEvent actionEvent)
    {
        searchMessagesField.setText("");
    }

    public void handleFilterMessages(KeyEvent actionEvent) {

        update(null);
        tableView.setItems(model);
        String string = String.valueOf(searchMessagesField.getCharacters());
        if(!string.isEmpty())
        {
            List<MessageTask> messages = service.getAllMessagesToFromUser(userCurent).stream().filter(x -> (service.findUser(x.getTo()).getPrenume()+
                    service.findUser(x.getTo()).getNume()+
                    service.findUser(x.getFrom()).getPrenume()+
                    service.findUser(x.getFrom()).getNume()+
                    x.getDescription()+x.getMessage()).contains(string)).toList();
            model.setAll(messages);
            tableView.setItems(model);
        }

    }

    public void handleSendMessage(ActionEvent actionEvent) {

        MessageTask msg = new MessageTask(null, descriptionTextField.getText(), messageTextField.getText(),
                userCurent.getID(), Long.parseLong(toTextField.getText()), service.generateCurrentDate());

        //if(service.estePrieten(userCurent, service.findUser(Long.parseLong(toTextField.getText())))) {

            try {
                service.addMessageTask(msg);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Sent", "Mesajul a fost trimis cu succes!");
            } catch (ValidationException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            }
        //}
        //else MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Nu iti este prieten!");
        toTextField.setText("To:");
        descriptionTextField.setText("Description:");
        messageTextField.setText("Message:");
        update(null);
    }

    public void handleSeeDetails(ActionEvent actionEvent) {
        MessageTask msg = tableView.getSelectionModel().getSelectedItem();
        Utilizator uTo = service.findUser(msg.getTo());
        Utilizator uFrom = service.findUser(msg.getFrom());

        try {
            URL fxmlLocation = HelloApplication.class.getResource("views/popUpView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Message Details");
            popUpController popUpController = fxmlLoader.getController();
            popUpController.setController(uTo, uFrom, msg);
            stage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}