package com.example.lab06_gui.domain;
import java.util.Objects;

public abstract class Task extends Entity<Long>{

    private String description;

    public Task(Long taskId, String description) {
        super.setID(taskId);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return super.getID() + " " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(super.getID(), task.getID()) &&
                Objects.equals(getDescription(), task.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getID(), getDescription());
    }

    public abstract void execute();
}
