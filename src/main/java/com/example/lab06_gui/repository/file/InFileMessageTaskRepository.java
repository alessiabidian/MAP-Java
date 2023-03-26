package com.example.lab06_gui.repository.file;

import com.example.lab06_gui.domain.MessageTask;
import com.example.lab06_gui.domain.validators.Validator;
import com.example.lab06_gui.utils.Constants;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class InFileMessageTaskRepository extends AbstractFileRepository<Long, MessageTask> {

    public InFileMessageTaskRepository(String fileName, Validator<MessageTask> validator) {
        super(fileName, validator);
    }

    @Override
    public MessageTask extractEntity(String linie) {
        //return Messages.createMessageTask(attributes);
        List<String> attr= Arrays.asList(linie.split(";"));

        Long id= Long.valueOf(attr.get(0).split("=")[1]);
        String desc=attr.get(1).split("=")[1];
        String msg=attr.get(2).split("=")[1];
        Long from= Long.valueOf(attr.get(3).split("=")[1]);
        Long to= Long.valueOf(attr.get(4).split("=")[1]);
        String dateAsString=attr.get(5).split("=")[1];
        LocalDateTime date= LocalDateTime.parse(dateAsString, Constants.DATE_TIME_FORMATTER);
        return new MessageTask(id,desc,msg,from,to,date);
    }

    @Override
    public String createEntityAsString(MessageTask x) {
        //id=1212|description=mesaj de la radu|message=ce faci?|from=radu|to=andrei|date=2018-11-18 22:09
        String res="";
        res+="id="+x.getID()+"|description="+x.getDescription()+"|message="+x.getMessage()
                +"|from="+x.getFrom()+"|to="+x.getTo()
                +"|date="+x.getDate().format(Constants.DATE_TIME_FORMATTER);
        return res;
    }

    @Override
    public List<MessageTask> filter(String criteriu) {
        return null;
    }
}


