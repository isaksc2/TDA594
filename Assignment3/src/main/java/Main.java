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
    static String filename = "small.dimacs";

    public static void main(String[] args){
        ISolver solver = SolverFactory.newDefault();
        ModelIterator mi = new ModelIterator(solver);
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);


        try {
            boolean unsat = true;

            IProblem problem = reader.parseInstance(Main.class.getClassLoader().getResourceAsStream(filename));
            if (problem.isSatisfiable()) {
                System.out.println("Problem is satisfiable!");
                //int[] model = problem.model();
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
            }
//            while (problem.isSatisfiable(assumptions)) {
//                //solver.add
//                unsat = false;
//                int[] model = problem.model();
//                for (int i : model) {
//                    System.out.print(i + " ");
//                }
//                System.out.println();
//            }
//            if (unsat) {
//                System.out.println("Unsatisfiable !");
//                // do something for unsat case
//            }
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
