package com.example.eventcounter;

public class Settings {

    private String button1Name;
    private String button2Name;
    private String button3Name;
    private int maxEvents;

    public Settings() {}

    public Settings(String b1, String b2, String b3, int max) {
        this.button1Name = b1;
        this.button2Name = b2;
        this.button3Name = b3;
        this.maxEvents = max;
    }

    public String getButton1Name() { return button1Name; }
    public String getButton2Name() { return button2Name; }
    public String getButton3Name() { return button3Name; }
    public int getMaxEvents() { return maxEvents; }

    public void setButton1Name(String name) { this.button1Name = name; }
    public void setButton2Name(String name) { this.button2Name = name; }
    public void setButton3Name(String name) { this.button3Name = name; }
    public void setMaxEvents(int max) { this.maxEvents = max; }

    public boolean isComplete() {
        return button1Name != null && !button1Name.isEmpty()
                && button2Name != null && !button2Name.isEmpty()
                && button3Name != null && !button3Name.isEmpty()
                && maxEvents >= 5 && maxEvents <= 200;
    }
}

