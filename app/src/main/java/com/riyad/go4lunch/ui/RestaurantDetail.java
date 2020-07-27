package com.riyad.go4lunch.ui;

import android.util.Log;

import com.google.gson.Gson;
import com.riyad.go4lunch.datadetail.OpeningHours;
import com.riyad.go4lunch.datadetail.Period;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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


    public String getFormattedOpeningHour() {
        String result = "";

        //TODO récupérer l'heure actuelle. utiliser Calendar.
        Calendar rightNow = Calendar.getInstance();
        rightNow.getTime();
        //TODO récupérer le jour de la semaine du user.
        int dayOfWeek = rightNow.get(Calendar.DAY_OF_WEEK);
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        int formatedCurrentHour = (hour * 100) + minute;
        Boolean isOpen = false;
        dayOfWeek = switchSunday(dayOfWeek);
        //TODO vérifier si Opening Hour n'est pas null.
        String previousName = "";
        if (getOpeningHours() != null) {
            //TODO parcourir la liste des périods

            Log.e("dayOfWeek", String.valueOf(switchSunday(dayOfWeek)) + " : " + dayOfWeek);
            for (int i = 0; i < getOpeningHours().getPeriods().size(); i++) {

                Gson gson = new Gson();

                Log.e("for period  before if", gson.toJson(getName()) + " " + gson.toJson(getOpeningHours().getPeriods().get(i)) + " " + dayOfWeek + " " + formatedCurrentHour);
                if (dayOfWeek == getOpeningHours().getPeriods().get(i).getOpen().getDay()
                        && dayOfWeek == getOpeningHours().getPeriods().get(i).getClose().getDay()) {
                    //TODO chercher l'interval actuel, si l'interval est trouvé affiché l'heure de fermeture sinon c'est fermé.
//                    String openHour = getOpeningHours().getPeriods().get(i).getOpen().getTime();
//                    String CloseHour = getOpeningHours().getPeriods().get(i).getClose().getTime();

                    Log.e("name : " + getName(), "dayOfWeek : " + dayOfWeek);
                    previousName = getName();
                    Log.e("for period", gson.toJson(getName()) + " " + gson.toJson(getOpeningHours().getPeriods().get(i)) + " " + dayOfWeek + " " + formatedCurrentHour);

                    if (formatedCurrentHour > Integer.parseInt(getOpeningHours().getPeriods().get(i).getOpen().getTime())
                            && formatedCurrentHour < Integer.parseInt(getOpeningHours().getPeriods().get(i).getClose().getTime())) {
                        Log.e("forattedHour", getOpeningHours().getPeriods().get(i).getClose().getTime() + ": " + getName());
                        result = "Open";
                        isOpen = true;

                    } else {
                        Log.i("heure", String.valueOf(formatedCurrentHour));
                        if (!isOpen) {
//                            DateFormat format = new SimpleDateFormat("kk:mm");
//                            Date date  = format.parse(openHour);
                            result = "Close, Open to : " +  getOpeningHours().getPeriods().get(i).getOpen().getTime() ;
                        } else {
                            result = "Open";
                        }
                    }
                } else if (result == "" ) {
                    Log.e(" else if " + getName(), "dayOfWeek : " + dayOfWeek + " Real Hour " + getOpeningHours().getPeriods().get(i).getOpen().getDay() );
                    result = "Closed Today";
                }
            }
        } else {
            //TODO si l'opening hour est null alors afficher indisponible.
            result = "Unavailable";
        }
        return result;
    }

    private int switchSunday(int dayOfWeek) {
        Log.e("swithSunday before if", String.valueOf(dayOfWeek));
        if (dayOfWeek == 7) {
            dayOfWeek = 0;
            Log.e("swithSunday after if", String.valueOf(dayOfWeek));
            return dayOfWeek;
        }
        Log.e("swithSunday without if", String.valueOf(dayOfWeek));
        return dayOfWeek;
    }
}
