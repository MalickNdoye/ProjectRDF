package rdf;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RiotException;
import rdfcomputation.LggGraphs;
import rdfcomputation.RDFComputation;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class RDFModelFactory {
    private String filepath;

    public RDFModelFactory(){
        filepath = "UNKOWN PATH";
    }

    public RDFModelFactory(String path){
        this.filepath = path ;
    }

    public Model read(){
        Model model = null ;
        try {
            model = RDFDataMgr.loadModel(filepath,Lang.NTRIPLES);
        }catch (RiotException e){
            System.err.println("Erreur sur le fichier N3 ou sur le paramètre de lecture : "+filepath);
            try {
                model = RDFDataMgr.loadModel(filepath, Lang.NT);
            }catch (RiotException er){
                System.err.println("Encore!");
                //er.printStackTrace();
            }
            e.printStackTrace();
        }
        return model;
    }

    public Model read(String filepath) {
        this.filepath = filepath;
        return this.read();
    }

    public LggGraphs loadlgg(String filepath1, String filepath2) {
        LggGraphs rdf = new LggGraphs();
        try {
            this.load(rdf, filepath1, 1);
            this.load(rdf, filepath2, 2);
            return rdf;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rdf;
    }

    private void load(RDFComputation rdf, final String fileName, int number) throws IOException {
        ArrayList<String> vars = null;
        Model query = null;
        switch (number) {
            case 1:
                vars = rdf.getVars1();
                query = rdf.getQuery1();
                break;
            case 2:
                vars = rdf.getVars2();
                query = rdf.getQuery2();
                break;
            default:
                throw new IOException("Erreur de chargement 1");
        }
        HashMap<String, String> prefixs = rdf.getPrefixs();
        HashMap<String, String> blanknodes = rdf.getBlanknodes();
        InputStream ips = new FileInputStream(fileName);
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String ligne;
        while ((ligne = br.readLine()) != null) {
            final String[] spl = ligne.split(" ");
            if (spl[0].equals("head")) {
                for (int i = 1; i < spl.length; ++i) {
                    vars.add(spl[i].substring(2));
                }
            } else if (spl[0].equals("@prefix")) {
                prefixs.put(spl[1].substring(0, spl[1].length() - 1), spl[2].substring(1, spl[2].length() - 1));
            } else {
                if (spl[0].length() <= 1 || spl[1].length() <= 1 || spl[2].length() <= 1) {
                    continue;
                }
                if (spl[0].charAt(0) == '_') {
                    if (spl[1].charAt(0) == '_') {
                        if (spl[2].charAt(0) == '_') {
                            String var1 = "";
                            if (blanknodes.containsKey(spl[0].substring(2))) {
                                var1 = blanknodes.get(spl[0].substring(2));
                            } else {
                                var1 = "y" + spl[0].substring(2);
                                blanknodes.put(spl[0].substring(2), var1);
                            }
                            final Resource rs1 = query.createResource(var1);
                            String var2 = "";
                            if (blanknodes.containsKey(spl[2].substring(2))) {
                                var2 = blanknodes.get(spl[2].substring(2));
                            } else {
                                var2 = "y" + spl[2].substring(2);
                                blanknodes.put(spl[2].substring(2), var2);
                            }
                            final Resource rs2 = query.createResource(var2);
                            final String st = "y" + spl[1].substring(2);
                            final Property pr = query.createProperty(st);
                            query.add(rs1, pr, rs2);
                        } else {
                            String var1 = "";
                            if (blanknodes.containsKey(spl[0].substring(2))) {
                                var1 = blanknodes.get(spl[0].substring(2));
                            } else {
                                var1 = "y" + spl[0].substring(2);
                                blanknodes.put(spl[0].substring(2), var1);
                            }
                            final Resource rs1 = query.createResource(var1);
                            final String st2 = "y" + spl[1].substring(2);
                            final Property pr2 = query.createProperty(st2);
                            Resource rs3 = null;
                            Literal lt = null;
                            if (prefixs.containsKey(getprefx(spl[2]))) {
                                rs3 = query.createResource(prefixs.get(getprefx(spl[2])) + getname(spl[2]));
                            } else if (spl[2].charAt(0) == '<') {
                                rs3 = query.createResource(spl[2].substring(1, spl[2].length() - 1));
                            } else if (spl[2].charAt(0) == '"') {
                                lt = query.createLiteral(spl[2].substring(1, spl[2].length() - 1));
                            } else {
                                rs3 = query.createResource(spl[2]);
                            }
                            if (rs3 != null) {
                                query.add(rs1, pr2, rs3);
                            } else {
                                query.add(rs1, pr2, lt);
                            }
                        }
                    } else if (spl[2].charAt(0) == '_') {
                        String var1 = "";
                        if (blanknodes.containsKey(spl[0].substring(2))) {
                            var1 = blanknodes.get(spl[0].substring(2));
                        } else {
                            var1 = "y" + spl[0].substring(2);
                            blanknodes.put(spl[0].substring(2), var1);
                        }
                        final Resource rs1 = query.createResource(var1);
                        String var2 = "";
                        if (blanknodes.containsKey(spl[2].substring(2))) {
                            var2 = blanknodes.get(spl[2].substring(2));
                        } else {
                            var2 = "y" + spl[2].substring(2);
                            blanknodes.put(spl[2].substring(2), var2);
                        }
                        final Resource rs2 = query.createResource(var2);
                        Property pr3 = null;
                        if (spl[1].equals("a")) {
                            pr3 = query.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                        } else if (prefixs.containsKey(getprefx(spl[1]))) {
                            pr3 = query.createProperty(prefixs.get(getprefx(spl[1])) + getname(spl[1]));
                        } else if (spl[1].charAt(0) == '<') {
                            pr3 = query.createProperty(spl[1].substring(1, spl[1].length() - 1));
                        } else {
                            pr3 = query.createProperty(spl[1]);
                        }
                        query.add(rs1, pr3, rs2);
                    } else {
                        String var1 = "";
                        if (blanknodes.containsKey(spl[0].substring(2))) {
                            var1 = blanknodes.get(spl[0].substring(2));
                        } else {
                            var1 = "y" + spl[0].substring(2);
                            blanknodes.put(spl[0].substring(2), var1);
                        }
                        final Resource rs1 = query.createResource(var1);
                        Resource rs4 = null;
                        Property pr2 = null;
                        Literal lt2 = null;
                        if (prefixs.containsKey(getprefx(spl[2]))) {
                            rs4 = query.createResource(prefixs.get(getprefx(spl[2])) + getname(spl[2]));
                        } else if (spl[2].charAt(0) == '<') {
                            rs4 = query.createResource(spl[2].substring(1, spl[2].length() - 1));
                        } else if (spl[2].charAt(0) == '"') {
                            lt2 = query.createLiteral(spl[2].substring(1, spl[2].length() - 1));
                        } else {
                            rs4 = query.createResource(spl[2]);
                        }
                        if (spl[1].equals("a")) {
                            pr2 = query.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                        } else if (prefixs.containsKey(getprefx(spl[1]))) {
                            pr2 = query.createProperty(prefixs.get(getprefx(spl[1])) + getname(spl[1]));
                        } else if (prefixs.containsKey(getprefx(spl[1]))) {
                            pr2 = query.createProperty(prefixs.get(getprefx(spl[1])) + getname(spl[1]));
                        } else if (spl[1].charAt(0) == '<') {
                            pr2 = query.createProperty(spl[1].substring(1, spl[1].length() - 1));
                        } else {
                            pr2 = query.createProperty(spl[1]);
                        }
                        if (rs4 != null) {
                            query.add(rs1, pr2, rs4);
                        } else {
                            query.add(rs1, pr2, lt2);
                        }
                    }
                } else if (spl[1].charAt(0) == '_') {
                    if (spl[2].charAt(0) == '_') {
                        Resource rs5 = null;
                        if (prefixs.containsKey(getprefx(spl[0]))) {
                            rs5 = query.createResource(prefixs.get(getprefx(spl[0])) + getname(spl[0]));
                        } else if (spl[0].charAt(0) == '<') {
                            rs5 = query.createResource(spl[0].substring(1, spl[0].length() - 1));
                        } else {
                            rs5 = query.createResource(spl[0]);
                        }
                        String var3 = "";
                        if (blanknodes.containsKey(spl[2].substring(2))) {
                            var3 = blanknodes.get(spl[2].substring(2));
                        } else {
                            var3 = "v_" + spl[2].substring(2);
                            blanknodes.put(spl[2].substring(2), var3);
                        }
                        final Resource rs4 = query.createResource(var3);
                        final String st3 = "v_" + spl[1].substring(2);
                        final Property pr3 = query.createProperty(st3);
                        query.add(rs5, pr3, rs4);
                    } else {
                        Resource rs5 = null;
                        if (prefixs.containsKey(getprefx(spl[0]))) {
                            rs5 = query.createResource(prefixs.get(getprefx(spl[0])) + getname(spl[0]));
                        } else if (spl[0].charAt(0) == '<') {
                            rs5 = query.createResource(spl[0].substring(1, spl[0].length() - 1));
                        } else {
                            rs5 = query.createResource(spl[0]);
                        }
                        final String st4 = "v_" + spl[1].substring(2);
                        final Property pr4 = query.createProperty(st4);
                        Resource rs2 = null;
                        Literal lt2 = null;
                        if (prefixs.containsKey(getprefx(spl[2]))) {
                            rs2 = query.createResource(prefixs.get(getprefx(spl[2])) + getname(spl[2]));
                        } else if (spl[2].charAt(0) == '<') {
                            rs2 = query.createResource(spl[2].substring(1, spl[2].length() - 1));
                        } else if (spl[2].charAt(0) == '"') {
                            lt2 = query.createLiteral(spl[2].substring(1, spl[2].length() - 1));
                        } else {
                            rs2 = query.createResource(spl[2]);
                        }
                        if (rs2 != null) {
                            query.add(rs5, pr4, rs2);
                        } else {
                            query.add(rs5, pr4, lt2);
                        }
                    }
                } else if (spl[2].charAt(0) == '_') {
                    Resource rs5 = null;
                    if (prefixs.containsKey(getprefx(spl[0]))) {
                        rs5 = query.createResource(prefixs.get(getprefx(spl[0])) + getname(spl[0]));
                    } else if (spl[0].charAt(0) == '<') {
                        rs5 = query.createResource(spl[0].substring(1, spl[0].length() - 1));
                    } else {
                        rs5 = query.createResource(spl[0]);
                    }
                    Property pr5 = null;
                    if (spl[1].equals("a")) {
                        pr5 = query.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                    } else if (prefixs.containsKey(getprefx(spl[1]))) {
                        pr5 = query.createProperty(prefixs.get(getprefx(spl[1])) + getname(spl[1]));
                    } else if (prefixs.containsKey(getprefx(spl[1]))) {
                        pr5 = query.createProperty(prefixs.get(getprefx(spl[1])) + getname(spl[1]));
                    } else if (spl[1].charAt(0) == '<') {
                        pr5 = query.createProperty(spl[1].substring(1, spl[1].length() - 1));
                    } else {
                        pr5 = query.createProperty(spl[1]);
                    }
                    String var2 = "";
                    if (blanknodes.containsKey(spl[2].substring(2))) {
                        var2 = blanknodes.get(spl[2].substring(2));
                    } else {
                        var2 = "v_" + spl[2].substring(2);
                        blanknodes.put(spl[2].substring(2), var2);
                    }
                    final Resource rs2 = query.createResource(var2);
                    query.add(rs5, pr5, rs2);
                } else {
                    Resource rs5 = null;
                    if (prefixs.containsKey(getprefx(spl[0]))) {
                        rs5 = query.createResource(prefixs.get(getprefx(spl[0])) + getname(spl[0]));
                    } else if (spl[0].charAt(0) == '<') {
                        rs5 = query.createResource(spl[0].substring(1, spl[0].length() - 1));
                    } else {
                        rs5 = query.createResource(spl[0]);
                    }
                    Property pr5 = null;
                    if (spl[1].equals("a")) {
                        pr5 = query.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                    } else if (prefixs.containsKey(getprefx(spl[1]))) {
                        pr5 = query.createProperty(prefixs.get(getprefx(spl[1])) + getname(spl[1]));
                    } else if (prefixs.containsKey(getprefx(spl[1]))) {
                        pr5 = query.createProperty(prefixs.get(getprefx(spl[1])) + getname(spl[1]));
                    } else if (spl[1].charAt(0) == '<') {
                        pr5 = query.createProperty(spl[1].substring(1, spl[1].length() - 1));
                    } else {
                        pr5 = query.createProperty(spl[1]);
                    }
                    Resource rs4 = null;
                    Literal lt3 = null;
                    if (prefixs.containsKey(getprefx(spl[2]))) {
                        rs4 = query.createResource(prefixs.get(getprefx(spl[2])) + getname(spl[2]));
                    } else if (spl[2].charAt(0) == '<') {
                        rs4 = query.createResource(spl[2].substring(1, spl[2].length() - 1));
                    } else if (spl[2].charAt(0) == '"' & spl[2].length() > 2) {
                        lt3 = query.createLiteral(spl[2].substring(1, spl[2].length() - 1));
                    } else {
                        rs4 = query.createResource(spl[2]);
                    }
                    if (rs4 != null) {
                        query.add(rs5, pr5, rs4);
                    } else {
                        query.add(rs5, pr5, lt3);
                    }
                }
            }
        }
    }

    private String getprefx(final String ligne) {
        final String[] ss = ligne.split(":");
        return ss[0];
    }

    private String getname(final String ligne) {
        final String[] ss = ligne.split(":");
        return ss[1];
    }

}
