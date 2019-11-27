import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.*;

import java.util.*;
import java.io.*;

public class Main {
    static String filename = "small.dimacs";
    static ISolver solver;
    static Reader reader;
    static WriteToFile writer;
    static List<int[]> implications = new ArrayList<>();

    public static void main(String[] args){
        solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        reader = new DimacsReader(solver);
        writer = new WriteToFile();

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

    static void findImplications(IProblem problem, HashMap<Integer, String > nameMap) throws FileNotFoundException, ParseFormatException,
            IOException, ContradictionException, TimeoutException{
        List<Integer> vars = new ArrayList<>();
        vars.addAll(nameMap.keySet());
        for (int var1 : vars){
            for(int iVar2=vars.indexOf(var1); iVar2<vars.size(); iVar2++){
                int a = var1;
                int b = vars.get(iVar2);
                IVecInt testassump = new VecInt();
                testassump.push(a);
                testassump.push(-b);
                Boolean test = problem.isSatisfiable(testassump);
                if(test){
                    int[] pair = {b,a };
                    implications.add(pair);
                }
                IVecInt testassump2 = new VecInt();
                testassump2.push(-a);
                testassump2.push(b);
                Boolean test2 = problem.isSatisfiable(testassump);
                if(test2){
                    int[] pair2 = {a, b};
                    implications.add(pair2);
                }
                /*
                int[] a = {var1, vars.get(iVar2)};    // SS = S
                IVecInt assumptions = new VecInt(a);
                boolean ss = problem.isSatisfiable(assumptions);
                a[1] = -a[1];          // SF = S
                assumptions = new VecInt(a);
                boolean sf = problem.isSatisfiable(assumptions);
                a[0] = -a[0];      // FF = S
                assumptions = new VecInt(a);
                boolean ff = problem.isSatisfiable(assumptions);
                a[1] = -a[1];         // FS = F
                assumptions = new VecInt(a);
                boolean fs = problem.isSatisfiable(assumptions);
                if(ss && fs && ff && !sf){
                    int[] pair = {a[0], -a[1]};
                    implications.add(pair);
                }
                if(ss && sf && ff && !fs){
                    int[] pair = {-a[1], a[0]};
                    implications.add(pair);
                }*/
            }
        }
        System.out.println(implications.size());
        writeImplicationsToFile(nameMap);

    }
    static void writeImplicationsToFile(HashMap<Integer, String > nameMap){
        StringBuilder stringToWrite = new StringBuilder();
        Iterator it = implications.iterator();
        while(it.hasNext()) {
            int[] item = (int[]) it.next();
            stringToWrite.append("{" + nameMap.get(item[0]) + " ==> " + nameMap.get(item[1]) + "}" + "\n");
            writer.WriteText(stringToWrite.toString());
        }
        System.out.println("Implications written");
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
