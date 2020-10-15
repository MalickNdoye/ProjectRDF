package rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RiotException;


public class RDFModelFactory {
    private String filepath;

    public RDFModelFactory(){
        filepath = "UNKOWN PATH";
    }

    public RDFModelFactory(String path){
        this.filepath = path ;
    }

    public Model read(){
        Model model = null ;
        try {
            model = RDFDataMgr.loadModel(filepath, Lang.N3);
        }catch (RiotException e){
            System.err.println("Erreur sur le fichier N3 ou sur le paramètre de lecture : "+filepath);
            try {
                model = RDFDataMgr.loadModel(filepath, Lang.NT);
            }catch (RiotException er){
                System.err.println("Encore!");
                //er.printStackTrace();
            }
            e.printStackTrace();
        }
        return  model;
    }

    public Model read(String filepath){
        this.filepath = filepath;
        return this.read();
    }


}
