package client.custom.inventory;

import tools.Pair;

import java.util.ArrayList;
import java.util.List;

public class CustomItem {
    public static enum CustomItemType {
        None, 보조장비, 각인석
    }


    public static enum CustomItemEffect {
        BdR, CrD, AllStatR, MesoR, DropR
    }

    private int id;
    private CustomItemType type;
    private String name;
    private List<Pair<CustomItemEffect, Integer>> effects;

    public CustomItem(int id, CustomItemType type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.effects = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public CustomItemType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<Pair<CustomItemEffect, Integer>> getEffects() {
        return effects;
    }

    public void addEffects(CustomItemEffect effect, Integer value) {
        this.effects.add(new Pair<>(effect, value));
    }
}
