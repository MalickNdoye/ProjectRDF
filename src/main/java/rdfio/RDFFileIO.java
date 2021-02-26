package rdfio;

import java.io.File;
import java.util.Map;

/**
 * RDFFileIO est la classe qui généralise toutes les classes qui traitent des fichiers.
 * @see rdfio.RDFFileInputMethod
 * @version 1.0.0
 */
public abstract class RDFFileIO implements RDFFileInputMethod {
    /**
     * Chemin par défaut vers le fichier décrivant un graphe RDF.
     */
    protected String filepath ;

    /**
     * Constructeur par défaut.
     */
    protected RDFFileIO(){
        this.filepath = "UNKOWN FILE" ;
    }

    /**
     * Constructeur avec initialisation du chemin vers le fichier RDF.
     * @param filepath Chemin vers le fichier RDF.
     */
    protected RDFFileIO(String filepath){
        this.filepath = filepath ;
    }

    /**
     * Cette méthode est implémentée dans toutes les sous-classes.
     * @see RDFFileInputMethod#load()
     */
    public Map<String, Integer> load() {
        return null;
    }

    /**
     * Determine la validité du chemin défini.
     * <p> Cette méthode vérifie le fichier indiqué par le chemin défini:
     *     <li>existe.</li>
     *     <li>est un fichier.</li>
     *     <li>n'est pas un répertoire.</li>
     * </p>
     * @param filepath Chemin vers un fichier.
     * @return True si le fichier existe et est un fichier, False sinon.
     */
    public Boolean checkFile(String filepath){
        File file = new File(filepath);
        return file.exists() && file.isFile() && !file.isDirectory();
    }

    /**
     * Teste la validité du chemin associé à cet objet.
     * @see #checkFile(String)
     */
    public Boolean checkFile(){
        return checkFile(filepath);
    }
}
