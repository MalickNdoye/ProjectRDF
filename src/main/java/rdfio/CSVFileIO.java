package rdfio;

import org.apache.jena.rdf.model.Model;
import rdf.DictionaryNode;

import java.io.*;
import java.util.*;

public class CSVFileIO extends RDFFileIO {

    public CSVFileIO(){
        super();
    }

    public CSVFileIO(String filepath){
        super(filepath);
    }

    public Model load(String filePath, ArrayList<String> vars) {
        return null;
    }

    public void save(){
        DictionaryNode dictionaryBN = DictionaryNode.getInstance();
        PrintWriter writerDic = null;
        Set<String> listKeys = null;
        try {
            writerDic = new PrintWriter(new FileWriter(dictionaryBN.getDictionaryPath()));
            listKeys = dictionaryBN.keySet();
            for (String key : listKeys) {
                writerDic.write(key + "; ;" + dictionaryBN.get(key));
                writerDic.write("\n");
            }
            writerDic.flush();
            writerDic.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Map<String, String> load(){
        if (!checkFile(filepath)){
            return null ;
        }

        try {
            InputStream ips = new FileInputStream(this.filepath);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            Map<String,String> dictionaryBN = new HashMap<String,String>() ;
            String line = "";
            while ((line = br.readLine()) != null) {
                final String[] splt = line.split("; ;");
                if (splt.length == 2) {
                    dictionaryBN.put(splt[0], splt[1]);
                }
            }
            return dictionaryBN ;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public void save(Model model) {

    }
}
