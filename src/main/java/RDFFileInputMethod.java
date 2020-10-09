import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;

public interface RDFFileInputMethod {
    public Model load(final String filePath, final ArrayList<String> vars);
}
