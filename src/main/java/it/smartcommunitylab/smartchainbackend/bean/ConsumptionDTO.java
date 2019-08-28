package it.smartcommunitylab.smartchainbackend.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonView;

import it.smartcommunitylab.smartchainbackend.controller.JsonVisibility;
import it.smartcommunitylab.smartchainbackend.model.Subscription.Consumption;

public class ConsumptionDTO {

    @JsonView(JsonVisibility.Public.class)
    private String id;
    @JsonView(JsonVisibility.Public.class)
    private Date timestamp;


    public ConsumptionDTO(Consumption consumption) {
        if (consumption != null) {
            id = consumption.getId();
            timestamp = consumption.getTimestamp();
        }
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public Date getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


}
