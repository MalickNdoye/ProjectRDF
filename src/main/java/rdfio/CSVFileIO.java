package rdfio;

import org.apache.jena.rdf.model.Model;
import rdf.DictionaryNode;
import tools.DefaultParameter;

import java.io.*;
import java.util.*;

public class CSVFileIO extends RDFFileIO {

    public CSVFileIO(){
        super();
    }

    public CSVFileIO(String filepath){
        super(filepath);
    }

    private void save(){
        DictionaryNode dictionaryBN = DictionaryNode.getInstance();
        PrintWriter writerDic = null;
        Set<String> listKeys = null;
        try {
            writerDic = new PrintWriter(new FileWriter(filepath));
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

    public void save(String filePath){
        this.filepath = filePath;
        this.save();
    }


    /*
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
    }*/

    public Map<String, Integer> load(){
        if (!checkFile(filepath)){
            return null ;
        }
        try {
            InputStream ips = new FileInputStream(this.filepath);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            Map<String,Integer> dictionaryBN = new HashMap<String,Integer>() ;
            String line = "";
            while ((line = br.readLine()) != null) {
                final String[] splt = line.split("; ;");
                if (splt.length == 2) {
                    dictionaryBN.put(splt[0], Integer.parseInt(splt[1]));
                }
            }
            return dictionaryBN ;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void writeInfo(long size,long timeProd) {
        PrintStream l_out = null;
        try {
            l_out = new PrintStream(new FileOutputStream(DefaultParameter.infoPathUsed, true));
            l_out.print(DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].substring(0, DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].length() - 4) + ";");
            l_out.print(DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].substring(0, DefaultParameter.graphPath1.split("/")[DefaultParameter.graphPath1.split("/").length - 1].length() - 4) + ";");
            l_out.print(size + ";");
            l_out.print(timeProd + ";");
            l_out.println();
            l_out.flush();
            l_out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
