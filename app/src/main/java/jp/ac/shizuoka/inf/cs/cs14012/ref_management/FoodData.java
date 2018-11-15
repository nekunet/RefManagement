package jp.ac.shizuoka.inf.cs.cs14012.ref_management;

import java.io.Serializable;

class FoodData implements Serializable{
    private String name; //品名
    private int number; //個数
    //賞味期限
    private int year;   //年
    private  int month;  //月
    private  int date;   //日

    private boolean search_flag;            //検索対象に入れるかどうか
    private boolean notification_flag;   //通知をするかどうか

    private int edit_id;    //編集用識別子 -1は新規作成を表す。0以上はリスト上の位置を表す。

    //デフォルトコンストラクタ
    public FoodData(){
        name = "UNKNOWN";
        number = 1;
        year = 2222;
        month = 2;
        date = 22;
        search_flag = false;
        notification_flag = false;
        edit_id = -1;
    }
    public FoodData(String name,int number,
                    int year,int month,int date,
                    boolean search_flag,
                    boolean notification_flag) {
        this.name = name;
        this.number = number;
        this.year = year;
        this.month = month;
        this.date = date;
        this.search_flag = search_flag;
        this.notification_flag = notification_flag;
        edit_id = -1;
    }
    void setName(String name){
        this.name = name;
    }
    String getName(){
        return name;
    }
    void setNumber(int number){
        this.number = number;
    }
    int getNumber(){
        return number;
    }
    void setUseByDate(int year,int month,int date){
        this.year = year;
        this.month = month;
        this.date = date;
    }
    String getUseByDateString(){
        return Integer.toString(year) + "/" + Integer.toString(month) + "/" + Integer.toString(date);
    }
    void setSearchFlag(boolean search_flag){
        this.search_flag = search_flag;
    }
    boolean getSearchFlag(){
        return search_flag;
    }
    void setNotificationFlag(boolean notification_flag){
        this.notification_flag = notification_flag;
    }
    boolean getNotificationFlag(){
        return notification_flag;
    }
    int getEditId(){
        return edit_id;
    }
    void setEditId(int edit_id){
        this.edit_id = edit_id;
    }
    int  getYear(){
        return year;
    }
    void setYear(int year){
        this.year = year;
    }
    int  getMonth(){
        return month;
    }
    void setMonth(int month){
        this.month = month;
    }
    int getDate(){
        return date;
    }
    void setDate(int date){
        this.date = date;
    }
    int getSerialDate(){
        return year*365 + month*30+ date;
    }
}