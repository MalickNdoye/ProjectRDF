package rdfcomputation;

import org.apache.jena.rdf.model.*;
import rdf.DictionaryNode;
import tools.DefaultParameter;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * RDFComputation est la classe qui généralise toutes les classes de traitement et calcul.
 *
 * <p>
 *  Les attributs nécessaires aux traitements sont :
 * <ul>
 * <li>Deux graphes RDF.</li>
 * <li>Ensembles regroupant les entêtes de requêtes.</li>
 * <li>Ensembles des noeuds anonymes.</li>
 * <li>Ensemble des préfixes d'URI.</li>
 * </ul>
 * </p>
 * <p>
 * De plus, un Zéro a une liste d'amis Zéro. Le membre pourra ajouter ou enlever
 * des amis à cette liste.
 * </p>
 * @version 1.0.0
 */
public abstract class RDFComputation {
    //RDF Graphs
    /**
     * Object Model du graphe 1.
     */
    protected Model query1;
    /**
     * Object Model du graphe 2.
     */
    protected Model query2;
    /**
     * Object Model du graphe des points communs.
     */
    protected Model resultProd;
    /**
     * Liste des entêtes de requêtes du graphe 1.
     */
    protected ArrayList<String> vars1; // should contain head variables of query1 but query1 is a graph
    /**
     * Liste des entêtes de requêtes du graphe 2.
     */
    protected ArrayList<String> vars2; // should contain head variables of query2 but query2 is a graph
    /**
     * Dictionaires des noeuds anonymes.
     */
    protected HashMap<String, String> blanknodes;
    /**
     * Dictionnaires des préfixes.
     */
    protected HashMap<String, String> prefixs;


    /**
     * Constructeur par defaut.
     * Elle initialise tous les attibuts et les graphes sont vides.
     */
    public RDFComputation() {
        query1 = ModelFactory.createDefaultModel();
        query2 = ModelFactory.createDefaultModel();
        resultProd = null;
        vars1 = new ArrayList<>();
        vars2 = new ArrayList<>();
        prefixs = new HashMap<>();
        blanknodes = new HashMap<>();
    }

    /**
     * Constructeur avec des objets Model déjà initialisés.
     * @param query1 Model du graphe 1.
     * @param query2 Model du graphe 2.
     */
    public RDFComputation(Model query1, Model query2) {
        this.query1 = query1;
        this.query2 = query2;
        resultProd = null;
        vars1 = new ArrayList<>();
        vars2 = new ArrayList<>();
        prefixs = new HashMap<>();
        blanknodes = new HashMap<>();
        this.loadAttributes(3);

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
     * Charge les attributs liés au graphe indiqué.
     * @param number Numéro du graphe dont les attributs sont chargés.
     */
    private void loadAttributes(int number) {
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
                this.loadAttributes(1);
                this.loadAttributes(2);
                break;
        }
        if (query != null) {
            StmtIterator iter = query.listStatements();
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();  // get next statement
                Resource subject = stmt.getSubject();     // get the subject
                Property predicate = stmt.getPredicate();   // get the predicate
                RDFNode object = stmt.getObject();      // get the object

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

                String[] spl = ligne.split(" ");
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

    /**
     * Determine si un URI est une entête de requête ou non.
     * @param s URI.
     * @return True si c'est une entête, False sinon.
     */
    public boolean isNotVars(String s) {
        return s.charAt(0) != 'y' && s.charAt(0) != '?';
    }

    /**
     * Retourne la liste des entêtes du graphe 1.
     * @return Liste des entêtes du graphe 1.
     * @see #vars1
     */
    public ArrayList<String> getVars1() {
        return vars1;
    }
    
    /**
     * Retourne la liste des entêtes du graphe 2.
     * @return Liste des entêtes du graphe 2.
     * @see #vars2
     */
    public ArrayList<String> getVars2() {
        return vars2;
    }

    /**
     * Retourne l'objet Model du graphe 1.
     * @return Objet Model du graphe 1.
     * @see #query1
     */
    public Model getQuery1() {
        return query1;
    }

    /**
     * Retourne l'objet Model du graphe 2.
     * @return Objet Model du graphe 2.
     * @see #query2
     */
    public Model getQuery2() {
        return query2;
    }

    /**
     * Retourne le dictionnaire des préfixes.
     * @return Dictionaire des préfixes.
     */
    public HashMap<String, String> getPrefixs() {
        return prefixs;
    }

    /**
     * Retourne le dictionnaire des noeuds anonymes.
     * @return Dictionnaire des noeuds anonymes.
     */
    public HashMap<String, String> getBlanknodes() {
        return blanknodes;
    }

    public Model getResultProd(){ return resultProd; }

    public abstract boolean lggQueryexists() ;

    public abstract void writelgg() ;
}
