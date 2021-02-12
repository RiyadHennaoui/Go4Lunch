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
    public String getName() {
        return name;
    }

    public String getFormatedAdress() {
        return formatedAdress;
    }

    public String getWebsite() {
        return website;
    }

    public String getFormattedNumber() {
        return formattedNumber;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    //SETTER
    public void setName(String name) {
        this.name = name;
    }

    public void setFormatedAdress(String formatedAdress) {
        this.formatedAdress = formatedAdress;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setFormattedNumber(String formattedNumber) {
        this.formattedNumber = formattedNumber;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }


    public String getFormattedOpeningHour() {
        String result = "";

        Calendar rightNow = Calendar.getInstance();
        int dayOfWeek = rightNow.get(Calendar.DAY_OF_WEEK);
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        int formatedCurrentHour = (hour * 100) + minute;
        Boolean isOpen = false;
        dayOfWeek = switchSunday(dayOfWeek);
        if (getOpeningHours() != null) {
            for (int i = 0; i < getOpeningHours().getPeriods().size(); i++) {

                if (dayOfWeek == getOpeningHours().getPeriods().get(i).getOpen().getDay()
                        && dayOfWeek == getOpeningHours().getPeriods().get(i).getClose().getDay()) {
                    String openHour = getOpeningHours().getPeriods().get(i).getOpen().getTime();
                    String closeHour = getOpeningHours().getPeriods().get(i).getClose().getTime();
                    String closeHourFormatted = closeHour.substring(0, 2) + ":" + closeHour.substring(2);
                    String openHourFormatted = openHour.substring(0, 2) + ":" + openHour.substring(2);

                    if (formatedCurrentHour > Integer.parseInt(openHour)
                            && formatedCurrentHour < Integer.parseInt(closeHour)) {
                        //TODO Trouver comment faire une string ressource
                        result = "Open until : " + closeHourFormatted;
                        isOpen = true;

                    } else {
                        if (!isOpen) {
                            result = "Close, Open at : " + openHourFormatted;
                        } else {
                            result = "Open";
                        }
                    }
                } else if (result == "") {
                    result = "Closed Today";
                }
            }
        } else {
            result = "Unavailable";
        }
        return result;
    }

    private int switchSunday(int dayOfWeek) {
        if (dayOfWeek == 7) {
            dayOfWeek = 0;
        }
        return dayOfWeek;
    }
}
