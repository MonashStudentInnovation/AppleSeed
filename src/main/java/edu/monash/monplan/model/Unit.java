package edu.monash.monplan.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Unit extends DataModel {

    public static final String nameField = "unitName";
    public static final String codeField = "unitCode";

    @Id
    private String id;

    @Index
    private String unitCode;

    @Index
    private String unitName;

    private String description;

    private Integer creditPoints;

    private Integer scaBand;

    private BigDecimal etfsl;

    private Integer enjoyResponse;

    private Integer learnResponse;

    private BigDecimal enjoyScore;

    private BigDecimal learnScore;

    public String fetchCode() {
        return unitCode;
    }

    public String fetchName() {
        return unitName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(Integer creditPoints) {
        this.creditPoints = creditPoints;
    }

    public Integer getScaBand() {
        return scaBand;
    }

    public void setScaBand(Integer scaBand) {
        this.scaBand = scaBand;
    }

    public BigDecimal getEtfsl() {
        return etfsl;
    }

    public void setEtfsl(BigDecimal etfsl) {
        this.etfsl = etfsl;
    }

    public Integer getEnjoyResponse() {
        return enjoyResponse;
    }

    public void setEnjoyResponse(Integer enjoyResponse) {
        this.enjoyResponse = enjoyResponse;
    }

    public Integer getLearnResponse() {
        return learnResponse;
    }

    public void setLearnResponse(Integer learnResponse) {
        this.learnResponse = learnResponse;
    }

    public BigDecimal getEnjoyScore() {
        return enjoyScore;
    }

    public void setEnjoyScore(BigDecimal enjoyScore) {
        this.enjoyScore = enjoyScore;
    }

    public BigDecimal getLearnScore() {
        return learnScore;
    }

    public void setLearnScore(BigDecimal learnScore) {
        this.learnScore = learnScore;
    }

    public void init() {
        // Protects us from accidentally re-initialising an object that's retrieved from db
        this.setId(UUID.randomUUID().toString());
    }

}
