import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class WriteToFile {
    /*
    Iterate through the list of implications and mapping the int to corresponding
    name and send it as a string to the WriteText function.
    */
    static void writeImplicationsToFile(List implications, HashMap<Integer, String > nameMap){
        StringBuilder stringToWrite = new StringBuilder();
        Iterator it = implications.iterator();
        while(it.hasNext()) {
            int[] item = (int[]) it.next();
            stringToWrite.append("{" + nameMap.get(item[0]) + " ==> " + nameMap.get(item[1]) + "}" + "\n");
            WriteText(stringToWrite.toString());
        }
        System.out.println("Implications written");
    }
        /*
        Creates a file if there is none on desktop with name implications.txt
        writes the string which is given to that file
        When restarting the program it writes over the old content.
        */
        static void WriteText(String text){
            try {
                String userHomeFolder = System.getProperty("user.home");
                File statText = new File(userHomeFolder + "/desktop/implications.txt");
                FileOutputStream is = new FileOutputStream(statText);
                OutputStreamWriter osw = new OutputStreamWriter(is);
                Writer w = new BufferedWriter(osw);
                w.write(text);
                w.close();
            } catch (IOException e) {
                System.err.println("Problem writing to the file statsTest.txt");
            }
        }
    }