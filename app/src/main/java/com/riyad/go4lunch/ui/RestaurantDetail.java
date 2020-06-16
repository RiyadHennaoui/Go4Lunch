package com.riyad.go4lunch.ui;

public class RestaurantDetail {


    private String name;
    private String formatedAdress;
    private String website;
    private String formattedNumber;
    private String urlPicture;

    public RestaurantDetail( String name, String formatedAdress, String website, String formattedNumber, String urlPicture) {

        this.name = name;
        this.formatedAdress = formatedAdress;
        this.website = website;
        this.formattedNumber = formattedNumber;
        this.urlPicture = urlPicture;
    }

    //GETTER
     public String getName() { return name; }
    public String getFormatedAdress() { return formatedAdress; }
    public String getWebsite() { return website; }
    public String getFormattedNumber() { return formattedNumber; }
    public String getUrlPicture() { return urlPicture; }

    //SETTER
      public void setName(String name) { this.name = name; }
    public void setFormatedAdress(String formatedAdress) { this.formatedAdress = formatedAdress; }
    public void setWebsite(String website) { this.website = website; }
    public void setFormattedNumber(String formattedNumber) { this.formattedNumber = formattedNumber; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
}
