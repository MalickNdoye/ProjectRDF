package rdfcomputation;

import org.apache.jena.rdf.model.*;
import org.apache.thrift.TProcessor;
import rdf.DictionaryNode;

import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Set;

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

    public Model ProductGraph(String dictionaryname) throws IOException {
        PrintWriter writerDic ;
        Model resultProd = ModelFactory.createDefaultModel();
        String varstmt2 = "";
        DictionaryNode dictionaryBN = DictionaryNode.getInstance(dictionaryname);
        int ctre = dictionaryBN.size() + 1;
        StmtIterator i = query1.listStatements();
        while (i.hasNext()) {
            Statement stmt1 = i.nextStatement();
            StmtIterator j = query2.listStatements();
            while (j.hasNext()) {
                Statement stmt2 = j.nextStatement();
                if (stmt1.getPredicate().equals((Object)stmt2.getPredicate())) {
                    if (stmt1.getSubject().equals((Object)stmt2.getSubject()) && !this.isVars(stmt1.getSubject().toString())) {
                        if (stmt1.getObject().equals(stmt2.getObject()) && !this.isVars(stmt1.getObject().toString())) {
                            resultProd.add(stmt1);
                        }
                        else {
                            if (!dictionaryBN.containsKey(stmt1.getObject().toString())) {
                                dictionaryBN.put(stmt1.getObject().toString(), new StringBuilder().append(ctre).toString());
                                ++ctre;
                            }
                            if (!dictionaryBN.containsKey(stmt2.getObject().toString())) {
                                dictionaryBN.put(stmt2.getObject().toString(), new StringBuilder().append(ctre).toString());
                                ++ctre;
                            }
                            String varstmt3 = dictionaryBN.get(stmt1.getObject().toString());
                            varstmt2 = dictionaryBN.get(stmt2.getObject().toString());
                            String var1 = "v__" + varstmt3 + "__" + varstmt2;
                            Resource rs = resultProd.createResource(var1);
                            resultProd.add(stmt1.getSubject(), stmt1.getPredicate(), (RDFNode)rs);
                        }
                    }
                    else if (stmt1.getObject().equals(stmt2.getObject()) && !this.isVars(stmt1.getObject().toString())) {
                        if (!dictionaryBN.containsKey(stmt1.getSubject().toString())) {
                            dictionaryBN.put(stmt1.getSubject().toString(), new StringBuilder().append(ctre).toString());
                            ++ctre;
                        }
                        if (!dictionaryBN.containsKey(stmt2.getSubject().toString())) {
                            dictionaryBN.put(stmt2.getSubject().toString(), new StringBuilder().append(ctre).toString());
                            ++ctre;
                        }
                        String varstmt3 = dictionaryBN.get(stmt1.getSubject().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getSubject().toString());
                        String var1 = "v__" + varstmt3 + "__" + varstmt2;
                        Resource rs = resultProd.createResource(var1);
                        resultProd.add(rs, stmt1.getPredicate(), stmt1.getObject());
                    }
                    else {
                        if (!dictionaryBN.containsKey(stmt1.getSubject().toString())) {
                            dictionaryBN.put(stmt1.getSubject().toString(), new StringBuilder().append(ctre).toString());
                            ++ctre;
                        }
                        if (!dictionaryBN.containsKey(stmt2.getSubject().toString())) {
                            dictionaryBN.put(stmt2.getSubject().toString(), new StringBuilder().append(ctre).toString());
                            ++ctre;
                        }
                        String varstmt3 = dictionaryBN.get(stmt1.getSubject().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getSubject().toString());
                        String var1 = "v__" + varstmt3 + "__" + varstmt2;
                        if (!dictionaryBN.containsKey(stmt1.getObject().toString())) {
                            dictionaryBN.put(stmt1.getObject().toString(), new StringBuilder().append(ctre).toString());
                            ++ctre;
                        }
                        if (!dictionaryBN.containsKey(stmt2.getObject().toString())) {
                            dictionaryBN.put(stmt2.getObject().toString(), new StringBuilder().append(ctre).toString());
                            ++ctre;
                        }
                        varstmt3 = dictionaryBN.get(stmt1.getObject().toString());
                        varstmt2 = dictionaryBN.get(stmt2.getObject().toString());
                        String var2 = "v__" + varstmt3 + "__" + varstmt2;
                        Resource rs2 = resultProd.createResource(var1);
                        Resource rs3 = resultProd.createResource(var2);
                        resultProd.add(rs2, stmt1.getPredicate(), (RDFNode)rs3);
                    }
                }
            }
        }
        dictionaryBN.save();
        return resultProd;
    }

    public boolean isVars(String s) {
        return s.substring(0, 1).equals("y") || s.substring(0, 1).equals("?");
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