package rdfio;

import org.apache.jena.rdf.model.Model;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public abstract class RDFFileIO implements RDFFileInputMethod {
    protected String filepath ;
    //protected InputStream inputStream ;

    protected RDFFileIO(){
        this.filepath = "UNKOWN FILE" ;
    }

    protected RDFFileIO(String filepath){
        this.filepath = filepath ;
    }

    public Map<String, Integer> load() {
        return null;
    }

    public Boolean checkFile(String filepath){
        File file = new File(filepath);
        return file.exists() && file.isFile() && !file.isDirectory();
    }
}
