package rdfcomputation;

import org.apache.jena.rdf.model.*;
import rdf.DictionaryNode;

import java.util.ArrayList;
import java.util.HashMap;

public class RDFComputation {
    //RDF Graphs
    protected Model query1 ;
    protected Model query2 ;
    protected Model resultProd;
    protected ArrayList<String> vars1; // should contain head variables of query1 but query1 is a graph
    protected ArrayList<String> vars2; // should contain head variables of query2 but query2 is a graph
    protected HashMap<String, String> blanknodes;
    protected HashMap<String, String> prefixs;


    public RDFComputation(){
        query1 = ModelFactory.createDefaultModel();
        query2 = ModelFactory.createDefaultModel();
        resultProd = null ;
        vars1 = new ArrayList<String>();
        vars2 = new ArrayList<String>();
        prefixs = new HashMap<String, String>();
        blanknodes = new HashMap<String, String>();
    }

    public RDFComputation(Model query1,Model query2){
        this.query1 = query1;
        this.query2 = query2;
        resultProd = null ;
        vars1 = new ArrayList<String>();
        vars2 = new ArrayList<String>();
        prefixs = new HashMap<String, String>();
        blanknodes = new HashMap<String, String>();
        this.load(3);

    }

    public Model ProductGraph(String dictionaryname) {
        resultProd = ModelFactory.createDefaultModel();
        int varstmt2;
        DictionaryNode dictionaryBN = DictionaryNode.getInstance(dictionaryname);
        int ctre = dictionaryBN.size() + 1;
        StmtIterator i = query1.listStatements();
        while (i.hasNext()) {
            Statement stmt1 = i.nextStatement();
            StmtIterator j = query2.listStatements();
            while (j.hasNext()) {
                Statement stmt2 = j.nextStatement();
                if (stmt1.getPredicate().equals(stmt2.getPredicate())) {
                    if (stmt1.getSubject().equals(stmt2.getSubject()) && this.isVars(stmt1.getSubject().toString())) {
                        if (stmt1.getObject().equals(stmt2.getObject()) && this.isVars(stmt1.getObject().toString())) {
                            resultProd.add(stmt1);
                        }
                        else {
                            if (dictionaryBN.containsKey(stmt1.getObject().toString())) {
                                dictionaryBN.put(stmt1.getObject().toString(), ctre);
                                ++ctre;
                            }
                            if (dictionaryBN.containsKey(stmt2.getObject().toString())) {
                                dictionaryBN.put(stmt2.getObject().toString(), ctre);
                                ++ctre;
                            }
                            int varstmt1 = dictionaryBN.get(stmt1.getObject().toString());
                            varstmt2 = dictionaryBN.get(stmt2.getObject().toString());
                            String var1 = "v__" + varstmt1 + "__" + varstmt2;
                            Resource rs = resultProd.createResource(var1);
                            resultProd.add(stmt1.getSubject(), stmt1.getPredicate(), rs);
                        }
                    }
                    else if (stmt1.getObject().equals(stmt2.getObject()) && this.isVars(stmt1.getObject().toString())) {
                        if (dictionaryBN.containsKey(stmt1.getSubject().toString())) {
                            dictionaryBN.put(stmt1.getSubject().toString(), ctre);
                            ++ctre;
                        }
                        if (dictionaryBN.containsKey(stmt2.getSubject().toString())) {
                            dictionaryBN.put(stmt2.getSubject().toString(), ctre);
                            ++ctre;
                        }
                        int varstmt1 = dictionaryBN.get(stmt1.getSubject().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getSubject().toString());
                        String var1 = "v__" + varstmt1 + "__" + varstmt2;
                        Resource rs = resultProd.createResource(var1);
                        resultProd.add(rs, stmt1.getPredicate(), stmt1.getObject());
                    }
                    else {
                        if (dictionaryBN.containsKey(stmt1.getSubject().toString())) {
                            dictionaryBN.put(stmt1.getSubject().toString(), ctre);
                            ++ctre;
                        }
                        if (dictionaryBN.containsKey(stmt2.getSubject().toString())) {
                            dictionaryBN.put(stmt2.getSubject().toString(), ctre);
                            ++ctre;
                        }
                        int varstmt1 = dictionaryBN.get(stmt1.getSubject().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getSubject().toString());
                        String var1 = "v__" + varstmt1 + "__" + varstmt2;
                        if (dictionaryBN.containsKey(stmt1.getObject().toString())) {
                            dictionaryBN.put(stmt1.getObject().toString(), ctre);
                            ++ctre;
                        }
                        if (dictionaryBN.containsKey(stmt2.getObject().toString())) {
                            dictionaryBN.put(stmt2.getObject().toString(), ctre);
                            ++ctre;
                        }
                        varstmt1 = dictionaryBN.get(stmt1.getObject().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getObject().toString());
                        String var2 = "v__" + varstmt1 + "__" + varstmt2;
                        Resource rs2 = resultProd.createResource(var1);
                        Resource rs3 = resultProd.createResource(var2);
                        resultProd.add(rs2, stmt1.getPredicate(), rs3);
                    }
                }
            }
        }
        dictionaryBN.save();
        return resultProd;
    }

