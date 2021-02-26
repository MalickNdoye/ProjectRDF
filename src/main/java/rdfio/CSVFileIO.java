package rdfio;

import rdf.DictionaryNode;
import tools.DefaultParameter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * CSVFileIO est la classe qui gère l'écriture et la lecture des fichiers csv.
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
            filepath = dictionaryBN.getDictionaryPath();
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
            InputStream inputStream = new FileInputStream(this.filepath);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Map<String,Integer> dictionaryBN = new HashMap<>() ;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String[] str = line.split("; ;");
                if (str.length == 2) {
                    dictionaryBN.put(str[0], Integer.parseInt(str[1]));
                }
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
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
