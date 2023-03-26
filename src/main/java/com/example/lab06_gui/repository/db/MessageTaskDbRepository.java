package com.example.lab06_gui.repository.db;

import com.example.lab06_gui.domain.MessageTask;
import com.example.lab06_gui.domain.validators.ValidationException;
import com.example.lab06_gui.domain.validators.Validator;
import com.example.lab06_gui.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageTaskDbRepository implements Repository<Long, MessageTask> {
    private String url;
    private String username;
    private String password;
    private Validator<MessageTask> validator;

    public MessageTaskDbRepository(String url, String username, String password, Validator<MessageTask> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public MessageTask findOne(Long id) {
        if(id == null)
            throw new IllegalArgumentException("Id must not be null");

        String sql = "SELECT * FROM messages where id = ?";

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                //Long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                String message = resultSet.getString("message");
                Long from = resultSet.getLong("from_id");
                Long to = resultSet.getLong("to_id");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                MessageTask msg = new MessageTask(id, description, message, from, to, date);
                msg.setID(id);
                return msg;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public MessageTask findByTwoStrings(String str1, String str2){
        MessageTask msg = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages where messages.description like ? or messages.message like ?")){
            statement.setString(1, str1);
            statement.setString(2, str2);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                String message = resultSet.getString("message");
                Long from = resultSet.getLong("from_id");
                Long to = resultSet.getLong("to_id");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                msg = new MessageTask(id, description, message, from, to, date);
                msg.setID(id);
                return msg;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return msg;
    }

    @Override
    public Iterable<MessageTask> findAll() {
        Set<MessageTask> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages ORDER BY date DESC");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                String message = resultSet.getString("message");
                Long from = resultSet.getLong("from_id");
                Long to = resultSet.getLong("to_id");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                MessageTask msg = new MessageTask(id, description, message, from, to, date);
                msg.setID(id);
                messages.add(msg);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public MessageTask save(MessageTask entity) throws ValidationException {
        if (entity==null)
            throw new IllegalArgumentException("Entity must be not null");

        validator.validate(entity);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String sql = "insert into messages (message, description, to_id, from_id, date) values (?, ?, ?, ?,'" + entity.getDate().format(formatter)+ "')";

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, entity.getMessage());
            ps.setString(2, entity.getDescription());
            ps.setLong(3, entity.getTo());
            ps.setLong(4, entity.getFrom());
            //ps.setDate(4, entity.getDate().format(formatter));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MessageTask delete(Long id) {
        if(id == null)
            throw new IllegalArgumentException("ID is null! Cannot delete!");

        String sql = "delete from messages where id = ?";
        MessageTask msg = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);

            msg = this.findOne(id);
            if(msg == null)
                return null;

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return msg;
    }

    @Override
    public MessageTask update(MessageTask entity) throws ValidationException {
        return null;
    }

    @Override
    public List<MessageTask> getAllAsList() {
        Iterable<MessageTask> iterable = findAll();
        List<MessageTask> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public int getSize() {
        return getAllAsList().size();
    }
}