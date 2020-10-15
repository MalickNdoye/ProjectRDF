package rdfio;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.*;
import java.util.ArrayList;

public class RDFGraphIO extends RDFFileIO {


    public RDFGraphIO(){
        super() ;
    }

    public RDFGraphIO(String path){
        super(path) ;
    }

    public Model read(){
        Model model = RDFDataMgr.loadModel(filepath, Lang.N3);
        return  model;
    }
}
