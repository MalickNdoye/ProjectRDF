import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import rdf.RDFModelFactory;
import rdfcomputation.LggGraphs;
import rdfcomputation.LggQueries;

import java.io.*;

public class MainLGG {
    private final Model rdfGraph1 ;
    private final Model rdfGraph2 ;
    private LggMode mode ;

    private MainLGG(){
        rdfGraph1 = ModelFactory.createDefaultModel() ;
        rdfGraph2 = ModelFactory.createDefaultModel() ;
        mode = LggMode.DEFAULT ;
    }

    private MainLGG(String pathGraph1, String pathGraph2, LggMode mode){
        rdfGraph1 = ModelFactory.createDefaultModel() ;
        rdfGraph2 = ModelFactory.createDefaultModel() ;
        switch (mode){
            case LGG_GRAPH_MODE:
                //lire
                break;
            case LGG_QUERY_MODE:
                //lire
                break;
            case DEFAULT:
            default:
                System.out.println("Choisir l'option de calcul de LGG");
                break;
        }
    }

    //EXECUTION:
    //java -jar MainLGG [OPTION] pathToFile1 pathToFile2 pathToInfoFile pathToOutputDirectory pathToDictionary
    private static Boolean parsingInput(String[] args, LggMode mode){
//        // calcul du LGG qui fonctionne
//        String graph1Adr = args[0]; // adresse complète du premier fichier (nom inclus) - fichier N3
//        String graph2Adr = args[1]; // adresse complète du second fichier (nom inclus) - fichier N3
//        String infoadr = args[2]; // adresse complète (nom inclus) du fichier d'info
//        String lggadr = args[3]; // chemin du répertoire dans lequel le lgg.n3 est construit
//        String dictionaryname = args[4]; // adresse complète (nom inclus) du fichier dictionnaire
        if (args.length!=6){
            System.out.println(args.length);
            return true ;
        }else{
            System.out.println("ERROR "+mode);
            return false ;
        }
    }

    public static void main(String[] args) {
        LggMode mode = LggMode.DEFAULT ;
        if (parsingInput(args,mode)==false){
            return;
        }
        // calcul du LGG qui fonctionne
        String graph1Adr = args[0]; // adresse complète du premier fichier (nom inclus) - fichier N3
        String graph2Adr = args[1]; // adresse complète du second fichier (nom inclus) - fichier N3
        String infoadr = args[2]; // adresse complète (nom inclus) du fichier d'info
        String lggadr = args[3]; // chemin du répertoire dans lequel le lgg.n3 est construit
        String dictionaryname = args[4]; // adresse complète (nom inclus) du fichier dictionnaire

        RDFModelFactory rdfModelFactory= new RDFModelFactory(graph1Adr);


        Model graph1 = rdfModelFactory.read();
        Model graph2 = rdfModelFactory.read(graph2Adr);
        LggGraphs lggGraphs = new LggGraphs(graph1, graph2);
        LggQueries lggQueries = new LggQueries(graph1,graph2);



        if (lggGraphs.getVars1().size() == lggGraphs.getVars2().size()) {
            Model resultat = ModelFactory.createDefaultModel();
            long timeProd = 0L;
            for (int i = 0; i < 5; ++i) {
                final long start = System.nanoTime();
                resultat = lggQueries.ProductGraph(dictionaryname);
                timeProd += System.nanoTime() - start;
            }
            timeProd /= 5L;
            //resultat.write((OutputStream)System.out, "N-TRIPLE");
            resultat.write(System.out, "N3");
            File csvFile = new File(infoadr);
            if (!csvFile.exists()) {
                System.err.println("Le fichier " + infoadr + " n'existe pas");
                return;
            }
            PrintStream l_out = null;
            try {
                l_out = new PrintStream(new FileOutputStream(infoadr, true));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            l_out.print(graph1Adr.split("/")[graph1Adr.split("/").length - 1].substring(0, graph1Adr.split("/")[graph1Adr.split("/").length - 1].length() - 4) + ";");
            l_out.print(graph2Adr.split("/")[graph2Adr.split("/").length - 1].substring(0, graph2Adr.split("/")[graph2Adr.split("/").length - 1].length() - 4) + ";");
            l_out.print(resultat.size() + ";");
            l_out.print(timeProd + ";");
            l_out.println();
            l_out.flush();
            l_out.close();
            l_out = null;
            System.out.println("Writing ...");
            lggGraphs.writelgg(lggadr, graph1Adr, graph2Adr, resultat);
            System.out.println("End");
        }
        else {
            System.out.println();
        }


    }

}
