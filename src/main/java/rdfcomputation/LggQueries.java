package rdfcomputation;

import org.apache.jena.rdf.model.*;
import rdf.DictionaryNode;
import rdfio.SPARQLFileIO;
import tools.DefaultParameter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * LggQueries est la classe qui effectue le calcul de LGG sur les rêquetes.
 *
 * @see RDFComputation
 * @version 1.0.0
 */
public class LggQueries extends RDFComputation{

    /**
     * @see RDFComputation#RDFComputation()
     */
    public LggQueries(){
        super();
    }


    /**
     * @see RDFComputation#RDFComputation(Model, Model)
     */
    public LggQueries(Model query1,Model query2){
        super(query1, query2);
    }

    /**
     * @param dictionaryname Chemin vers le fichier du dictionnaire
     * @return Objet Model
     * @see RDFComputation#productGraph()
     */
    public Model productGraph(String dictionaryname) {
        return null;
    }

    public boolean lggexists() {
        final Map<String, String> e = new HashMap<>();
        System.out.println("size = " + vars1.size());
        DictionaryNode dictionaryBN = DictionaryNode.getInstance();
        for (int j = 0; j < vars1.size(); ++j) {
            final StmtIterator i = this.resultProd.listStatements();
            while (i.hasNext()) {
                final Statement s = i.nextStatement();
                System.out.println(dictionaryBN.size() + " s=" + s.toString());
                System.out.println("v_" + dictionaryBN.get(vars1.get(j)) + "_" + dictionaryBN.get(vars2.get(j)));
                if (s.toString().contains("v_" + dictionaryBN.get(vars1.get(j)) + "_" + dictionaryBN.get(vars2.get(j)))) {
                    e.put("v_" + dictionaryBN.get(vars1.get(j)) + "_" + dictionaryBN.get(vars2.get(j)), "true");
                    break;
                }
                e.put("v_" + dictionaryBN.get(vars1.get(j)) + "_" + dictionaryBN.get(vars2.get(j)), "false");
            }
        }
        return !e.containsValue("false");
    }


    public void writelgg() {
        String filepath = DefaultParameter.outputDirectoryUsed + "/Lgg" +
                DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].substring(0, DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].length() - 4) + DefaultParameter.graphPath2.split("/")[DefaultParameter.graphPath2.split("/").length - 1].substring(0, DefaultParameter.graphPath2.split("/")[DefaultParameter.graphPath2.split("/").length - 1].length() - 4)
                + ".sparql" ;
        SPARQLFileIO sparqlFileIO = new SPARQLFileIO(this);
        sparqlFileIO.save(filepath);
    }


    @Override
    public boolean lggQueryexists() {
        return this.lggexists() ;
    }
}