    public void load(int number) {
        ArrayList<String> vars = null;
        Model query = null;
        switch (number) {
            case 1:
                vars = this.vars1;
                query = query1;
                break;
            case 2:
                vars = this.vars2;
                query = this.query2;
                break;
            default:
                this.load(1);
                this.load(2);
                break;
        }
        if (query != null) {
            StmtIterator iter = query.listStatements();
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();  // get next statement
                Resource subject = stmt.getSubject();     // get the subject
                Property predicate = stmt.getPredicate();   // get the predicate
                RDFNode object = stmt.getObject();      // get the object
                //System.out.print(" " + predicate.toString() + " ");
                String ligne;
                String objectType;
                if (object instanceof Resource) {
                    objectType = object.toString();
                } else {
                    // object is a literal
                    objectType = " \"" + object.toString() + "\"";
                }
                ligne = String.format("<%s> <%s> %s",
                        subject.toString(),
                        predicate.toString(),
                        objectType);

                final String[] spl = ligne.split(" ");
                if (spl[0].equals("head")) {
                    for (int i = 1; i < spl.length; ++i) {
                        vars.add(spl[i].substring(2));
                    }
                } else if (spl[0].equals("@prefix")) {
                    this.prefixs.put(spl[1].substring(0, spl[1].length() - 1), spl[2].substring(1, spl[2].length() - 1));
                } else {
                    if (spl[0].length() <= 1 || spl[1].length() <= 1 || spl[2].length() <= 1) {
                        continue;
                    }
                    if (spl[0].charAt(0) == '_') {
                        if (spl[1].charAt(0) == '_') {
                            String var1 = "";
                            if (!this.blanknodes.containsKey(spl[0].substring(2))) {
                                var1 = "y" + spl[0].substring(2);
                                this.blanknodes.put(spl[0].substring(2), var1);
                            }
                            if (spl[2].charAt(0) == '_') {
                                String var2 = "";
                                if (!this.blanknodes.containsKey(spl[2].substring(2))) {
                                    var2 = "y" + spl[2].substring(2);
                                    this.blanknodes.put(spl[2].substring(2), var2);
                                }
                            } else if (spl[2].charAt(0) == '_') {
                                if (!this.blanknodes.containsKey(spl[0].substring(2))) {
                                    var1 = "y" + spl[0].substring(2);
                                    this.blanknodes.put(spl[0].substring(2), var1);
                                }
                                //final Resource rs1 = query.createResource(var1);
                                String var2 = "";
                                if (!this.blanknodes.containsKey(spl[2].substring(2))) {
                                    var2 = "y" + spl[2].substring(2);
                                    this.blanknodes.put(spl[2].substring(2), var2);
                                }
                            } else {
                                if (!this.blanknodes.containsKey(spl[0].substring(2))) {
                                    var1 = "y" + spl[0].substring(2);
                                    this.blanknodes.put(spl[0].substring(2), var1);
                                }
                            }
                        } else if (spl[1].charAt(0) == '_') {
                            if (spl[2].charAt(0) == '_') {
                                String var3 = "";
                                if (!this.blanknodes.containsKey(spl[2].substring(2))) {
                                    var3 = "v_" + spl[2].substring(2);
                                    this.blanknodes.put(spl[2].substring(2), var3);
                                }
                            }
                        } else if (spl[2].charAt(0) == '_') {
                            String var2 = "";
                            if (!this.blanknodes.containsKey(spl[2].substring(2))) {
                                var2 = "v_" + spl[2].substring(2);
                                this.blanknodes.put(spl[2].substring(2), var2);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isVars(String s) {
        return s.charAt(0) != 'y' && s.charAt(0) != '?';
    }

    public ArrayList<String> getVars2() {
        return vars2;
    }

    public ArrayList<String> getVars1() {
        return vars1;
    }

    public Model getQuery1() {
        return query1;
    }

    public Model getQuery2() {
        return query2;
    }

    public HashMap<String, String> getPrefixs() {
        return prefixs;
    }

    public HashMap<String, String> getBlanknodes() {
        return blanknodes;
    }

    /*
    public void setQuery1(Model query1) {
        this.query1 = query1;
    }

    public void setQuery2(Model query2) {
        this.query2 = query2;
    }

    public void setVars1(ArrayList<String> vars1) {
        this.vars1 = vars1;
    }

    public void setVars2(ArrayList<String> vars2) {
        this.vars2 = vars2;
    }
    */
}
