package rdfcomputation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import rdf.DictionaryNode;
import tools.DefaultParameter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * LggGraphs est la classe qui effectue le calcul de LGG sur des graphes.
 *
 * @see RDFComputation
 * @version 1.0.0
 */
public class LggGraphs extends RDFComputation{

    /**
     * @see RDFComputation#RDFComputation()
     */
    public LggGraphs(){
        super();
    }

    /**
     * @see RDFComputation#RDFComputation(Model, Model)
     */
    public LggGraphs(Model query1,Model query2){
        super(query1, query2);
    }

    /**
     * Calcule le graphe des points communs.
     * @return Objet Model du graphe des points communs.
     */
    public Model productGraph() {
        resultProd = ModelFactory.createDefaultModel();
        DictionaryNode dictionaryBN = DictionaryNode.getInstance(DefaultParameter.dictionaryPathUsed);
        for(Statement stmt1 : query1.listStatements().toList()){
            dictionaryBN.update(stmt1.getSubject().toString());
            dictionaryBN.update(stmt1.getObject().toString());
            for(Statement stmt2 : query2.listStatements().toList()){
                dictionaryBN.update(stmt2.getSubject().toString());
                dictionaryBN.update(stmt2.getObject().toString());
                if (stmt1.getPredicate().equals(stmt2.getPredicate())) {
                    if (stmt1.getSubject().equals(stmt2.getSubject()) && this.isNotVars(stmt1.getSubject().toString())) {
                        if (stmt1.getObject().equals(stmt2.getObject()) && this.isNotVars(stmt1.getObject().toString())) {
                            resultProd.add(stmt1);
                        } else {
                            String var1 = String.format("v__%d__%d", dictionaryBN.get(stmt1.getObject().toString()),
                                    dictionaryBN.get(stmt2.getObject().toString()));
                            resultProd.add(stmt1.getSubject(), stmt1.getPredicate(), resultProd.createResource(var1));
                        }
                    } else {
                        String var1 = String.format("v__%d__%d",
                                dictionaryBN.get(stmt1.getSubject().toString()),
                                dictionaryBN.get(stmt2.getSubject().toString()));
                        if (stmt1.getObject().equals(stmt2.getObject()) && this.isNotVars(stmt1.getObject().toString())) {
                            resultProd.add(resultProd.createResource(var1), stmt1.getPredicate(), stmt1.getObject());
                        } else {
                            String var2 = String.format("v__%d__%d", dictionaryBN.get(stmt1.getObject().toString()),
                                    dictionaryBN.get(stmt2.getObject().toString()));
                            resultProd.add(resultProd.createResource(var1), stmt1.getPredicate(),
                                    resultProd.createResource(var2));
                        }
                    }
                }
            }
        }
        dictionaryBN.save();
        return resultProd;
    }

    /**
     * Calcule le LGG de deux graphes. Le résultat est stocké dans resultProd.
     * @see RDFComputation
     */
    public void writelgg() {
        PrintWriter writer ;
        try {
            writer = new PrintWriter(new FileWriter(DefaultParameter.graphResult));
            if (resultProd == null) {
                this.productGraph();
            }
            StmtIterator si = resultProd.listStatements();
            while (si.hasNext()) {
                Statement s = si.nextStatement();
                if (s.getSubject().toString().charAt(0) == 'v') {
                    if (s.getPredicate().toString().charAt(0) == 'v') {
                        if (s.getObject().toString().charAt(0) == 'v') {
                            writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " ?" + s.getPredicate().toString().replaceAll("\\.", "") + " ?" + s.getObject().toString().replaceAll("\\.", "") + " .\n");
                        } else if (s.getObject().isLiteral()) {
                            writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " ?" + s.getPredicate().toString().replaceAll("\\.", "") + " \"" + s.getObject() + "\" .\n");
                        }
                        else {
                            writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " ?" + s.getPredicate().toString().replaceAll("\\.", "") + " <" + s.getObject() + "> .\n");
                        }
                    }
                    else if (s.getObject().toString().charAt(0) == 'v') {
                        writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " <" + s.getPredicate() + "> ?" + s.getObject().toString().replaceAll("\\.", "") + " .\n");
                    }
                    else if (s.getObject().isLiteral()) {
                        writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " <" + s.getPredicate() + "> \"" + s.getObject() + "\" .\n");
                    }
                    else {
                        writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " <" + s.getPredicate() + "> <" + s.getObject() + "> .\n");
                    }
                }
                else if (s.getPredicate().toString().charAt(0) == 'v') {
                    if (s.getObject().toString().charAt(0) == 'v') {
                        writer.write("<" + s.getSubject() + "> ?" + s.getPredicate().toString().replaceAll("\\.", "") + " ?" + s.getObject().toString().replaceAll("\\.", "") + " .\n");
                    }
                    else if (s.getObject().isLiteral()) {
                        writer.write("<" + s.getSubject() + "> ?" + s.getPredicate().toString().replaceAll("\\.", "") + " \"" + s.getObject() + "\" .\n");
                    }
                    else {
                        writer.write("<" + s.getSubject() + "> ?" + s.getPredicate().toString().replaceAll("\\.", "") + " <" + s.getObject() + "> .\n");
                    }
                }
                else if (s.getObject().toString().charAt(0) == 'v') {
                    writer.write("<" + s.getSubject() + "> <" + s.getPredicate() + "> ?" + s.getObject().toString().replaceAll("\\.", "") + " .\n");
                }
                else if (s.getObject().isLiteral()) {
                    writer.write("<" + s.getSubject() + "> <" + s.getPredicate() + "> \"" + s.getObject() + "\" .\n");
                }
                else {
                    writer.write("<" + s.getSubject() + "> <" + s.getPredicate() + "> <" + s.getObject() + "> .\n");
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean lggQueryexists() {
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
