package rdfcomputation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import rdfio.SPARQLFileIO;
import tools.DefaultParameter;
import java.util.HashMap;
import java.util.Map;

/**
 * LggQueries est la classe qui effectue le calcul de LGG de requêtes.
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
     * Détermine l'existence du "Least General Generalization" (LGG).
     * @return Renvoie True si le LGG existe, False sinon.
     */
    public boolean lggExists() {
        Map<String, String> e = new HashMap<>();
        System.out.print("heads anti-unification: ");
        for (int j=0; j<getVars1().size(); j++) {
            for (StmtIterator i = resultProd.listStatements(); i.hasNext(); ) {
                Statement s = i.nextStatement();
                if (s.toString().contains("v_y"+getVars1().get(j)+"_y"+getVars2().get(j))) {
                    e.put("v_y"+getVars1().get(j)+"_y"+getVars2().get(j), "true");break;
                }
                else {
                    e.put("v_y"+getVars1().get(j)+"_y"+getVars2().get(j), "false");
                }
            }
        }
        System.out.println(e.toString());
        return !e.containsValue("false");
    }

    /**
     * Exporte le "Least General Generalization" (LGG) sur un fichier.
     */
    public void writeLGG() {
        SPARQLFileIO sparqlFileIO = new SPARQLFileIO(this);
        sparqlFileIO.save(DefaultParameter.graphResult);
    }


    /**
     * Calcule le produit des deux graphes.
     * @return produit des deux graphes.
     */
    public Model product() {
        resultProd = ModelFactory.createDefaultModel();
        String var1, var2, var3 ;
        for (StmtIterator i = query1.listStatements(); i.hasNext(); ) {
            Statement stmt1 = i.nextStatement();
            for (StmtIterator j = query2.listStatements(); j.hasNext(); ) {
                Statement stmt2 = j.nextStatement();
                if (stmt1.getSubject().equals(stmt2.getSubject()) && (isVars(stmt1.getSubject().toString()))) {
                    if (stmt1.getPredicate().equals(stmt2.getPredicate()) && (isVars(stmt1.getPredicate().toString()))) {
                        if (stmt1.getObject().equals(stmt2.getObject()) && (isVars(stmt1.getObject().toString()))) {
                            resultProd.add(stmt1);
                        }
                        else {
                            if (stmt1.getObject().toString().contains("#")) {
                                if (stmt2.getObject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var1 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject().toString().split("://")[1];
                                else
                                    var1 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject();
                            }
                            else if (stmt1.getObject().toString().contains("://")){
                                if (stmt2.getObject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var1 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject().toString().split("://")[1];
                                else
                                    var1 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject();
                            }
                            else {
                                if (stmt2.getObject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getObject()+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var1 = "v_"+stmt1.getObject()+"_"+stmt2.getObject().toString().split("://")[1];
                                else
                                    var1 = "v_"+stmt1.getObject()+"_"+stmt2.getObject();
                            }
                            Resource rs = resultProd.createResource(var1);
                            resultProd.add(stmt1.getSubject(), stmt1.getPredicate(), rs);
                        }
                    }
                    else {
                        if (stmt1.getObject().equals(stmt2.getObject()) && (isVars(stmt1.getObject().toString()))) {
                            if (stmt1.getPredicate().toString().contains("#")) {
                                if (stmt2.getPredicate().toString().contains("#"))
                                    var2 = "v_"+stmt1.getPredicate().toString().split("#")[1]+"_"+stmt2.getPredicate().toString().split("#")[1];
                                else if (stmt2.getPredicate().toString().contains("://"))
                                    var2 = "v_"+stmt1.getPredicate().toString().split("#")[1]+"_"+stmt2.getPredicate().toString().split("://")[1];
                                else
                                    var2 = "v_"+stmt1.getPredicate().toString().split("#")[1]+"_"+stmt2.getPredicate();
                            }
                            else if (stmt1.getPredicate().toString().contains("://")){
                                if (stmt2.getPredicate().toString().contains("#"))
                                    var2 = "v_"+stmt1.getPredicate().toString().split("://")[1]+"_"+stmt2.getPredicate().toString().split("#")[1];
                                else if (stmt2.getPredicate().toString().contains("://"))
                                    var2 = "v_"+stmt1.getPredicate().toString().split("://")[1]+"_"+stmt2.getPredicate().toString().split("://")[1];
                                else
                                    var2 = "v_"+stmt1.getPredicate().toString().split("://")[1]+"_"+stmt2.getPredicate();
                            }
                            else {
                                if (stmt2.getPredicate().toString().contains("#"))
                                    var2 = "v_"+stmt1.getPredicate()+"_"+stmt2.getPredicate().toString().split("#")[1];
                                else if (stmt2.getPredicate().toString().contains(":"))
                                    var2 = "v_"+stmt1.getPredicate()+"_"+stmt2.getPredicate().toString().split(":")[1];
                                else
                                    var2 = "v_"+stmt1.getPredicate()+"_"+stmt2.getPredicate();
                            }
                            Property pr = resultProd.createProperty(var2);
                            resultProd.add(stmt1.getSubject(), pr, stmt1.getObject());
                        }
                        else {
                            if (stmt1.getObject().toString().contains("#")) {
                                if (stmt2.getObject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var1 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject().toString().split("://")[1];
                                else
                                    var1 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject();
                            }
                            else if (stmt1.getObject().toString().contains("://")) {
                                if (stmt2.getObject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var1 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject().toString().split("://")[1];
                                else
                                    var1 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject();
                            }
                            else {
                                if (stmt2.getObject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getObject()+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var1 = "v_"+stmt1.getObject()+"_"+stmt2.getObject().toString().split(":")[1];
                                else
                                    var1 = "v_"+stmt1.getObject()+"_"+stmt2.getObject();
                            }

                            if (stmt1.getPredicate().toString().contains("#")) {
                                if (stmt2.getPredicate().toString().contains("#"))
                                    var2 = "v_"+stmt1.getPredicate().toString().split("#")[1]+"_"+stmt2.getPredicate().toString().split("#")[1];
                                else
                                    var2 = "v_"+stmt1.getPredicate().toString().split("#")[1]+"_"+stmt2.getPredicate();
                            }
                            else {
                                if (stmt2.getPredicate().toString().contains("#"))
                                    var2 = "v_"+stmt1.getPredicate()+"_"+stmt2.getPredicate().toString().split("#")[1];
                                else
                                    var2 = "v_"+stmt1.getPredicate()+"_"+stmt2.getPredicate();
                            }
                            Resource rs = resultProd.createResource(var1);
                            Property pr = resultProd.createProperty(var2);
                            resultProd.add(stmt1.getSubject(), pr, rs);
                        }
                    }
                }
                else {
                    if (stmt1.getPredicate().equals(stmt2.getPredicate()) && (isVars(stmt1.getPredicate().toString()))) {
                        if (stmt1.getObject().equals(stmt2.getObject()) && (isVars(stmt1.getObject().toString()))) {
                            if (stmt1.getSubject().toString().contains("#")) {
                                if (stmt2.getSubject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getSubject().toString().split("#")[1]+"_"+stmt2.getSubject().toString().split("#")[1];
                                else
                                    var1 = "v_"+stmt1.getSubject().toString().split("#")[1]+"_"+stmt2.getSubject();
                            }
                            else {
                                if (stmt2.getSubject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getSubject()+"_"+stmt2.getSubject().toString().split("#")[1];
                                else
                                    var1 = "v_"+stmt1.getSubject()+"_"+stmt2.getSubject();
                            }
                            Resource rs = resultProd.createResource(var1);
                            resultProd.add(rs, stmt1.getPredicate(), stmt1.getObject());
                        }
                        else {
                            if (stmt1.getSubject().toString().contains("#")) {
                                if (stmt2.getSubject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getSubject().toString().split("#")[1]+"_"+stmt2.getSubject().toString().split("#")[1];
                                else
                                    var1 = "v_"+stmt1.getSubject().toString().split("#")[1]+"_"+stmt2.getSubject();
                            }
                            else {
                                if (stmt2.getSubject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getSubject()+"_"+stmt2.getSubject().toString().split("#")[1];
                                else
                                    var1 = "v_"+stmt1.getSubject()+"_"+stmt2.getSubject();
                            }

                            if (stmt1.getObject().toString().contains("#")) {
                                if (stmt2.getObject().toString().contains("#"))
                                    var2 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var2 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject().toString().split("://")[1];
                                else
                                    var2 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject();
                            }
                            else if (stmt1.getObject().toString().contains("://")){
                                if (stmt2.getObject().toString().contains("#"))
                                    var2 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var2 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject().toString().split("://")[1];
                                else
                                    var2 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject();
                            }
                            else {
                                if (stmt2.getObject().toString().contains("#"))
                                    var2 = "v_"+stmt1.getObject()+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var2 = "v_"+stmt1.getObject()+"_"+stmt2.getObject().toString().split("://")[1];
                                else
                                    var2 = "v_"+stmt1.getObject()+"_"+stmt2.getObject();
                            }

                            Resource rs1 = resultProd.createResource(var1);
                            Resource rs2 = resultProd.createResource(var2);
                            resultProd.add(rs1, stmt1.getPredicate(), rs2);
                        }
                    }
                    else {
                        if (stmt1.getObject().equals(stmt2.getObject()) && (isVars(stmt1.getObject().toString()))) {
                            if (stmt1.getSubject().toString().contains("#")) {
                                if (stmt2.getSubject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getSubject().toString().split("#")[1]+"_"+stmt2.getSubject().toString().split("#")[1];
                                else
                                    var1 = "v_"+stmt1.getSubject().toString().split("#")[1]+"_"+stmt2.getSubject();
                            }
                            else {
                                if (stmt2.getSubject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getSubject()+"_"+stmt2.getSubject().toString().split("#")[1];
                                else
                                    var1 = "v_"+stmt1.getSubject()+"_"+stmt2.getSubject();
                            }

                            if (stmt1.getPredicate().toString().contains("#")) {
                                if (stmt2.getPredicate().toString().contains("#"))
                                    var2 = "v_"+stmt1.getPredicate().toString().split("#")[1]+"_"+stmt2.getPredicate().toString().split("#")[1];
                                else
                                    var2 = "v_"+stmt1.getPredicate().toString().split("#")[1]+"_"+stmt2.getPredicate();
                            }
                            else {
                                if (stmt2.getPredicate().toString().contains("#"))
                                    var2 = "v_"+stmt1.getPredicate()+"_"+stmt2.getPredicate().toString().split("#")[1];
                                else
                                    var2 = "v_"+stmt1.getPredicate()+"_"+stmt2.getPredicate();
                            }
                            Resource rs1 = resultProd.createResource(var1);
                            Property pr = resultProd.createProperty(var2);
                            resultProd.add(rs1, pr, stmt1.getObject());
                        }
                        else {
                            if (stmt1.getSubject().toString().contains("#")) {
                                if (stmt2.getSubject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getSubject().toString().split("#")[1]+"_"+stmt2.getSubject().toString().split("#")[1];
                                else
                                    var1 = "v_"+stmt1.getSubject().toString().split("#")[1]+"_"+stmt2.getSubject();
                            }
                            else {
                                if (stmt2.getSubject().toString().contains("#"))
                                    var1 = "v_"+stmt1.getSubject()+"_"+stmt2.getSubject().toString().split("#")[1];
                                else
                                    var1 = "v_"+stmt1.getSubject()+"_"+stmt2.getSubject();
                            }
                            if (stmt1.getPredicate().toString().contains("#")) {
                                if (stmt2.getPredicate().toString().contains("#"))
                                    var2 = "v_"+stmt1.getPredicate().toString().split("#")[1]+"_"+stmt2.getPredicate().toString().split("#")[1];
                                else
                                    var2 = "v_"+stmt1.getPredicate().toString().split("#")[1]+"_"+stmt2.getPredicate();
                            }
                            else {
                                if (stmt2.getPredicate().toString().contains("#"))
                                    var2 = "v_"+stmt1.getPredicate()+"_"+stmt2.getPredicate().toString().split("#")[1];
                                else
                                    var2 = "v_"+stmt1.getPredicate()+"_"+stmt2.getPredicate();
                            }

                            if (stmt1.getObject().toString().contains("#")) {
                                if (stmt2.getObject().toString().contains("#"))
                                    var3 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var3 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject().toString().split("://")[1];
                                else
                                    var3 = "v_"+stmt1.getObject().toString().split("#")[1]+"_"+stmt2.getObject();
                            }
                            else if (stmt1.getObject().toString().contains("://")){
                                if (stmt2.getObject().toString().contains("#"))
                                    var3 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var3 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject().toString().split("://")[1];
                                else
                                    var3 = "v_"+stmt1.getObject().toString().split("://")[1]+"_"+stmt2.getObject();
                            }
                            else {
                                if (stmt2.getObject().toString().contains("#"))
                                    var3 = "v_"+stmt1.getObject()+"_"+stmt2.getObject().toString().split("#")[1];
                                else if (stmt2.getObject().toString().contains("://"))
                                    var3 = "v_"+stmt1.getObject()+"_"+stmt2.getObject().toString().split("://")[1];
                                else
                                    var3 = "v_"+stmt1.getObject()+"_"+stmt2.getObject();
                            }

                            Resource rs1 = resultProd.createResource(var1);
                            Property pr = resultProd.createProperty(var2);
                            Resource rs2 = resultProd.createResource(var3);
                            resultProd.add(rs1, pr, rs2);
                        }
                    }
                }
            }

        }
        return resultProd;
    }

    /**
     * Determine si un URI est une entête de requête ou non.
     * @param s URI.
     * @return True si c'est une entête, False sinon.
     */
    public boolean isVars(final String s) {
        return s.charAt(0) != 'y' && s.charAt(0) != '?';
    }

}
