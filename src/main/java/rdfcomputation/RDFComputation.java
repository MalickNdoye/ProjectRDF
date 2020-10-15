package rdfcomputation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.thrift.TProcessor;

import java.util.ArrayList;

public class RDFComputation {
    //RDF Graphs
    protected Model query1 ;
    protected Model query2 ;
    protected ArrayList<String> vars1; // should contain head variables of query1 but query1 is a graph
    protected ArrayList<String> vars2; // should contain head variables of query2 but query2 is a graph


    public RDFComputation(){
        query1 = ModelFactory.createDefaultModel();
        query2 = ModelFactory.createDefaultModel();
        vars1 = new ArrayList<String>();
        vars2 = new ArrayList<String>();
    }

    public RDFComputation(Model query1,Model query2){
        this.query1 = query1;
        this.query2 = query2;
        vars1 = new ArrayList<String>();
        vars2 = new ArrayList<String>();
    }

    public Model getQuery1() {
        return query1;
    }

    public void setQuery1(Model query1) {
        this.query1 = query1;
    }

    public Model getQuery2() {
        return query2;
    }

    public void setQuery2(Model query2) {
        this.query2 = query2;
    }

    public ArrayList<String> getVars1() {
        return vars1;
    }

    public void setVars1(ArrayList<String> vars1) {
        this.vars1 = vars1;
    }

    public ArrayList<String> getVars2() {
        return vars2;
    }

    public void setVars2(ArrayList<String> vars2) {
        this.vars2 = vars2;
    }
}
