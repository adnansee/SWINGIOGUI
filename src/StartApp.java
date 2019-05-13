import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

class StartApp {
    private List<String> summaryList = new ArrayList<>();


    void pleaseStart(Path resources) throws IOException {

        Path sortedFolder = resources.resolve("sortedFolder");
        Files.createDirectory(sortedFolder);

        File[] files = new File(String.valueOf(resources)).listFiles();
        List<File> onlyFiles = new ArrayList<>();
        for (File f : files
        ) {
            if (!f.isDirectory()) {
                onlyFiles.add(f);
            }

        }

        copyFiles(onlyFiles, sortedFolder);
        files = new File(String.valueOf(sortedFolder)).listFiles();
        Path summary = sortedFolder.resolve("summary");
        Files.createDirectory(summary);
        Path summaryTxt = summary.resolve("summary.txt");
        Files.createFile(summaryTxt);
        assert files != null;
        makeSummary(files);
        Files.write(summaryTxt, summaryList);

    }

    private void makeSummary(File[] files) throws IOException {
        for (File file : files) {
            if (file.isDirectory()) {
                if (!file.getPath().equals("summary.txt")) {
                    System.out.println("Directory: " + file.getName());
                    summaryList.add(file.getName() + ":");
                    summaryList.add("-----\n");
                    makeSummary(Objects.requireNonNull(file.listFiles())); // Calls same method again.
                    summaryList.add("-----");
                }

            } else { // file

                summaryList.add(String.format("%-20s|%11c          |%11c          |%-20s          |", file.getName(),
                        (Files.isReadable(file.toPath()) ? 'x' : '/'),
                        (Files.isWritable(file.toPath()) ? 'x' : '/'),
                        (Files.getLastModifiedTime(file.toPath()))

                ));
            }
        }

    }


    private void copyFiles(List<File> onlyFiles, Path sortedFolder) {
        for (File file : onlyFiles) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                copyFiles(Arrays.asList(Objects.requireNonNull(file.listFiles())), sortedFolder); // Calls same method again.
            } else {

                String extension = "";

                int i = file.getName().lastIndexOf('.');
                if (i > 0) {

                    extension = file.getName().substring(i + 1);

                } else // empty name
                    extension = file.getName().substring(1);

                System.out.println(",  and extension = " + extension);
                Path newPath = sortedFolder.resolve(extension);

                try {

                    Files.createDirectories(newPath);

                    Path newFile = newPath.resolve(file.getName());
                    Scanner sc = null;
                    boolean overwriteAll = false, overwriteNone = false;

                    if ((new File(String.valueOf(newFile))).exists()) {
                        boolean overwrite = false;
                        if (!overwriteNone) {
                            System.out.println("File exists, Do you want to overwrite ? No, Yes, Yes to all, No to all");
                            boolean validInput = false;
                            while (!validInput) {
                                validInput = true;
                                assert false;
                                String input = sc.nextLine();
                                switch (input) {
                                    case "No":
                                        break;
                                    case "Yes":
                                        overwrite = true;
                                        break;
                                    case "Yes to all":
                                        overwriteAll = true;
                                        break;
                                    case "No to all":
                                        overwriteNone = true;
                                        break;
                                    default:
                                        validInput = false;
                                        System.out.println("Wrong answer. File exists, Do you want to overwrite ? No, Yes, Yes to all, No to all");
                                }
                            }
                        }
                        if (overwrite || overwriteAll) {
                            Files.copy(file.toPath(), newFile, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println(newFile + " overwritten");
                        }
                    } else {
                        Files.copy(file.toPath(), newFile, StandardCopyOption.COPY_ATTRIBUTES);
                        System.out.println(newFile + " written");
                    }

                } catch (IOException se) {

                    se.printStackTrace();
                }
            }
        }

    }


}
