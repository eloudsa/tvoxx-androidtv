package net.noratek.tvoxx.androidtv.model;

public class Speaker {

    private String uuid;
    private String lastName;
    private String firstName;
    private String lang;
    private String company;
    private String avatarUrl;


    public Speaker() {
    }

    public Speaker(String uuid, String lastName, String firstName) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.firstName = firstName;
    }


    public Speaker(String uuid, String lastName, String firstName, String lang, String company, String avatarUrl) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.lang = lang;
        this.company = company;
        this.avatarUrl = avatarUrl;
    }

    // Getters and Setters
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
