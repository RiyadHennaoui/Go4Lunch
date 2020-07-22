package com.riyad.go4lunch.ui;

import com.riyad.go4lunch.datadetail.OpeningHours;
import com.riyad.go4lunch.datadetail.Period;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetail {


    private String name;
    private String formatedAdress;
    private String website;
    private String formattedNumber;
    private String urlPicture;
    private OpeningHours openingHours;


    public RestaurantDetail() {
    }





    public RestaurantDetail(String name, String formatedAdress, String website, String formattedNumber, String urlPicture, OpeningHours openingHours) {

        this.name = name;
        this.formatedAdress = formatedAdress;
        this.website = website;
        this.formattedNumber = formattedNumber;
        this.urlPicture = urlPicture;
        this.openingHours = openingHours;

    }

    //GETTER
    public String getName() { return name; }
    public String getFormatedAdress() { return formatedAdress; }
    public String getWebsite() { return website; }
    public String getFormattedNumber() { return formattedNumber; }
    public String getUrlPicture() { return urlPicture; }
    public OpeningHours getOpeningHours() { return openingHours; }



    //SETTER
    public void setName(String name) { this.name = name; }
    public void setFormatedAdress(String formatedAdress) { this.formatedAdress = formatedAdress; }
    public void setWebsite(String website) { this.website = website; }
    public void setFormattedNumber(String formattedNumber) { this.formattedNumber = formattedNumber; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setOpeningHours(OpeningHours openingHours) { this.openingHours = openingHours; }



    public String getFormattedOpeningHour (){
        String result ="";
        //TODO récupérer le jour de la semain du user.
        //TODO récupérer l'heure actuelle. utiliser Calendar.

        //TODO vérifier si Opening Hour n'est pas null.

            //TODO parcourir la liste des périods

            //TODO chercher l'interval actuel, si l'interval est trouvé affiché l'heure de fermeture sinon c'est fermé.
        //TODO si l'opening hour est null alors afficher indisponible.

        return result;
    }
}
