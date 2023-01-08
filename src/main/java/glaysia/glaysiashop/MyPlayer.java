package glaysia.glaysiashop;

public class MyPlayer {
    private String name;
    private String uuid;
    private double money;

    public MyPlayer(String name, String uuid, double money) {
        this.name = name;
        this.uuid = uuid;
        this.money = money;
    }
    public String getName() {
        return name;
    }
    public String getUUID() {
        return uuid;
    }
    public double getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public void addMoney(int pmoney) {
        this.money = this.money+pmoney;
    }
    public void subMoney(int mmoney) {
        this.money = this.money-mmoney;
    }

}