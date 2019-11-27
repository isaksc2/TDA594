import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.*;

import java.util.*;
import java.io.*;

public class Main {
    static String filename = "ecos_x86.dimacs";
    static ISolver solver;
    static Reader reader;
    static List<int[]> implications = new ArrayList<>();

    public static void main(String[] args){
        solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        reader = new DimacsReader(solver);

        try {
            IProblem problem = reader.parseInstance(Main.class.getClassLoader().getResourceAsStream(filename));
            if (problem.isSatisfiable()) {
                System.out.println("Problem is satisfiable!");
                System.out.println("There are " + problem.nVars() + " variables!");
                HashMap<Integer, String> names = findNames();
                // Loop over all variables.
                System.out.println("Dead features: ");
                int numDeadFeatures = printAndCountDeadFeatures(problem,names);
                System.out.println("There are " + numDeadFeatures + " dead features!");
                findImplications(problem, names);
            }
//
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");
        }
    }
    static int printAndCountDeadFeatures(IProblem problem, HashMap<Integer, String> names) {
        int numDeadFeatures = 0;
        int[] vars = problem.model();
        try {
            for (int var : vars) {
                var = Math.abs(var);
                int[] a = {var}; // set the current feature to be enabled (true)
                IVecInt assumptions = new VecInt(a);
                if (!problem.isSatisfiable(assumptions)) {
                    numDeadFeatures++;
                    System.out.println(var + " " + names.get(var));
                }
            }
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");
        }
        return numDeadFeatures;
    }


    static void findImplications(IProblem problem, HashMap<Integer, String > nameMap) throws FileNotFoundException, ParseFormatException,
            IOException, ContradictionException, TimeoutException{
        List<Integer> vars = new ArrayList<>();
        vars.addAll(nameMap.keySet());
        for (int var1 : vars){
            for(int iVar2=vars.indexOf(var1)+1; iVar2<vars.size(); iVar2++){
                int a = var1;
                int b = vars.get(iVar2);

                // SF = S
                IVecInt assumptions2 = new VecInt();
                assumptions2.push(a);
                assumptions2.push(-b);
                boolean sf = problem.isSatisfiable(assumptions2);

                // FS = F
                IVecInt assumptions4 = new VecInt();
                assumptions4.push(-a);
                assumptions4.push(b);
                boolean fs = problem.isSatisfiable(assumptions4);

                if(!sf){
                    int[] pair = {a, b};
                    implications.add(pair);
                }
                if(!fs){
                    int[] pair = {b, a};
                    implications.add(pair);
                }
            }
        }
        System.out.println(implications.size());
        // Comment out this for faster calculation, writing to file takes time.
        WriteToFile.writeImplicationsToFile(implications, nameMap);
    }


    static  HashMap<Integer, String> findNames() {
        HashMap<Integer, String> names = new HashMap<Integer, String>();
        InputStream stream = Main.class.getClassLoader().getResourceAsStream(filename);
        Scanner sc = new Scanner(stream);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.startsWith("c")) {
                String[] components = line.split(" ");
                names.put(Integer.parseInt(components[1]), components[2]);
            }
            else {
                break;
            }
        }
        return names;
    }
}
