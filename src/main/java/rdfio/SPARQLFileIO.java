package rdfio;

import org.apache.jena.dboe.base.file.FileException;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import rdf.RDFModelFactory;
import rdfcomputation.LggQueries;
import rdfcomputation.RDFComputation;
import tools.DefaultParameter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SPARQLFileIO extends RDFFileIO{
    private LggQueries lggQueries ;

    public SPARQLFileIO(RDFComputation rdfComputation) {
        if(rdfComputation instanceof LggQueries){
            this.lggQueries = (LggQueries) rdfComputation ;
        } else {
            lggQueries = new LggQueries();
        }
    }

    public SPARQLFileIO(){
        lggQueries = new LggQueries();
    }

    @Override
    public void save(String filepath) {
        try {
            PrintWriter writer ;
            //writer = new PrintWriter(new FileWriter("./"+lggadr+"/Lgg"+query1Adr.split("/")[query1Adr.split("/").length-1].substring(0, query1Adr.split("/")[query1Adr.split("/").length-1].length()-4)+query2Adr.split("/")[query2Adr.split("/").length-1].substring(0, query2Adr.split("/")[query2Adr.split("/").length-1].length()-4)+".sparql"));
            writer = new PrintWriter(filepath);

            //		writer.write("SELECT (count(*) as ?"+query1Adr.split("/")[query1Adr.split("/").length-1].substring(0, query1Adr.split("/")[query1Adr.split("/").length-1].length()-4)+query2Adr.split("/")[query2Adr.split("/").length-1].substring(0, query2Adr.split("/")[query2Adr.split("/").length-1].length()-4)+") \n");
            //		writer.write("WHERE { \n");
            writer.write("SELECT DISTINCT ");
            for (int j=0; j<lggQueries.getVars1().size(); j++) {
                writer.write("?v_y"+lggQueries.getVars1().get(j)+"_y"+lggQueries.getVars2().get(j)+" ");
            }
            writer.write(" \n");
            writer.write("WHERE { \n");
            //resultat.write(writer, "N-TRIPLES");
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

    public void convertToNTriples(String path){
        RDFModelFactory factory = new RDFModelFactory(path);
        lggQueries = factory.loadQueries(path,"unknown") ;
        try {
            String output = DefaultParameter.outputDirectoryUsed
                    + path.substring(path.lastIndexOf("/"),path.lastIndexOf('.'));
            PrintWriter writer = new PrintWriter(new FileWriter(output+".n3")) ;
            lggQueries.getQuery1().write(writer,"N-Triples");

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
