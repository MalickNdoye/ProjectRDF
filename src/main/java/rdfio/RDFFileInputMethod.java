package rdfio;

import java.util.Map;

/**
 * RDFFileInputMethod est l'interface qui définit les méthodes communes de traitements de fichiers.
 * @see rdfio.RDFFileIO
 * @version 1.0.0
 */
public interface RDFFileInputMethod {
    /**
     * Retourne un dictionnaire des URIs et noeuds anonymes à partir d'un tableur.
     * @return Dictionnaire des URIs et noeuds anonymes.
     */
    Map<String, Integer> load();

    /**
     * Sauvegarde et écrit dans le fichier indiqué par le chemin défini.
     * @param filepath Chemin défini vers le fichier de sauvegarde.
     */
    void save(String filepath);
}
