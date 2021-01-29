package rdfcomputation;

import org.apache.jena.rdf.model.*;
import rdf.DictionaryNode;
import rdfio.SPARQLFileIO;
import tools.DefaultParameter;
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
    @SuppressWarnings("unused")
    public LggQueries(Model query1, Model query2){
        super(query1, query2);
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

    public Model product() {
        DictionaryNode dictionaryBN = DictionaryNode.getInstance();
        this.resultProd = ModelFactory.createDefaultModel();
        int varstmt2 ;
        final StmtIterator i = query1.listStatements();
        while (i.hasNext()) {
            final Statement stmt1 = i.nextStatement();
            final StmtIterator j = query2.listStatements();
            while (j.hasNext()) {
                final Statement stmt2 = j.nextStatement();
                if (stmt1.getSubject().equals(stmt2.getSubject()) && !this.isVars(stmt1.getSubject().toString())) {
                    if (stmt1.getPredicate().equals(stmt2.getPredicate()) && !this.isVars(stmt1.getPredicate().toString())) {
                        if (stmt1.getObject().equals(stmt2.getObject()) && !this.isVars(stmt1.getObject().toString())) {
                            this.resultProd.add(stmt1);
                        }
                        else {
                            dictionaryBN.update(stmt1.getObject().toString());
                            dictionaryBN.update(stmt2.getObject().toString());
                            final int varstmt3 = dictionaryBN.get(stmt1.getObject().toString());
                            varstmt2 = dictionaryBN.get(stmt2.getObject().toString());
                            final String var1 = "v_" + varstmt3 + "_" + varstmt2;
                            final Resource rs = this.resultProd.createResource(var1);
                            this.resultProd.add(stmt1.getSubject(), stmt1.getPredicate(), rs);
                        }
                    }
                    else if (stmt1.getObject().equals(stmt2.getObject()) && !this.isVars(stmt1.getObject().toString())) {
                        dictionaryBN.update(stmt1.getPredicate().toString());
                        dictionaryBN.update(stmt2.getPredicate().toString());
                        final int varstmt3 = dictionaryBN.get(stmt1.getPredicate().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getPredicate().toString());
                        final String var2 = "v_" + varstmt3 + "_" + varstmt2;
                        final Property pr = this.resultProd.createProperty(var2);
                        this.resultProd.add(stmt1.getSubject(), pr, stmt1.getObject());
                        System.out.println(stmt1.getSubject().toString() + " var? " + this.isVars(stmt1.getSubject().toString()));
                    }
                    else {
                        dictionaryBN.update(stmt1.getObject().toString());
                        dictionaryBN.update(stmt2.getObject().toString());
                        int varstmt3 = dictionaryBN.get(stmt1.getObject().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getObject().toString());
                        final String var1 = "v_" + varstmt3 + "_" + varstmt2;
                        dictionaryBN.update(stmt1.getPredicate().toString());
                        dictionaryBN.update(stmt2.getPredicate().toString());
                        varstmt3 = dictionaryBN.get(stmt1.getPredicate().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getPredicate().toString());
                        final String var2 = "v_" + varstmt3 + "_" + varstmt2;
                        final Resource rs = this.resultProd.createResource(var1);
                        final Property pr2 = this.resultProd.createProperty(var2);
                        this.resultProd.add(stmt1.getSubject(), pr2, rs);
                    }
                }
                else if (stmt1.getPredicate().equals(stmt2.getPredicate()) && !this.isVars(stmt1.getPredicate().toString())) {
                    if (stmt1.getObject().equals(stmt2.getObject()) && !this.isVars(stmt1.getObject().toString())) {
                        dictionaryBN.update(stmt1.getSubject().toString());
                        dictionaryBN.update(stmt2.getSubject().toString());
                        final int varstmt3 = dictionaryBN.get(stmt1.getSubject().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getSubject().toString());
                        final String var1 = "v_" + varstmt3 + "_" + varstmt2;
                        final Resource rs = this.resultProd.createResource(var1);
                        this.resultProd.add(rs, stmt1.getPredicate(), stmt1.getObject());
                    }
                    else {
                        dictionaryBN.update(stmt1.getSubject().toString());
                        dictionaryBN.update(stmt2.getSubject().toString());
                        int varstmt3 = dictionaryBN.get(stmt1.getSubject().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getSubject().toString());
                        final String var1 = "v_" + varstmt3 + "_" + varstmt2;
                        dictionaryBN.update(stmt1.getObject().toString());
                        dictionaryBN.update(stmt2.getObject().toString());
                        varstmt3 = dictionaryBN.get(stmt1.getObject().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getObject().toString());
                        final String var2 = "v_" + varstmt3 + "_" + varstmt2;
                        System.out.println("*** " + stmt1.getObject().toString());
                        final Resource rs2 = this.resultProd.createResource(var1);
                        final Resource rs3 = this.resultProd.createResource(var2);
                        this.resultProd.add(rs2, stmt1.getPredicate(), rs3);
                        System.out.println("5*** = " + rs2.toString() + " " + stmt1.getPredicate().toString() + " " + rs3.toString());
                    }
                }
                else if (stmt1.getObject().equals(stmt2.getObject()) && !this.isVars(stmt1.getObject().toString())) {
                    dictionaryBN.update(stmt1.getSubject().toString());
                    dictionaryBN.update(stmt2.getSubject().toString());
                    int varstmt3 = dictionaryBN.get(stmt1.getSubject().toString());
                    varstmt2 = dictionaryBN.get(stmt2.getSubject().toString());
                    final String var1 = "v_" + varstmt3 + "_" + varstmt2;
                    dictionaryBN.update(stmt1.getPredicate().toString());
                    dictionaryBN.update(stmt2.getPredicate().toString());
                    varstmt3 = dictionaryBN.get(stmt1.getPredicate().toString());
                    varstmt2 = dictionaryBN.get(stmt2.getPredicate().toString());
                    final String var2 = "v_" + varstmt3 + "_" + varstmt2;
                    final Resource rs2 = this.resultProd.createResource(var1);
                    final Property pr2 = this.resultProd.createProperty(var2);
                    this.resultProd.add(rs2, pr2, stmt1.getObject());
                }
                else {
                    dictionaryBN.update(stmt1.getSubject().toString());
                    dictionaryBN.update(stmt2.getSubject().toString());
                    int varstmt3 = dictionaryBN.get(stmt1.getSubject().toString());
                    varstmt2 = dictionaryBN.get(stmt2.getSubject().toString());
                    final String var1 = "v_" + varstmt3 + "_" + varstmt2;
                    dictionaryBN.update(stmt1.getPredicate().toString());
                    dictionaryBN.update(stmt2.getPredicate().toString());
                    varstmt3 = dictionaryBN.get(stmt1.getPredicate().toString());
                    varstmt2 = dictionaryBN.get(stmt2.getPredicate().toString());
                    final String var2 = "v_" + varstmt3 + "_" + varstmt2;
                    dictionaryBN.update(stmt1.getObject().toString());
                    dictionaryBN.update(stmt2.getObject().toString());
                    varstmt3 = dictionaryBN.get(stmt1.getObject().toString());
                    varstmt2 = dictionaryBN.get(stmt2.getObject().toString());
                    final String var3 = "v_" + varstmt3 + "_" + varstmt2;
                    final Resource rs2 = this.resultProd.createResource(var1);
                    final Property pr2 = this.resultProd.createProperty(var2);
                    final Resource rs4 = this.resultProd.createResource(var3);
                    this.resultProd.add(rs2, pr2, rs4);
                }
            }
        }
        dictionaryBN.save();
        return this.resultProd;
    }

    public boolean isVars(final String s) {
        return s.charAt(0) == 'y' || s.charAt(0) == '?';
    }
}
