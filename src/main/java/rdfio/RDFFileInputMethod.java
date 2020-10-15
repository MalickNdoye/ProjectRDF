package rdfio;

import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;
import java.util.Map;

public interface RDFFileInputMethod {
    //LOADING METHOD
    Map<String, String> load();

    //SAVING METHOD
    void save(final Model model);
}
