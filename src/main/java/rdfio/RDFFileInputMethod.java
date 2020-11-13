package rdfio;

import java.util.Map;

public interface RDFFileInputMethod {
    //LOADING METHOD
    Map<String, Integer> load();

    //SAVING METHOD
    void save(String filepath);
}
