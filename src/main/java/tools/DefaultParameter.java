package tools;


/**
 * DefaultParameter est la classe qui sont stockés les variables par défaut et les constantes de l'application.
 * @author tndoye
 * @version 1.0.0
 */
public final class DefaultParameter {
    public static final String version = "1.0.0" ;
    public static final String releasedOn = "February 2021" ;
    public static final String author  = "tndoye" ;
    //CONSTANTES DE CHEMINS DE REPERTOIRES ET FICHIERS
    /**
     * Constante qui définit le chemin par défaut vers le fichier du dictionnaire.
     */
    public static final String dictionaryPath = "./dico.csv";

    /**
     * Constante qui définit le chemin par défaut vers le fichier des temps d'exécutions.
     */
    public static final String infoPath = "./info.csv";

    /**
     * Constante qui définit le chemin par défaut vers le répertoire de sortie.
     */
    public static final String outputDirectory = "./";


    //VARIABLES
    /**
     * Variable qui définit le chemin vers le dictionnaire utilisé.
     * Il est initialisé lors du lancement de l'application. Non spécifié, il se réfère à la
     * constante par défaut.
     * @see #dictionaryPath
     */
    public static String dictionaryPathUsed = dictionaryPath;

    /**
     * Variable qui définit le chemin vers le fichier des temps d'exécutions utilisé.
     * Il est initialisé lors du lancement de l'application. Non spécifié, il se réfère à la
     * constante par défaut.
     * @see #infoPath
     */
    public static String infoPathUsed = infoPath;

    /**
     * Variable qui définit le chemin vers le repertoire de sortie utilisé.
     * Il est initialisé lors du lancement de l'application. Non spécifié, il se réfère à la
     * constante par défaut.
     * @see #dictionaryPath
     */
    public static String outputDirectoryUsed = outputDirectory;

    /**
     * Variable qui définit le chemin vers le fichier décrivant le graphe 1.
     * Il doit être impérativement défini et est initialisé lors du lancement de l'application.
     */
    public static String graphPath1 = null;

    /**
     * Variable qui définit le chemin vers le fichier décrivant le graphe 2.
     * Il doit être impérativement défini et est initialisé lors du lancement de l'application.
     */
    public static String graphPath2 = null;

    /**
     * Variable qui définit le nom du fichier contenant le résultat du calcul.
     * Ce fichier est disponible dans le répertoire de sortie après l'exécution.5
     * @see #outputDirectory
     */
    public static String graphResult = null;

    //DESCRIPTIONS DE PARAMÈTRES DE MAIN ET AUTRES
    /**
     * Constante contenant le descriptif de l'option d'exécution [-d].
     */
    public static final String dictionaryArgumentDesc =
            "L'option -d permet de specifier un chemin vers un fichier csv qui servira de dictionnaire." +
                    "Ce fichier csv a un format bien défini afin d'assurer le fonctionnement de ce programme.\n" +
                    "Veuillez consulter la documentation pour plus d'informations.\n" +
                    "Cette option est optionnel mais requiert la présence d'un dico.csv avec l'arborescence suivante " +
                    dictionaryPath;

    /**
     * Constante contenant le descriptif de l'option d'exécution [-f].
     */
    public static final String fileArgumentDesc =
            "L'option -f permet de specifier les chemins vers les deux fichiers représentant des graphes RDF." +
                    "Ces fichiers ont un format bien défini afin d'assurer le fonctionnement de ce programme.\n" +
                    "Veuillez consulter la documentation pour plus d'informations.\n" +
                    "Cette option est obligatoire." ;

    /**
     * Constante contenant le descriptif de l'option d'exécution [-i].
     */
    public static final String infoArgumentDesc =
            "L'option -i permet de specifier un chemin vers un fichier csv qui servira de fichier d'informations." +
                    "Ce fichier csv a un format bien défini afin d'assurer le bon fonctionnement de ce programme.\n" +
                    "Veuillez consulter la documentation pour plus d'informations.\n" +
                    "Cette option est optionnel mais requiert la présence d'un info.csv avec l'arborescence suivante " +
                    infoPath;

    /**
     * Constante contenant le descriptif de l'option d'exécution [-o].
     */
    public static final String outputArgumentDesc =
            "L'option -o permet de specifier un chemin vers le repertoire où sera redirigé les fichiers en sortie.\n" +
                    "Veuillez consulter la documentation pour plus d'informations.\n" +
                    "Cette option est optionnel mais requiert la présence d'un dico.csv avec l'arborescence suivante " +
                    outputDirectory;


    /**
     * Constante contenant le descriptif de l'option d'exécution [-v || --version].
     */
    public static final String versionArgumentDesc = "Information sur la version de l'application." ;
    /**
     * Constante contenant le descriptif de l'option d'aide [-h || --help].
     */
    public static String helpDesc = "Affiche l'aide";

    /**
     * Constante contenant le descriptif de l'option d'exécution [q].
     */
    public static final String queryModeArgumentDesc =
            "L'option -q définit le mode d'exécution en QUERY_MODE.\n" +
            "Cette option est une option de mode d'exécution. Une option de mode d'exécution est obligatoire." +
            "Veuillez consulter la documentation pour plus d'informations.\n" ;

    /**
     * Constante contenant le descriptif de l'option d'exécution [-g].
     */
    public static final String graphModeArgumentDesc =
            "L'option -g définit le mode d'exécution en GRAPH_MODE.\n" +
            "Cette option est une option de mode d'exécution. Une option de mode d'exécution est obligatoire." +
            "Veuillez consulter la documentation pour plus d'informations.\n" ;

    /**
     * Constante contenant le descriptif de l'option d'exécution [-c].
     */
    public static final String conversionArgumentDesc =
            "L'option -c définit le mode d'exécution en CONVERSION_MODE.\n" +
                    "Cette option est une option de mode d'exécution. Une option de mode d'exécution est obligatoire." +
                    "Veuillez consulter la documentation pour plus d'informations.\n" ;


    /**
     * Constante contenant le descriptif des paramètres d'exécutions.
     */
    public static final String header =
            "\t\t\t[<arg1>] [<arg2>] [<arg3>] ...\n\t\tOptions, flags and arguments may be in any order";

    /**
     * Constante contenant le descriptif de bas de page.
     */
    public static final String footer =
            String.format("This is RDFGraph Application (version %s) released on %s", version, releasedOn);

}
