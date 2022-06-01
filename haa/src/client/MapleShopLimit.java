package client;

public class MapleShopLimit {//mush
    private int accountid;
    private int charid;
    private int shopid;
    private int itemid;
    private int position;
    private int limitcountacc;
    private int limitcountchr;
    private int lastbuymonth;
    private int lastbuyday;

    public MapleShopLimit(int accountid, int charid, int shopid, int itemid, int position, int limitcountacc, int limitcountchr, int lastbuymonth, int lastbuyday) {
        this.accountid = accountid;
        this.charid = charid;
        this.shopid = shopid;
        this.itemid = itemid;
        this.position = position;
        this.limitcountacc = limitcountacc;
        this.limitcountchr = limitcountchr;
        this.lastbuymonth = lastbuymonth;
        this.lastbuyday = lastbuyday;
    }

    public int getAccId() {
        return this.accountid;
    }

    public void setAccId(int accountid) {
        this.accountid = accountid;
    }

    public int getCharId() {
        return this.charid;
    }

    public void setCharId(int charid) {
        this.charid = charid;
    }

    public int getShopId() {
        return this.shopid;
    }

    public void setShopId(int shopid) {
        this.shopid = shopid;
    }

    public int getItemid() {
        return this.itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLimitCountAcc() {
        return this.limitcountacc;
    }

    public void setLimitCountAcc(int limitcountacc) {
        this.limitcountacc = limitcountacc;
    }

    public int getLimitCountChr() {
        return this.limitcountchr;
    }

    public void setLimitCountChr(int limitcountchr) {
        this.limitcountchr = limitcountchr;
    }

    public int getLastBuyMonth() {
        return this.lastbuymonth;
    }

    public void setLastBuyMonth(int lastbuymonth) {
        this.lastbuymonth = lastbuymonth;
    }

    public int getLastBuyDay() {
        return this.lastbuyday;
    }

    public void setLastBuyDay(int lastbuyday) {
        this.lastbuyday = lastbuyday;
    }
}

