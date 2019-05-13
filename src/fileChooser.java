import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;

public class fileChooser {

    public static void main(String[] args) throws IOException {

        JButton open = new JButton();
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new java.io.File(""));
        fc.setDialogTitle("Choose Folder To Filter");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(fc.showOpenDialog(open)== JFileChooser.APPROVE_OPTION){
            StartApp s = new StartApp();
            s.pleaseStart(Paths.get(fc.getSelectedFile().getAbsolutePath()));


        }
        System.out.println("You Choose "+fc.getSelectedFile().getAbsolutePath());
    }
}
