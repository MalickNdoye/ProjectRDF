package rdfio;

import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;

public interface RDFFileInputMethod {
    //LOADING METHOD
    public Model load(final String filePath, final ArrayList<String> vars);

    //SAVING METHOD
    public void save(final Model model);
}
