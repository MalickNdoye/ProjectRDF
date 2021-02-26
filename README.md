# ProjectRDF
Web semantic - RDF graph

usage: CLIsample [-c] [-d <dico>] [-f <input>] [-g] [-h] [-i <info>] [-o <output>] [-q] [-v]

Options, flags and arguments may be in any order
 -c             L'option -c définit le mode d'exécution en
                CONVERSION_MODE.
                Cette option est une option de mode d'exécution. Une
                option de mode d'exécution est obligatoire.Veuillez
                consulter la documentation pour plus d'informations.
 -d <dico>      L'option -d permet de specifier un chemin vers un fichier
                csv qui servira de dictionnaire.Ce fichier csv a un format
                bien défini afin d'assurer le fonctionnement de ce
                programme.
                Veuillez consulter la documentation pour plus
                d'informations.
                Cette option est optionnel mais requiert la présence d'un
                dico.csv avec l'arborescence suivante ./dico.csv
 -f <input>     L'option -f permet de specifier les chemins vers les deux
                fichiers représentant des graphes RDF.Ces fichiers ont un
                format bien défini afin d'assurer le fonctionnement de ce
                programme.
                Veuillez consulter la documentation pour plus
                d'informations.
                Cette option est obligatoire.
 -g             L'option -g définit le mode d'exécution en GRAPH_MODE.
                Cette option est une option de mode d'exécution. Une
                option de mode d'exécution est obligatoire.Veuillez
                consulter la documentation pour plus d'informations.
 -h,--help      Affiche l'aide
 -i <info>      L'option -i permet de specifier un chemin vers un fichier
                csv qui servira de fichier d'informations.Ce fichier csv a
                un format bien défini afin d'assurer le bon fonctionnement
                de ce programme.
                Veuillez consulter la documentation pour plus
                d'informations.
                Cette option est optionnel mais requiert la présence d'un
                info.csv avec l'arborescence suivante ./info.csv
 -o <output>    L'option -o permet de specifier un chemin vers le
                repertoire où sera redirigé les fichiers en sortie.
                Veuillez consulter la documentation pour plus
                d'informations.
                Cette option est optionnel mais requiert la présence d'un
                dico.csv avec l'arborescence suivante ./
 -q             L'option -q définit le mode d'exécution en QUERY_MODE.
                Cette option est une option de mode d'exécution. Une
                option de mode d'exécution est obligatoire.Veuillez
                consulter la documentation pour plus d'informations.
 -v,--version   Information sur la version de l'application.
This is RDFGraph Application (version 1.0.0) released on February 2021
  
#Exemple

$java -jar MainLGG [EXEC_MODE] -f pathToFile1 pathToFile2 -i pathToInfoFile -o pathToOutputDirectory -d pathToDictionary

$java -jar MainLGG [EXEC_MODE] -f pathToFile1 pathToFile2

$java -jar MainLGG --help
    
 
