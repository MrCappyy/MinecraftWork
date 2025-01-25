package xyz.talecraft.playercard;

public class PlayerCard {

    private String name = "Unknown";
    private String age = "Unknown";
    private String discord = "Unknown";
    private String nation = "Unknown";
    private String pronouns = "Unknown";
    private String race = "Unknown";
    private String religion = "Unknown";
    private String wiki = "Unknown";

    // Constructor
    public PlayerCard() {}

    // Getters
    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getDiscord() {
        return discord;
    }

    public String getNation() {
        return nation;
    }

    public String getPronouns() {
        return pronouns;
    }

    public String getRace() {
        return race;
    }

    public String getReligion() {
        return religion;
    }

    public String getWiki() {
        return wiki;
    }

    // Setters
    public void setName(String name) {
        this.name = name == null || name.isEmpty() ? "Unknown" : name;
    }

    public void setAge(String age) {
        this.age = age == null || age.isEmpty() ? "Unknown" : age;
    }

    public void setDiscord(String discord) {
        this.discord = discord == null || discord.isEmpty() ? "Unknown" : discord;
    }

    public void setNation(String nation) {
        this.nation = nation == null || nation.isEmpty() ? "Unknown" : nation;
    }

    public void setPronouns(String pronouns) {
        this.pronouns = pronouns == null || pronouns.isEmpty() ? "Unknown" : pronouns;
    }

    public void setRace(String race) {
        this.race = race == null || race.isEmpty() ? "Unknown" : race;
    }

    public void setReligion(String religion) {
        this.religion = religion == null || religion.isEmpty() ? "Unknown" : religion;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki == null || wiki.isEmpty() ? "Unknown" : wiki;
    }

    // For Debugging or Display Purposes
    @Override
    public String toString() {
        return "PlayerCard{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", discord='" + discord + '\'' +
                ", nation='" + nation + '\'' +
                ", pronouns='" + pronouns + '\'' +
                ", race='" + race + '\'' +
                ", religion='" + religion + '\'' +
                ", wiki='" + wiki + '\'' +
                '}';
    }
}
