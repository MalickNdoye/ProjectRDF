package tools;

public final class DefaultParameter {
    //CONSTANTES DE CHEMINS DE REPERTOIRES ET FICHIERS
    public static final String dictionaryPath = "./Ressource/Archive/dico.csv";
    public static final String infoPath = "./Ressource/Archive/info.csv";
    public static final String outputDirectory = "./Ressource/Output/";


    //VARIABLES
    public static String dictionaryPathUsed = dictionaryPath;
    public static String infoPathUsed = infoPath;
    public static String outputDirectoryUsed = outputDirectory;
    public static String graphPath1 = null;
    public static String graphPath2 = null;
    public static String graphResult = null;



    //PARAMETRES DE MAIN
    public static final String dictionaryArgumentDesc =
            "L'option -d permet de specifier un chemin vers un fichier csv qui servira de dictionnaire." +
                    "Ce fichier csv a un format bien défini afin d'assurer le fonctionnement de ce programme." +
                    "Veuillez consulter la documentation pour plus d'informations.\n" +
                    "Cet option est optionnel mais requiert la présence d'un dico.csv avec l'arborescence suivante " +
                    dictionaryPath;
    public static final String graphArgumentDesc =
            "L'option -g permet de specifier les chemins vers les deux fichiers representant des graphes RDF." +
                    "Ces fichiers ont un format bien défini afin d'assurer le fonctionnement de ce programme." +
                    "Veuillez consulter la documentation pour plus d'informations.\n" +
                    "Cet option est obligatoire" ;
    public static final String infoArgumentDesc =
            "L'option -i permet de specifier un chemin vers un fichier csv qui servira de fichier d'informations." +
                    "Ce fichier csv a un format bien défini afin d'assurer le bon fonctionnement de ce programme." +
                    "Veuillez consulter la documentation pour plus d'informations.\n" +
                    "Cet option est optionnel mais requiert la présence d'un info.csv avec l'arborescence suivante " +
                    infoPath;
    public static final String outputArgumentDesc =
            "L'option -o permet de specifier un chemin vers le repertoire où sera redirigé les fichiers en sortie." +
                    "Veuillez consulter la documentation pour plus d'informations.\n" +
                    "Cet option est optionnel mais requiert la présence d'un dico.csv avec l'arborescence suivante " +
                    outputDirectory;
    public static final String header =
            "\t\t\t[<arg1>] [<arg2>] [<arg3>] ...\n\t\tOptions, flags and arguments may be in any order";
    public static final String footer =
            "This is DwB's solution brought to Commons CLI 1.3.1 compliance (deprecated methods replaced)";
    public static String helpDesc = "L'option pour afficher l'aide.";
}
