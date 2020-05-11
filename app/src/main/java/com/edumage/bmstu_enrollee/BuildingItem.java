package com.edumage.bmstu_enrollee;

import android.net.Uri;

public class BuildingItem {
    private String name;
    private String description;
    private int img_id;
    private String address;
    private Uri geoLocation;
    private int id;


    public  BuildingItem(){

    }

    //for testing
    public static BuildingItem generate(){
        BuildingItem item = new BuildingItem();
        item.address="Рубцовская наб., 2/18, Москва";
        item.description="Общая площадь — более 80 тыс. м². Рассчитан"+
        "на одновременный приём более 5 тыс. студентов."+
                "В нём 100 аудиторий, 20 компьютерных классов, 19 лифтов,"+
                "библиотека (рассчитанная на хранение 800 тыс. томов книг),"+
                "читальный зал на 680 мест.";
        item.name="Учебно-лабораторный корпус (УЛК)";
        item.img_id=R.drawable.ulk;
        item.generateGeoLocation();
        return item;
    }

    void generateGeoLocation(){
        geoLocation= Uri.parse("geo:55.771818, 37.693002?z=3");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setId(int id){
        this.id=id;
        defineImgByXmlId();
    }

    public void defineImgByXmlId(){
        switch (id){
            case 1:
                img_id=R.drawable.guk;
                break;
            case 2:
                img_id=R.drawable.ulk;
                break;
            case 3:
                img_id=R.drawable.ulk;
                break;
            case 4:
                img_id=R.drawable.izmailovo;
                break;
            case 5:
                img_id=R.drawable.nomts;
                break;
        }
    }

    public String getAddress() {
        return address;
    }

    public Uri getGeoLocation(){
        return geoLocation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = Uri.parse(geoLocation);
    }
}
