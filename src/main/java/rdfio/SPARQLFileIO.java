package rdfio;

import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import rdf.RDFModelFactory;
import rdfcomputation.LggQueries;
import rdfcomputation.RDFComputation;
import tools.DefaultParameter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Cette classe permet l'écriture dans un fichier SPARQL la requête correspondante à un graphe.
 * @see LggQueries
 */
public class SPARQLFileIO extends RDFFileIO{
    /**
     * Attribut contenant les informations de graphes et de requêtes.
     */
    private LggQueries lggQueries ;

    /**
     * Constructeur de la classe.
     * @param rdfComputation Objet de type LGGComputation.
     */
    public SPARQLFileIO(RDFComputation rdfComputation) {
        if(rdfComputation instanceof LggQueries){
            this.lggQueries = (LggQueries) rdfComputation ;
        } else {
            lggQueries = new LggQueries();
        }
    }

    /**
     * Constructeur standard.
     */
    public SPARQLFileIO(){
        lggQueries = new LggQueries();
    }

    /**
     * Écriture dans un fichier de la requête du Least General Generalization (LGG).
     * @param filepath Chemin défini vers le fichier de sauvegarde.
     */
    @Override
    public void save(String filepath) {
        try {
            PrintWriter writer ;
            writer = new PrintWriter(filepath);
            writer.write("SELECT DISTINCT ");
            for (int j=0; j<lggQueries.getVars1().size(); j++) {
                writer.write("?v_y"+lggQueries.getVars1().get(j)+"_y"+lggQueries.getVars2().get(j)+" ");
            }
            writer.write(" \n");
            writer.write("WHERE { \n");
            for (StmtIterator si = lggQueries.getResultProd().listStatements(); si.hasNext(); ) {
                Statement s = si.nextStatement();
                if (s.getSubject().toString().charAt(0) == 'v') {
                    if (s.getPredicate().toString().charAt(0) == 'v') {
                        if (s.getObject().toString().charAt(0) == 'v') {
                            writer.write("?"+s.getSubject().toString().replaceAll("\\.","")+" ?"+s.getPredicate().toString().replaceAll("\\.","")+" ?"+s.getObject().toString().replaceAll("\\.","")+" .\n");
                        }
                        else {
                            if (s.getObject().isLiteral())
                                writer.write("?"+s.getSubject().toString().replaceAll("\\.","")+" ?"+s.getPredicate().toString().replaceAll("\\.","")+" \""+s.getObject()+"\" .\n");
                            else
                                writer.write("?"+s.getSubject().toString().replaceAll("\\.","")+" ?"+s.getPredicate().toString().replaceAll("\\.","")+" <"+s.getObject()+"> .\n");
                        }
                    }
                    else {
                        if (s.getObject().toString().charAt(0) == 'v') {
                            writer.write("?"+s.getSubject().toString().replaceAll("\\.","")+" <"+s.getPredicate()+"> ?"+s.getObject().toString().replaceAll("\\.","")+" .\n");
                        }
                        else {
                            if (s.getObject().isLiteral())
                                writer.write("?"+s.getSubject().toString().replaceAll("\\.","")+" <"+s.getPredicate()+"> \""+s.getObject()+"\" .\n");
                            else
                                writer.write("?"+s.getSubject().toString().replaceAll("\\.","")+" <"+s.getPredicate()+"> <"+s.getObject()+"> .\n");
                        }
                    }
                }
                else {
                    if (s.getPredicate().toString().charAt(0) == 'v') {
                        if (s.getObject().toString().charAt(0) == 'v') {
                            writer.write(""+s.getSubject()+" ?"+s.getPredicate().toString().replaceAll("\\.","")+" ?"+s.getObject().toString().replaceAll("\\.","")+" .\n");
                        }
                        else {
                            if (s.getObject().isLiteral())
                                writer.write(""+s.getSubject()+" ?"+s.getPredicate().toString().replaceAll("\\.","")+" \""+s.getObject()+"\" .\n");
                            else
                                writer.write(""+s.getSubject()+" ?"+s.getPredicate().toString().replaceAll("\\.","")+" <"+s.getObject()+"> .\n");
                        }
                    }
                    else {
                        if (s.getObject().toString().charAt(0) == 'v') {
                            writer.write(""+s.getSubject()+" <"+s.getPredicate()+"> ?"+s.getObject().toString().replaceAll("\\.","")+" .\n");
                        }
                        else {
                            if (s.getObject().isLiteral())
                                writer.write(""+s.getSubject()+" <"+s.getPredicate()+"> \""+s.getObject()+"\" .\n");
                            else
                                writer.write(""+s.getSubject()+" <"+s.getPredicate()+"> <"+s.getObject()+"> .\n");
                        }
                    }
                }
            }
            writer.write("} ");
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Convertie le graphe du format Turtle (.ttl) en format N-Triple (.nt ou .n3)
     * @param path Chemin vers le fichier à convertir
     */
    public void convertToNTriples(String path){
        lggQueries = RDFModelFactory.loadQueries(path,"unknown") ;
        try {
            String output = DefaultParameter.outputDirectoryUsed
                    + path.substring(path.lastIndexOf("/"),path.lastIndexOf('.'));
            PrintWriter writer = new PrintWriter(new FileWriter(output + Lang.NTRIPLES)) ;
            lggQueries.getQuery1().write(writer, String.valueOf(Lang.NTRIPLES));

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
