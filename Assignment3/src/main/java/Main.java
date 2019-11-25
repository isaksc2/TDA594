import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.*;
import org.sat4j.tools.ModelIterator;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args){
        ISolver solver = SolverFactory.newDefault();
        ModelIterator mi = new ModelIterator(solver);
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);

        int[] a = {-1, -2, 3};
        IVecInt assumptions = new VecInt(a);
        try {
            boolean unsat = true;

            IProblem problem = reader.parseInstance(Main.class.getClassLoader().getResourceAsStream("small.dimacs"));
            if (problem.isSatisfiable()) {
                int[] model = problem.model();
                for (int i : model) {
                    System.out.print(i + " ");

                    if (problem.isSatisfiable(assumptions)) {
                        System.out.println("satisfied");
                    }
                    else {
                        System.out.println("not satisfied");
                    }
                }
                System.out.println();

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
}
