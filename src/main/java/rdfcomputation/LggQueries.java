package rdfcomputation;

import org.apache.jena.rdf.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LggQueries extends RDFComputation{
    private final Map<String, String> blanknodes;
    private final Map<String, String> prefixs;
    private final Model resultProd ;

    public LggQueries(){
        super();
        resultProd = ModelFactory.createDefaultModel();
        this.blanknodes = new HashMap<String, String>();
        this.prefixs = new HashMap<String, String>();
    }

    public  LggQueries(Model query1,Model query2){
        super(query1, query2);
        resultProd = ModelFactory.createDefaultModel();
        this.blanknodes = new HashMap<String, String>();
        this.prefixs = new HashMap<String, String>();
    }

    public Model ProductGraph(String dictionaryname) {
        return null;
    }

    public void load(final Model query, final String fileName, final ArrayList<String> vars) throws IOException {
        final InputStream ips = new FileInputStream(fileName);
        final InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String ligne;
        while ((ligne = br.readLine()) != null) {
            final String[] spl = ligne.split(" ");
            if (spl[0].equals("head")) {
                for (int i = 1; i < spl.length; ++i) {
                    vars.add(spl[i].substring(2));
                }
            }
            else if (spl[0].equals("@prefix")) {
                this.prefixs.put(spl[1].substring(0, spl[1].length() - 1), spl[2].substring(1, spl[2].length() - 1));
            }
            else {
                if (spl[0].length() <= 1 || spl[1].length() <= 1 || spl[2].length() <= 1) {
                    continue;
                }
                if (spl[0].charAt(0) == '_') {
                    if (spl[1].charAt(0) == '_') {
                        if (spl[2].charAt(0) == '_') {
                            String var1 = "";
                            if (this.blanknodes.containsKey(spl[0].substring(2))) {
                                var1 = this.blanknodes.get(spl[0].substring(2));
                            }
                            else {
                                var1 = "y" + spl[0].substring(2);
                                this.blanknodes.put(spl[0].substring(2), var1);
                            }
                            final Resource rs1 = query.createResource(var1);
                            String var2 = "";
                            if (this.blanknodes.containsKey(spl[2].substring(2))) {
                                var2 = this.blanknodes.get(spl[2].substring(2));
                            }
                            else {
                                var2 = "y" + spl[2].substring(2);
                                this.blanknodes.put(spl[2].substring(2), var2);
                            }
                            final Resource rs2 = query.createResource(var2);
                            final String st = "y" + spl[1].substring(2);
                            final Property pr = query.createProperty(st);
                            query.add(rs1, pr, rs2);
                        }
                        else {
                            String var1 = "";
                            if (this.blanknodes.containsKey(spl[0].substring(2))) {
                                var1 = this.blanknodes.get(spl[0].substring(2));
                            }
                            else {
                                var1 = "y" + spl[0].substring(2);
                                this.blanknodes.put(spl[0].substring(2), var1);
                            }
                            final Resource rs1 = query.createResource(var1);
                            final String st2 = "y" + spl[1].substring(2);
                            final Property pr2 = query.createProperty(st2);
                            Resource rs3 = null;
                            Literal lt = null;
                            if (this.prefixs.containsKey(this.getprefx(spl[2]))) {
                                rs3 = query.createResource(this.prefixs.get(this.getprefx(spl[2])) + this.getname(spl[2]));
                            }
                            else if (spl[2].charAt(0) == '<') {
                                rs3 = query.createResource(spl[2].substring(1, spl[2].length() - 1));
                            }
                            else if (spl[2].charAt(0) == '"') {
                                lt = query.createLiteral(spl[2].substring(1, spl[2].length() - 1));
                            }
                            else {
                                rs3 = query.createResource(spl[2]);
                            }
                            if (rs3 != null) {
                                query.add(rs1, pr2, rs3);
                            }
                            else {
                                query.add(rs1, pr2, lt);
                            }
                        }
                    }
                    else if (spl[2].charAt(0) == '_') {
                        String var1 = "";
                        if (this.blanknodes.containsKey(spl[0].substring(2))) {
                            var1 = this.blanknodes.get(spl[0].substring(2));
                        }
                        else {
                            var1 = "y" + spl[0].substring(2);
                            this.blanknodes.put(spl[0].substring(2), var1);
                        }
                        final Resource rs1 = query.createResource(var1);
                        String var2 = "";
                        if (this.blanknodes.containsKey(spl[2].substring(2))) {
                            var2 = this.blanknodes.get(spl[2].substring(2));
                        }
                        else {
                            var2 = "y" + spl[2].substring(2);
                            this.blanknodes.put(spl[2].substring(2), var2);
                        }
                        final Resource rs2 = query.createResource(var2);
                        Property pr3 = null;
                        if (spl[1].equals("a")) {
                            pr3 = query.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                        }
                        else if (this.prefixs.containsKey(this.getprefx(spl[1]))) {
                            pr3 = query.createProperty(this.prefixs.get(this.getprefx(spl[1])) + this.getname(spl[1]));
                        }
                        else if (spl[1].charAt(0) == '<') {
                            pr3 = query.createProperty(spl[1].substring(1, spl[1].length() - 1));
                        }
                        else {
                            pr3 = query.createProperty(spl[1]);
                        }
                        query.add(rs1, pr3, rs2);
                    }
                    else {
                        String var1 = "";
                        if (this.blanknodes.containsKey(spl[0].substring(2))) {
                            var1 = this.blanknodes.get(spl[0].substring(2));
                        }
                        else {
                            var1 = "y" + spl[0].substring(2);
                            this.blanknodes.put(spl[0].substring(2), var1);
                        }
                        final Resource rs1 = query.createResource(var1);
                        Resource rs4 = null;
                        Property pr2 = null;
                        Literal lt2 = null;
                        if (this.prefixs.containsKey(this.getprefx(spl[2]))) {
                            rs4 = query.createResource(this.prefixs.get(this.getprefx(spl[2])) + this.getname(spl[2]));
                        }
                        else if (spl[2].charAt(0) == '<') {
                            rs4 = query.createResource(spl[2].substring(1, spl[2].length() - 1));
                        }
                        else if (spl[2].charAt(0) == '"') {
                            lt2 = query.createLiteral(spl[2].substring(1, spl[2].length() - 1));
                        }
                        else {
                            rs4 = query.createResource(spl[2]);
                        }
                        if (spl[1].equals("a")) {
                            pr2 = query.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                        }
                        else if (this.prefixs.containsKey(this.getprefx(spl[1]))) {
                            pr2 = query.createProperty(this.prefixs.get(this.getprefx(spl[1])) + this.getname(spl[1]));
                        }
                        else if (this.prefixs.containsKey(this.getprefx(spl[1]))) {
                            pr2 = query.createProperty(this.prefixs.get(this.getprefx(spl[1])) + this.getname(spl[1]));
                        }
                        else if (spl[1].charAt(0) == '<') {
                            pr2 = query.createProperty(spl[1].substring(1, spl[1].length() - 1));
                        }
                        else {
                            pr2 = query.createProperty(spl[1]);
                        }
                        if (rs4 != null) {
                            query.add(rs1, pr2, rs4);
                        }
                        else {
                            query.add(rs1, pr2, lt2);
                        }
                    }
                }
                else if (spl[1].charAt(0) == '_') {
                    if (spl[2].charAt(0) == '_') {
                        Resource rs5 = null;
                        if (this.prefixs.containsKey(this.getprefx(spl[0]))) {
                            rs5 = query.createResource(this.prefixs.get(this.getprefx(spl[0])) + this.getname(spl[0]));
                        }
                        else if (spl[0].charAt(0) == '<') {
                            rs5 = query.createResource(spl[0].substring(1, spl[0].length() - 1));
                        }
                        else {
                            rs5 = query.createResource(spl[0]);
                        }
                        String var3 = "";
                        if (this.blanknodes.containsKey(spl[2].substring(2))) {
                            var3 = this.blanknodes.get(spl[2].substring(2));
                        }
                        else {
                            var3 = "v_" + spl[2].substring(2);
                            this.blanknodes.put(spl[2].substring(2), var3);
                        }
                        final Resource rs4 = query.createResource(var3);
                        final String st3 = "v_" + spl[1].substring(2);
                        final Property pr3 = query.createProperty(st3);
                        query.add(rs5, pr3, rs4);
                    }
                    else {
                        Resource rs5 = null;
                        if (this.prefixs.containsKey(this.getprefx(spl[0]))) {
                            rs5 = query.createResource(this.prefixs.get(this.getprefx(spl[0])) + this.getname(spl[0]));
                        }
                        else if (spl[0].charAt(0) == '<') {
                            rs5 = query.createResource(spl[0].substring(1, spl[0].length() - 1));
                        }
                        else {
                            rs5 = query.createResource(spl[0]);
                        }
                        final String st4 = "v_" + spl[1].substring(2);
                        final Property pr4 = query.createProperty(st4);
                        Resource rs2 = null;
                        Literal lt2 = null;
                        if (this.prefixs.containsKey(this.getprefx(spl[2]))) {
                            rs2 = query.createResource(this.prefixs.get(this.getprefx(spl[2])) + this.getname(spl[2]));
                        }
                        else if (spl[2].charAt(0) == '<') {
                            rs2 = query.createResource(spl[2].substring(1, spl[2].length() - 1));
                        }
                        else if (spl[2].charAt(0) == '"') {
                            lt2 = query.createLiteral(spl[2].substring(1, spl[2].length() - 1));
                        }
                        else {
                            rs2 = query.createResource(spl[2]);
                        }
                        if (rs2 != null) {
                            query.add(rs5, pr4, rs2);
                        }
                        else {
                            query.add(rs5, pr4, lt2);
                        }
                    }
                }
                else if (spl[2].charAt(0) == '_') {
                    Resource rs5 = null;
                    if (this.prefixs.containsKey(this.getprefx(spl[0]))) {
                        rs5 = query.createResource(this.prefixs.get(this.getprefx(spl[0])) + this.getname(spl[0]));
                    }
                    else if (spl[0].charAt(0) == '<') {
                        rs5 = query.createResource(spl[0].substring(1, spl[0].length() - 1));
                    }
                    else {
                        rs5 = query.createResource(spl[0]);
                    }
                    Property pr5 = null;
                    if (spl[1].equals("a")) {
                        pr5 = query.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                    }
                    else if (this.prefixs.containsKey(this.getprefx(spl[1]))) {
                        pr5 = query.createProperty(this.prefixs.get(this.getprefx(spl[1])) + this.getname(spl[1]));
                    }
                    else if (this.prefixs.containsKey(this.getprefx(spl[1]))) {
                        pr5 = query.createProperty(this.prefixs.get(this.getprefx(spl[1])) + this.getname(spl[1]));
                    }
                    else if (spl[1].charAt(0) == '<') {
                        pr5 = query.createProperty(spl[1].substring(1, spl[1].length() - 1));
                    }
                    else {
                        pr5 = query.createProperty(spl[1]);
                    }
                    String var2 = "";
                    if (this.blanknodes.containsKey(spl[2].substring(2))) {
                        var2 = this.blanknodes.get(spl[2].substring(2));
                    }
                    else {
                        var2 = "v_" + spl[2].substring(2);
                        this.blanknodes.put(spl[2].substring(2), var2);
                    }
                    final Resource rs2 = query.createResource(var2);
                    query.add(rs5, pr5, rs2);
                }
                else {
                    Resource rs5 = null;
                    if (this.prefixs.containsKey(this.getprefx(spl[0]))) {
                        rs5 = query.createResource(this.prefixs.get(this.getprefx(spl[0])) + this.getname(spl[0]));
                    }
                    else if (spl[0].charAt(0) == '<') {
                        rs5 = query.createResource(spl[0].substring(1, spl[0].length() - 1));
                    }
                    else {
                        rs5 = query.createResource(spl[0]);
                    }
                    Property pr5 = null;
                    if (spl[1].equals("a")) {
                        pr5 = query.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                    }
                    else if (this.prefixs.containsKey(this.getprefx(spl[1]))) {
                        pr5 = query.createProperty(this.prefixs.get(this.getprefx(spl[1])) + this.getname(spl[1]));
                    }
                    else if (this.prefixs.containsKey(this.getprefx(spl[1]))) {
                        pr5 = query.createProperty(this.prefixs.get(this.getprefx(spl[1])) + this.getname(spl[1]));
                    }
                    else if (spl[1].charAt(0) == '<') {
                        pr5 = query.createProperty(spl[1].substring(1, spl[1].length() - 1));
                    }
                    else {
                        pr5 = query.createProperty(spl[1]);
                    }
                    Resource rs4 = null;
                    Literal lt3 = null;
                    if (this.prefixs.containsKey(this.getprefx(spl[2]))) {
                        rs4 = query.createResource(this.prefixs.get(this.getprefx(spl[2])) + this.getname(spl[2]));
                    }
                    else if (spl[2].charAt(0) == '<') {
                        rs4 = query.createResource(spl[2].substring(1, spl[2].length() - 1));
                    }
                    else if (spl[2].charAt(0) == '"' & spl[2].length() > 2) {
                        lt3 = query.createLiteral(spl[2].substring(1, spl[2].length() - 1));
                    }
                    else {
                        rs4 = query.createResource(spl[2]);
                    }
                    if (rs4 != null) {
                        query.add(rs5, pr5, rs4);
                    }
                    else {
                        query.add(rs5, pr5, lt3);
                    }
                }
            }
        }
    }

    public String getprefx(final String ligne) {
        final String[] ss = ligne.split(":");
        return ss[0];
    }

    public String getname(final String ligne) {
        final String[] ss = ligne.split(":");
        return ss[1];
    }

}
