package edu.monash.monplan.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Rules {

    public static final class Fields {
        public static final String Code = "unitCode";
    }

    @Id
    private String id;

    private String unitCode;

    private String RULE_TEXT;


    public Rules() {
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

    public String getRULE_TEXT() { return RULE_TEXT; }

    public void setRULE_TEXT(String RULE_TEXT) {
        this.RULE_TEXT = RULE_TEXT;
    }
}
