package rdfio;

import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;
import java.util.Map;

public interface RDFFileInputMethod {
    //LOADING METHOD
    public Map<String, String> load();

    //SAVING METHOD
    public void save(final Model model);
}
