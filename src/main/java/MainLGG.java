import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import javax.xml.bind.annotation.XmlType;

public class MainLGG {
    private Model rdfGraph1 ;
    private Model rdfGraph2 ;
    private LggMode mode ;

    private MainLGG(){
        rdfGraph1 = ModelFactory.createDefaultModel() ;
        rdfGraph2 = ModelFactory.createDefaultModel() ;
        mode = LggMode.DEFAULT ;
    }

    private MainLGG(String pathGraph1,String pathGraph2,LggMode mode){
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


    public static void main(String[] args) {


    }
}
