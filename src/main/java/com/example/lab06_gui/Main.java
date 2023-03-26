package com.example.lab06_gui;


import com.example.lab06_gui.domain.MessageTask;
import com.example.lab06_gui.domain.Prietenie;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.MessageTaskValidator;
import com.example.lab06_gui.domain.validators.PrietenieValidator;
import com.example.lab06_gui.domain.validators.UtilizatorValidator;
import com.example.lab06_gui.domain.validators.Validator;
import com.example.lab06_gui.repository.Repository;
import com.example.lab06_gui.repository.db.MessageTaskDbRepository;
import com.example.lab06_gui.repository.db.PrietenieDbRepository;
import com.example.lab06_gui.repository.db.UtilizatorDbRepository;
import com.example.lab06_gui.service.Service;
import com.example.lab06_gui.ui.UI;

public class Main {
    public static void main(String[] args) {

        Validator<Utilizator> valUtilizator = new UtilizatorValidator();
        Validator<Prietenie> valPretenie = new PrietenieValidator();
        Validator<MessageTask> valMessageTask = new MessageTaskValidator();
        //Repository<Long, Utilizator> repoUtilizator = new UtilizatorFile("src/repository/file/users.txt", valUtilizator);
        //Repository<String, Prietenie> repoPrietenie = new PrietenieFile("src/repository/file/friends.txt", valPretenie);
        Repository<String, Prietenie> repoDBPrietenie = new PrietenieDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres","3.B1d1an", valPretenie);
        Repository<Long, Utilizator> repoDBUtilizator = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","3.B1d1an", valUtilizator);
        Repository<Long, MessageTask> repoDBMessageTask = new MessageTaskDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","3.B1d1an", valMessageTask);

        Service srv = new Service(repoDBUtilizator, repoDBPrietenie, repoDBMessageTask);

        UI ui = new UI(srv);

        ui.start();
    }
}
