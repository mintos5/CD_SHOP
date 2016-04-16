package sk.stuba.fiit.michal.nikolas.data.model;

import android.graphics.Bitmap;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nikolas on 31.3.2016.
 */
public class Album {

    long id;
    String name;
    String artist;
    int genre;
    int decade;
    int country;
    String description;
    int price;
    int count;
    Date releaseDate;
    List<String> songs;
    Date createdAt;
    Date updatedAt;
    String recordHash;
    boolean sales;
    String url;
    Bitmap picture;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public boolean getSales() {
        return sales;
    }

    public void setSales(boolean sales) {
        this.sales = sales;
    }

    /*public void generateUUID(){
        this.recordHash = UUID.randomUUID().toString();
    }*/

    public String getRecordHash() {
        return recordHash;
    }

    public void setRecordHash(String recordHash) {
        this.recordHash = recordHash;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public int getDecade() {
        return decade;
    }

    public void setDecade(int decade) {
        this.decade = decade;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean parseSongsFromString(String songsText) {
        List<String> help = Arrays.asList(songsText.split("\\s*;\\s*"));
        this.songs = new ArrayList<String>(help);
        return (this.songs != null);
    }

    public List<String> getSongs() {
        return songs;
    }

    public boolean setReleaseDateFromString(String datetime) {

        Timestamp unixTime = new Timestamp(Long.valueOf(datetime));
        this.setReleaseDate(new Date(unixTime.getTime()));

        return true;
    }
}
