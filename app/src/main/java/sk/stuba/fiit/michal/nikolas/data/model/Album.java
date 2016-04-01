package sk.stuba.fiit.michal.nikolas.data.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import sk.stuba.fiit.michal.nikolas.cd_shop.model.GenresEnum;

/**
 * Created by Nikolas on 31.3.2016.
 */
public class Album {

    long id;
    String name;
    String artist;
    GenresEnum genre;
    int decade;
    int country;
    String description;
    int price;
    int rating;
    Date releaseDate;
    List<String> songs;
    Date createdAt;
    Date updatedAt;
    String recordHash;

    public void generateUUID(){
        this.recordHash = UUID.randomUUID().toString();
    }

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

    public GenresEnum getGenre() {
        return genre;
    }

    public void setGenre(GenresEnum genre) {
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
        this.songs = Arrays.asList(songsText.split("\\s*,\\s*"));
        return (this.songs != null);
    }

    public List<String> getSongs() {
        return songs;
    }

    public boolean setReleaseDateFromString(String datetime) {
        //TODO: MAGIC
//        String dateInString = new java.text.SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss")
//                .format(cal.getTime())
//        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss");
//        Date parsedDate = formatter.parse(dateInString);
        return  true;

    }
}
