package rdfcomputation;

import org.apache.jena.rdf.model.*;
import rdf.DictionaryNode;

import java.util.ArrayList;
import java.util.HashMap;

public class RDFComputation {
    //RDF Graphs
    protected Model query1;
    protected Model query2;
    protected Model resultProd;
    protected ArrayList<String> vars1; // should contain head variables of query1 but query1 is a graph
    protected ArrayList<String> vars2; // should contain head variables of query2 but query2 is a graph
    protected HashMap<String, String> blanknodes;
    protected HashMap<String, String> prefixs;


    public RDFComputation() {
        query1 = ModelFactory.createDefaultModel();
        query2 = ModelFactory.createDefaultModel();
        resultProd = null;
        vars1 = new ArrayList<>();
        vars2 = new ArrayList<>();
        prefixs = new HashMap<>();
        blanknodes = new HashMap<>();
    }

    public RDFComputation(Model query1, Model query2) {
        this.query1 = query1;
        this.query2 = query2;
        resultProd = null;
        vars1 = new ArrayList<>();
        vars2 = new ArrayList<>();
        prefixs = new HashMap<>();
        blanknodes = new HashMap<>();
        this.load(3);

    }

    public Model ProductGraph(String dictionaryname) {
        resultProd = ModelFactory.createDefaultModel();
        DictionaryNode dictionaryBN = DictionaryNode.getInstance(dictionaryname);
        StmtIterator i = query1.listStatements();
        while (i.hasNext()) {
            Statement stmt1 = i.nextStatement();
            StmtIterator j = query2.listStatements();
            while (j.hasNext()) {
                Statement stmt2 = j.nextStatement();
                if (stmt1.getPredicate().equals(stmt2.getPredicate())) {
                    if (stmt1.getSubject().equals(stmt2.getSubject()) && this.isNotVars(stmt1.getSubject().toString())) {
                        if (stmt1.getObject().equals(stmt2.getObject()) && this.isNotVars(stmt1.getObject().toString())) {
                            resultProd.add(stmt1);
                        } else {
                            dictionaryBN.update(stmt1.getObject().toString());
                            dictionaryBN.update(stmt2.getObject().toString());
                            String var1 = String.format("v__%d__%d", dictionaryBN.get(stmt1.getObject().toString()),
                                    dictionaryBN.get(stmt2.getObject().toString()));
                            Resource rs = resultProd.createResource(var1);
                            resultProd.add(stmt1.getSubject(), stmt1.getPredicate(), rs);
                        }
                    } else {
                        dictionaryBN.update(stmt1.getObject().toString());
                        dictionaryBN.update(stmt2.getObject().toString());
                        String var1 = String.format("v__%d__%d", dictionaryBN.get(stmt1.getSubject().toString()),
                                dictionaryBN.get(stmt2.getSubject().toString()));
                        if (stmt1.getObject().equals(stmt2.getObject()) && this.isNotVars(stmt1.getObject().toString())) {
                            Resource rs = resultProd.createResource(var1);
                            resultProd.add(rs, stmt1.getPredicate(), stmt1.getObject());
                        } else {
                            dictionaryBN.update(stmt1.getObject().toString());
                            dictionaryBN.update(stmt2.getObject().toString());
                            String var2 = String.format("v__%d__%d", dictionaryBN.get(stmt1.getObject().toString()),
                                    dictionaryBN.get(stmt2.getObject().toString()));
                            Resource rs2 = resultProd.createResource(var1);
                            Resource rs3 = resultProd.createResource(var2);
                            resultProd.add(rs2, stmt1.getPredicate(), rs3);
                        }
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
                            if (!this.blanknodes.containsKey(spl[0].substring(2))) {
                                this.blanknodes.put(spl[0].substring(2), "y" + spl[0].substring(2));
                            }
                            if (spl[2].charAt(0) == '_') {
                                if (!this.blanknodes.containsKey(spl[2].substring(2))) {
                                    this.blanknodes.put(spl[2].substring(2), "y" + spl[2].substring(2));
                                }
                            } else if (spl[2].charAt(0) == '_') {
                                if (!this.blanknodes.containsKey(spl[0].substring(2))) {
                                    this.blanknodes.put(spl[0].substring(2), "y" + spl[0].substring(2));
                                }
                                if (!this.blanknodes.containsKey(spl[2].substring(2))) {
                                    this.blanknodes.put(spl[2].substring(2), "y" + spl[2].substring(2));
                                }
                            } else {
                                if (!this.blanknodes.containsKey(spl[0].substring(2))) {
                                    this.blanknodes.put(spl[0].substring(2), "y" + spl[0].substring(2));
                                }
                            }
                        } else if (spl[1].charAt(0) == '_') {
                            if (spl[2].charAt(0) == '_') {
                                if (!this.blanknodes.containsKey(spl[2].substring(2))) {
                                    this.blanknodes.put(spl[2].substring(2), "v_" + spl[2].substring(2));
                                }
                            }
                        } else if (spl[2].charAt(0) == '_') {
                            if (!this.blanknodes.containsKey(spl[2].substring(2))) {
                                this.blanknodes.put(spl[2].substring(2), "v_" + spl[2].substring(2));
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isNotVars(String s) {
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

}
