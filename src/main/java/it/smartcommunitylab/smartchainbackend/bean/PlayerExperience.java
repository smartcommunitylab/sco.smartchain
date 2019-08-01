package it.smartcommunitylab.smartchainbackend.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonView;

import it.smartcommunitylab.smartchainbackend.controller.JsonVisibility;
import it.smartcommunitylab.smartchainbackend.model.GameModel.ModelExperience;

public class PlayerExperience {
    @JsonView(JsonVisibility.Public.class)
    private String experienceId;

    @JsonView(JsonVisibility.Public.class)
    private String name;

    @JsonView(JsonVisibility.Public.class)
    private String description;

    @JsonView(JsonVisibility.Public.class)
    private String iconUrl;

    @JsonView(JsonVisibility.Public.class)
    private List<PlayerCertificationAction> certificationActions = new ArrayList<>();

    public PlayerExperience(ModelExperience exp) {
        this(exp, false);
    }

    private void setFields(ModelExperience exp) {
        this.experienceId = exp.getExperienceId();
        this.name = exp.getName();
        this.description = exp.getDescription();
        this.iconUrl = exp.getIconUrl();
    }

    public PlayerExperience(ModelExperience exp, boolean completed) {
        setFields(exp);
        this.certificationActions = exp.getCertificationActions().stream()
                .map(ca -> new PlayerCertificationAction(ca, completed))
                .collect(Collectors.toList());
    }

    public PlayerExperience(ModelExperience experience,
            Collection<String> completedCertifications) {
        setFields(experience);
        this.certificationActions = experience.getCertificationActions().stream()
                .map(ca -> new PlayerCertificationAction(ca,
                        completedCertifications.contains(ca.getCertificationId())))
                .collect(Collectors.toList());

    }
}
