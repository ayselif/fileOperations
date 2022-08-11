import java.io.*;
import java.util.Scanner;

public class FileOperations {
    private String fileName;
    private File file;

    FileOperations(String fileName) throws IOException  {
        this.fileName = fileName;
        this.file = getFile();
    }

    public void start() throws IOException {
        String mail = getMail();

        if (isMailValid(mail)) {
            //Is Existing user
            if (isMailFounded(file, mail)) {
                System.out.println("Welcome " + mail);
                System.exit(0);
            } else {
                writeToFile(file, mail);
                System.out.println("New user added.");
                start();
            }
        } else {
            System.out.println("Your mail is not valid.");
            start();
        }
    }

    private String getMail(){
        System.out.println("Enter your e-mail for login");
        Scanner sc = new Scanner(System.in);
        String inputMail = sc.nextLine();
        return inputMail;
    }


    private File getFile() throws IOException {
        File file = new File(fileName);
        if(!file.exists()){
            file.createNewFile();
        }

        return file;
    }

    private Boolean isMailFounded(File mailFile, String mail) throws IOException {
        FileReader fileReader = new FileReader(mailFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;

        while ((line = bufferedReader.readLine()) != null){
            if (line.equals(mail)){
                fileReader.close();
                return true;
            }
        }
        fileReader.close();
        return false;
    }

    private Boolean isMailValid(String mail) {
        if(mail.length()<3){
            return false;
        }
        return true;
    }

    private void writeToFile(File mailFile, String mail) throws IOException {
        FileWriter fileWriter = new FileWriter(mailFile, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("\n"+mail);
        bufferedWriter.close();
    }

    public static void main(String[] args) throws IOException {
        FileOperations fileOperations = new FileOperations("eMail.txt");
        fileOperations.start();
    }
}
