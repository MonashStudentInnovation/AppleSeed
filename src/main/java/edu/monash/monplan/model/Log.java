package edu.monash.monplan.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.threewks.gaetools.search.SearchIndex;
import org.joda.time.DateTime;
import org.monplan.abstraction_layer.DataModel;

import java.util.ArrayList;

@Entity
public class Log extends DataModel {

    public static String codeField = "";
    public static String nameField = "";

    @Id
    private String id;

    @Index
    private DateTime requestTime;

    @Index
    private String endpoint;

    @Index
    @SearchIndex
    private ArrayList<String> queryParams;

    @Index
    @SearchIndex
    private String payload;

    public Log() {}

    public Log(DateTime a_requestTime, String a_endpoint, ArrayList<String> a_queryParams, String a_requestBody) {
        requestTime = a_requestTime;
        endpoint = a_endpoint;
        queryParams = a_queryParams;
        payload = a_requestBody;
    }

    @Override
    public String fetchCode() {
        return null;
    }

    @Override
    public String fetchName() {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getCodeField() {
        return codeField;
    }

    public static void setCodeField(String codeField) {
        Log.codeField = codeField;
    }

    public static String getNameField() {
        return nameField;
    }

    public static void setNameField(String nameField) {
        Log.nameField = nameField;
    }

    public DateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(DateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public ArrayList<String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(ArrayList<String> queryParams) {
        this.queryParams = queryParams;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
