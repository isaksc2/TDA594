import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.*;
import org.sat4j.tools.ModelIterator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String filename = "ecos_x86.dimacs";
    static ISolver solver;
    static Reader reader;

    public static void main(String[] args){
        solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        reader = new DimacsReader(solver);

        try {
            IProblem problem = reader.parseInstance(Main.class.getClassLoader().getResourceAsStream(filename));
            if (problem.isSatisfiable()) {
                System.out.println("Problem is satisfiable!");
                System.out.println("There are " + problem.nVars() + " variables!");
                int numDeadFeatures = 0;
                HashMap<Integer, String> names = findNames();
                // Loop over all variables.
                System.out.println("Dead features: ");
                int[] vars = problem.model();
                for (int var : vars) {
                    var = Math.abs(var);
                    int[] a = {var}; // set the current feature to be enabled (true)
                    IVecInt assumptions = new VecInt(a);
                    if (!problem.isSatisfiable(assumptions)) {
                        numDeadFeatures++;
                        System.out.println(var + " " + names.get(var));
                    }
                }
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

    static void findImplications(IProblem problem, HashMap<Integer, String > variables) throws FileNotFoundException, ParseFormatException,
            IOException, ContradictionException, TimeoutException{
        List<Integer> vars = new ArrayList<>();
        vars.addAll(variables.keySet());
        List<int[]> implications = new ArrayList<>();
        int count = 0;
        for (int var1 : vars){
            for (int var2 : vars) {
                if (var1 == var2) {
                    continue;
                }
                IVecInt assumptions = new VecInt();
                assumptions.push(var1);
                assumptions.push(-var2);
                boolean tf = problem.isSatisfiable(assumptions);
                if (!tf) {
                    int[] pair = {var1, var2};
                    implications.add(pair);
                }
            }
        }
        System.out.println(implications.size());
        System.out.println(count);
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
        }
        return names;
    }
}
