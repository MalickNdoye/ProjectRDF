package rdfio;

import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import rdfcomputation.LggQueries;
import rdfcomputation.RDFComputation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SPARQLFileIO extends RDFFileIO{
    private final LggQueries lggQueries ;

    public SPARQLFileIO(RDFComputation rdfComputation) {
        if(rdfComputation instanceof LggQueries){
            this.lggQueries = (LggQueries) rdfComputation ;
        } else {
            lggQueries = new LggQueries();
        }
    }

    @Override
    public void save(String filepath) {
        try {
            final PrintWriter writer = new PrintWriter(new FileWriter(filepath)) ;
            writer.write("SELECT DISTINCT ");
            for (int j = 0; j < lggQueries.getVars1().size(); ++j) {
                writer.write("?v_y" + lggQueries.getVars1().get(j) + "_y" + lggQueries.getVars2().get(j) + " ");
            }
            writer.write(" \n");
            writer.write("WHERE { \n");
            final StmtIterator si = lggQueries.getResultProd().listStatements();
            while (si.hasNext()) {
                final Statement s = si.nextStatement();
                if (s.getSubject().toString().charAt(0) == 'v') {
                    if (s.getPredicate().toString().charAt(0) == 'v') {
                        if (s.getObject().toString().charAt(0) == 'v') {
                            writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " ?" + s.getPredicate().toString().replaceAll("\\.", "") + " ?" + s.getObject().toString().replaceAll("\\.", "") + " .\n");
                        } else if (s.getObject().isLiteral()) {
                            writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " ?" + s.getPredicate().toString().replaceAll("\\.", "") + " \"" + s.getObject() + "\" .\n");
                        } else {
                            writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " ?" + s.getPredicate().toString().replaceAll("\\.", "") + " <" + s.getObject() + "> .\n");
                        }
                    } else if (s.getObject().toString().charAt(0) == 'v') {
                        writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " <" + s.getPredicate() + "> ?" + s.getObject().toString().replaceAll("\\.", "") + " .\n");
                    } else if (s.getObject().isLiteral()) {
                        writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " <" + s.getPredicate() + "> \"" + s.getObject() + "\" .\n");
                    } else {
                        writer.write("?" + s.getSubject().toString().replaceAll("\\.", "") + " <" + s.getPredicate() + "> <" + s.getObject() + "> .\n");
                    }
                } else if (s.getPredicate().toString().charAt(0) == 'v') {
                    if (s.getObject().toString().charAt(0) == 'v') {
                        writer.write(s.getSubject() + " ?" + s.getPredicate().toString().replaceAll("\\.", "") + " ?" + s.getObject().toString().replaceAll("\\.", "") + " .\n");
                    } else if (s.getObject().isLiteral()) {
                        writer.write(s.getSubject() + " ?" + s.getPredicate().toString().replaceAll("\\.", "") + " \"" + s.getObject() + "\" .\n");
                    } else {
                        writer.write(s.getSubject() + " ?" + s.getPredicate().toString().replaceAll("\\.", "") + " <" + s.getObject() + "> .\n");
                    }
                } else if (s.getObject().toString().charAt(0) == 'v') {
                    writer.write(s.getSubject() + " <" + s.getPredicate() + "> ?" + s.getObject().toString().replaceAll("\\.", "") + " .\n");
                } else if (s.getObject().isLiteral()) {
                    writer.write(s.getSubject() + " <" + s.getPredicate() + "> \"" + s.getObject() + "\" .\n");
                } else {
                    writer.write(s.getSubject() + " <" + s.getPredicate() + "> <" + s.getObject() + "> .\n");
                }
            }
            writer.write("} ");
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
