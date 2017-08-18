package edu.monash.monplan.service;

import com.threewks.gaetools.logger.Logger;
import com.threewks.gaetools.search.Is;
import edu.monash.monplan.model.Diesel;
import edu.monash.monplan.repository.DieselRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.asList;

@Service
public class DieselService {

    private final DieselRepository dieselRepository;

    public DieselService(DieselRepository dieselRepository) {
        this.dieselRepository = dieselRepository;
    }

    public void doIt() {
        List<String> strings = asList("AA", "AB", "BB", "CC");
        long b = strings.stream().filter(s -> s.contains("B")).count();

        Logger.info("count = " + b);

        Diesel diesel = new Diesel();
        diesel.setIndexedThing("testing index yo");
        diesel.setSearchIndexedThing("hello i am in the text search index");

        dieselRepository.put(diesel);

        List<Diesel> byField = dieselRepository.getByField("indexedThing", "index");
        System.out.println(byField);
        List<Diesel> byField2 = dieselRepository.getByField("indexedThing", "testing index yo");
        System.out.println(byField2);
        List<Diesel> results = dieselRepository.search().field("searchIndexedThing", Is.EqualTo, "edu/monash/monplan").run().getResults();
        System.out.println(results);

    }

}
