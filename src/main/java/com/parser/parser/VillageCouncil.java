package com.parser.parser;

import java.net.URL;
import java.util.List;
import java.util.Set;

public class VillageCouncil {

    private String title;

    private String head;

    private List<String> phones;

    private String image;

    private String email;

    private String address;

    private String oblast;

    private String oldRegion;

    private URL redirect;

    private String area;

    private String website;

    private Set<SellType> sells;

    private String services;

    private String villageCouncil;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public List<String> getPhones() {
        return phones;
    }

    public Set<SellType> getSells() {
        return sells;
    }

    public void setSells(Set<SellType> sells) {
        this.sells = sells;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getVillageCouncil() {
        return villageCouncil;
    }

    public void setVillageCouncil(String villageCouncil) {
        this.villageCouncil = villageCouncil;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOblast() {
        return oblast;
    }

    public void setOblast(String oblast) {
        this.oblast = oblast;
    }

    public String getOldRegion() {
        return oldRegion;
    }

    public void setOldRegion(String oldRegion) {
        this.oldRegion = oldRegion;
    }

    public URL getRedirect() {
        return redirect;
    }

    public void setRedirect(URL redirect) {
        this.redirect = redirect;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
