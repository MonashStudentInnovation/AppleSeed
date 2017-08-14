package edu.monash.monplan.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.threewks.gaetools.search.SearchIndex;

@Entity
public class Diesel {

    @Id
    private Long id;

    @Index
    private String indexedThing;

    @SearchIndex
    private String searchIndexedThing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndexedThing() {
        return indexedThing;
    }

    public void setIndexedThing(String indexedThing) {
        this.indexedThing = indexedThing;
    }

    public String getSearchIndexedThing() {
        return searchIndexedThing;
    }

    public void setSearchIndexedThing(String searchIndexedThing) {
        this.searchIndexedThing = searchIndexedThing;
    }

}
