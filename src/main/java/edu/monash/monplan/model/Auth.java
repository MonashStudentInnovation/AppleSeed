package edu.monash.monplan.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.threewks.gaetools.search.SearchIndex;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.monplan.utils.TextSearch;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
public class Auth {

    @Id
    private String id;

    private String clientID;
    private String privateKey;
    private List<String> allowedAddresses;

    public List<String> getAllowedAddresses() {
        return allowedAddresses;
    }

    public void setAllowedAddresses(List<String> allowedAddresses) {
        this.allowedAddresses = allowedAddresses;
    }


    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientID", clientID)
                .append("privateKey", privateKey)
                .toString();
    }

}
