package rdfcomputation;

import org.apache.jena.rdf.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LggQueries extends RDFComputation{
    private final Map<String, String> blanknodes;
    private final Map<String, String> prefixs;
    private final Model resultProd ;

    public LggQueries(){
        super();
        resultProd = ModelFactory.createDefaultModel();
        this.blanknodes = new HashMap<String, String>();
        this.prefixs = new HashMap<String, String>();
    }

    public  LggQueries(Model query1,Model query2){
        super(query1, query2);
        resultProd = ModelFactory.createDefaultModel();
        this.blanknodes = new HashMap<String, String>();
        this.prefixs = new HashMap<String, String>();
    }

    public Model ProductGraph(String dictionaryname) {
        return null;
    }


}
