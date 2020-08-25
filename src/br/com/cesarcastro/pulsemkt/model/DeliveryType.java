package br.com.cesarcastro.pulsemkt.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;


public class DeliveryType {

    @Expose
    private Integer id;
    @Expose
    private String description;

    public DeliveryType(){}

    public DeliveryType(Integer id, String description){
        this.id = id;
        this.description = description;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId(){
        return this.id;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public String toString(){
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create().toJson(this);
    }

    public static DeliveryType formJson(String json){
        return new Gson().fromJson(json, DeliveryType.class);
    }
}
