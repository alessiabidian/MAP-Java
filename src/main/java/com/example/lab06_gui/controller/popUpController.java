package com.example.lab06_gui.controller;

import com.example.lab06_gui.domain.MessageTask;
import com.example.lab06_gui.domain.Utilizator;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class popUpController {
    @FXML
    public Text fieldTo;
    @FXML
    public Text fieldFrom;
    @FXML
    public Text fieldMessage;
    @FXML
    public Text fieldDescription;
    public void setController(Utilizator uTo, Utilizator uFrom, MessageTask msg) {
        fieldTo.setText( uTo.getID() + " / " + uTo.getNume() + " " +uTo.getPrenume());
        fieldFrom.setText(uFrom.getID() + " / " +uFrom.getNume() + " " + uFrom.getPrenume());
        fieldDescription.setText(msg.getDescription());
        fieldMessage.setText(msg.getMessage());
    }
}
