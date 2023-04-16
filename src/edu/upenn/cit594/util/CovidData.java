package edu.upenn.cit594.util;

import java.time.LocalDate;
import java.util.Date;

public class CovidData {
    public int zipCode;
    public LocalDate vaccination;
    public int positive;
    public int negative;
    public int partial;
    public int full;
    public int hospitalization;
    public int deaths;
    public int booster;

    /*
    public CovidData(int zipCode, Date vaccination, int positive, int negative, int partial, int full, int hospitalization, int deaths, int booster) {
        this.zipCode = zipCode;
        this.vaccination = vaccination;
        this.positive = positive;
        this.negative = negative;
        this.partial = partial;
        this.full = full;
        this.hospitalization = hospitalization;
        this.deaths = deaths;
        this.booster = booster;
    }
    */


    // getter methods
    public int getZipCode() {
        return zipCode;
    }

    public LocalDate getVaccination() {
        return vaccination;
    }

    public int getPositive() {
        return positive;
    }

    public int getNegative() {
        return negative;
    }

    public int getPartial() {
        return partial;
    }

    public int getFull() {
        return full;
    }

    public int getHospitalization() {
        return hospitalization;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBooster() {
        return booster;
    }

    // setter methods
    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public void setVaccination(LocalDate vaccination) {
        this.vaccination = vaccination;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }

    public void setPartial(int partial) {
        this.partial = partial;
    }

    public void setFull(int full) {
        this.full = full;
    }

    public void setHospitalization(int hospitalization) {
        this.hospitalization = hospitalization;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setBooster(int booster) {
        this.booster = booster;
    }

}