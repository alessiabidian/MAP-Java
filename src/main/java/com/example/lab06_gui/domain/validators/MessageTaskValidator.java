package com.example.lab06_gui.domain.validators;

import com.example.lab06_gui.domain.MessageTask;

public class MessageTaskValidator implements Validator<MessageTask>{
    @Override
    public void validate(MessageTask entity) throws ValidationException {
        if(entity.getMessage().isEmpty())
            throw new ValidationException("Empty message body!");
    }
}
