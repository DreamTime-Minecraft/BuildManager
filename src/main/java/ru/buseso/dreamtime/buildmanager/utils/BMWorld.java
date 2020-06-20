package ru.buseso.dreamtime.buildmanager.utils;

import java.util.List;

public class BMWorld {
    private String id;
    private String owner;
    private List<String> builders;
    private Progress progress;
    private String name;
    private String description;
    private Games game;
    private boolean privat;

    public BMWorld(String id, String owner, List<String> builders,
                   Progress progress, String name, String description, Games game, boolean privat) {
        this.id = id;
        this.owner = owner;
        this.builders = builders;
        this.progress = progress;
        this.name = name;
        this.description = description;
        this.game = game;
        this.privat = privat;
    }

    public Games getGame() {
        return game;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getBuilders() {
        return builders;
    }

    public Progress getProgress() {
        return progress;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBuilders(List<String> builders) {
        this.builders = builders;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGame(Games game) {
        this.game = game;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public void setPrivat(boolean privat) {
        this.privat = privat;
    }

    public boolean isPrivat() {
        return privat;
    }

    public void addBuilder(String name) {
        getBuilders().add(name);
    }

    public void removeBuilder(String name) {
        getBuilders().remove(name);
    }
}
