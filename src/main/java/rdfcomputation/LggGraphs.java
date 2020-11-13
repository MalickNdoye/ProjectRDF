package rdfcomputation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import tools.DefaultParameter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class LggGraphs extends RDFComputation{


    public LggGraphs(){
        super();
    }

    public  LggGraphs(Model query1,Model query2){
        super(query1, query2);
    }

    public void writelgg() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(DefaultParameter.graphResult));
            if (resultProd == null) {
                this.ProductGraph(DefaultParameter.dictionaryPathUsed);
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
    public String toString() {
        return super.toString();
    }
}
