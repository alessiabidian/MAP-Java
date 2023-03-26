package com.example.lab06_gui.controller;
import com.example.lab06_gui.HelloApplication;
import com.example.lab06_gui.domain.Prietenie;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.ValidationException;
import com.example.lab06_gui.service.Service;
import com.example.lab06_gui.utils.events.PrietenieEntityChangeEvent;
import com.example.lab06_gui.utils.events.UtilizatorEntityChangeEvent;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;

public class UtilizatorController implements Observer<PrietenieEntityChangeEvent> {
    Service service;
    private ObservableList<Utilizator> modelFriends = FXCollections.observableArrayList();
    private ObservableList<Utilizator> modelUser = FXCollections.observableArrayList();
    @FXML
    public Button sendRequestButton;
    @FXML
    public Button sentRequestsButton;
    @FXML
    public TextField searchBar;
    @FXML
    public Button seeRequestsButton;
    @FXML
    public ImageView myAccountIcon;
    @FXML
    public Text myAccountText;

    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator,String> tableColumnFirstName;
    @FXML
    TableColumn<Utilizator,String> tableColumnLastName;

    private Utilizator userCurent;

    @FXML
    public void setUserCurent(Utilizator userCurent) {
        this.userCurent = userCurent;
    }

    public void setUtilizatorService(Service service) {
        this.service = service;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("nume"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("prenume"));
        tableView.setItems(modelUser);
        //tableView.setItems(modelFriends);
    }

    private void initModel() {
        /*Iterable<Utilizator> messages = service.getAll();
        List<Utilizator> users = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);*/

        modelFriends.setAll();
        modelFriends.addAll(service.getFriendsOfUserList(userCurent));

        modelUser.setAll();
        modelUser.addAll(service.getStrangersOfUserList(userCurent));

        /*List<Utilizator> allUsers = service.getListUtilizatori();

        for(Utilizator u : allUsers)
        {
            if (!modelFriends.contains(u) && !modelUser.contains(u))
                modelUser.add(u);
        }*/

    }

    @Override
    public void update(PrietenieEntityChangeEvent utilizatorEntityChangeEvent) {
        tableView.setItems(null);
        initModel();
        initialize();
    }

    //nu le folosesc
    public void handleAddUtilizator(ActionEvent actionEvent) {
        /*Utilizator user = new Utilizator();
        try{
            Utilizator saved = service.addUser(user);
        } catch (ValidationException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
            return;
        }*/
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "User adaugat cu succes!");
//        if(service.addUtilizator(user) == null){
//            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "User adaugat cu succes!");
//        } else{
//            MessageAlert.showErrorMessage(null, "Failed adding user");
//        }
    }

    //nu le folosesc
    public void handleDeleteUtilizator(ActionEvent actionEvent) {
        Utilizator user=(Utilizator) tableView.getSelectionModel().getSelectedItem();
        if (user!=null) {
            //Utilizator deleted =
                    service.deleteUser(user.getID());
        }
    }

    //nu le folosesc
    public void handleUpdateUtilizator(ActionEvent actionEvent) {
    }

    /*
    PRIMUL ID ESTE CEL AL TRIMITATORULUI DE REQUEST, AL2LEA ID ESTE CEL CARE PRIMESTE REQUESTUL!!!!
     */
    public void handleSendRequest(ActionEvent actionEvent) {
        Utilizator user = tableView.getSelectionModel().getSelectedItem();
        boolean status = false;
        try {
            status = service.sendFriendRequest(userCurent.getID(), user.getID());
            if(status)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Cerere trimisa cu succes!");
            else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Cerere bidirectionala!\nPrietenie adaugata!");
        } catch (ValidationException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
        }

    }

    public void handleActivateButtonAdd(MouseEvent actionEvent)
    {
        //tableView.getSelectionModel().clearSelection();
        sendRequestButton.setDisable(false);
    }

    public void handleClickSearch(MouseEvent actionEvent)
    {
        searchBar.setText("");
    }

    public void handleSearchUsers(KeyEvent keyEvent) {
        modelUser.setAll(service.getStrangersOfUserList(userCurent));
        tableView.setItems(modelUser);
        String valoareNume = String.valueOf(searchBar.getCharacters());
        if(!valoareNume.equals(""))
        {
            List<Utilizator> strangers = service.getStrangersOfUserList(userCurent).stream().filter(x -> (x.getNume()+x.getPrenume()).contains(valoareNume)).toList();
            modelUser.setAll(strangers);
            tableView.setItems(modelUser);
        }
        sendRequestButton.setDisable(true);
    }

    public void handleOpenSeeRequests(ActionEvent actionEvent) {
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/RequestsView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Received Requests Page");
            RequestsController requestsController= fxmlLoader.getController();
            requestsController.setUserCurent(userCurent);
            requestsController.setUtilizatorService(service);
            requestsController.setController(this);
            stage.show();
            //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void handleOpenAccount(MouseEvent mouseEvent) {
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/MyAccountView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("My Account");
            MyAccountController myAccountController= fxmlLoader.getController();
            myAccountController.setUserCurent(userCurent);
            myAccountController.setUtilizatorService(service);
            myAccountController.setController(this);
            myAccountController.setPreviousStage((Node)mouseEvent.getSource()); //!!!!!!!!!!
            stage.show();
            //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSeeSentRequests(ActionEvent actionEvent) {
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/SentByMeRequestsView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Sent Requests Page");
            SentByMeRequestsController sentByMeRequestsController= fxmlLoader.getController();
            sentByMeRequestsController.setUserCurent(userCurent);
            sentByMeRequestsController.setUtilizatorService(service);
            sentByMeRequestsController.setController(this);
            stage.show();
            //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
