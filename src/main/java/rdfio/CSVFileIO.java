package rdfio;

import rdf.DictionaryNode;
import tools.DefaultParameter;

import java.io.*;
import java.util.*;


/**
 * CSVFileIO est la classe qui gère l'écriture et la lecture des fichiers csv.
 *
 * @see rdfio.RDFFileIO
 * @version 1.0.0
 */
public class CSVFileIO extends RDFFileIO {

    /**
     * @see RDFFileIO#RDFFileIO()
     */
    public CSVFileIO(){
        super();
    }

    /**
     * @see RDFFileIO#RDFFileIO(String)
     */
    public CSVFileIO(String filepath){
        super(filepath);
    }

    /**
     * Sauvegarde le dictionnaire dans le fichier indiqué par le chemin défini par défaut.
     * @see RDFFileInputMethod#save(String)
     * @see DictionaryNode#save()
     */
    private void save(){
        DictionaryNode dictionaryBN = DictionaryNode.getInstance();
        PrintWriter writerDic;
        Set<String> listKeys;
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

    /**
     * Sauvegarde le dictionnaire dans le fichier indiqué par le chemin défini en entrée.
     * @see #save()
     */
    public void save(String filePath){
        this.filepath = filePath;
        this.save();
    }

    /**
     * @see RDFFileInputMethod#load()
     */
    public Map<String, Integer> load(){
        if (!checkFile(filepath)){
            return null ;
        }
        try {
            InputStream ips = new FileInputStream(this.filepath);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            Map<String,Integer> dictionaryBN = new HashMap<>() ;
            String line;
            while ((line = br.readLine()) != null) {
                final String[] splt = line.split("; ;");
                if (splt.length == 2) {
                    dictionaryBN.put(splt[0], Integer.parseInt(splt[1]));
                }
            }
            br.close();
            ipsr.close();
            ips.close();
            return dictionaryBN ;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sauvegarde les informations d'exécutions dans un fichier
     * @param size Taille du graphe obtenu après traitement.
     * @param timeProd Temps de traitement.
     * @see rdfcomputation.RDFComputation
     */
    public void writeInfo(long size,long timeProd) {
        PrintStream l_out ;
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
