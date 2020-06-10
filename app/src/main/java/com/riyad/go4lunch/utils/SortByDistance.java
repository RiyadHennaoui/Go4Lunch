package com.riyad.go4lunch.utils;

import com.riyad.go4lunch.ui.Restaurant;

import java.util.Comparator;

public class SortByDistance implements Comparator<Restaurant> {
    @Override
    public int compare(Restaurant o1, Restaurant o2) {
        return o1.getDistance() -o2.getDistance();
    }
}
