package rdfcomputation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LggGraphs extends RDFComputation{


    public LggGraphs(){
        super();
    }

    public  LggGraphs(Model query1,Model query2){
        super(query1, query2);
    }

    public void writelgg(final String lggadr, final String query1Adr, final String query2Adr, final Model resultat) {
        PrintWriter writer = null;
        //writer = new PrintWriter(new FileWriter("./" + lggadr + "/Lgg" + query1Adr.split("/")[query1Adr.split("/").length - 1].split("\\.")[0] + query2Adr.split("/")[query2Adr.split("/").length - 1].split("\\.")[0] + ".n3"));
        try {
            writer = new PrintWriter(new FileWriter(lggadr + "/Lgg" + query1Adr.split("/")[query1Adr.split("/").length - 1].split("\\.")[0] + query2Adr.split("/")[query2Adr.split("/").length - 1].split("\\.")[0] + ".n3"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StmtIterator si = resultat.listStatements();
        while (si.hasNext()) {
            final Statement s = si.nextStatement();
            if (s.getSubject().toString().charAt(0) == 'v') {
                if (s.getPredicate().toString().charAt(0) == 'v') {
                    if (s.getObject().toString().charAt(0) == 'v') {
                        writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " ?" + s.getPredicate().toString().replaceAll("\\.", "") + " ?" + s.getObject().toString().replaceAll("\\.", "") + " .\n");
                    }
                    else if (s.getObject().isLiteral()) {
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
    }

}
