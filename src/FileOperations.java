import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class FileOperations {
    private String fileName;
    private File file;
    private Scanner sc = new Scanner(System.in);

    FileOperations(String fileName) throws IOException  {
        this.fileName = fileName;
        this.file = getFile();
    }

    public void start() throws IOException {
        String mail = getMail();
        User foundedUser = getUser(file, mail);

        if (foundedUser != null) {
            checkPassword(foundedUser);
            System.exit(0);
        } else {
            System.out.println("User not found, try again");
            start();
        }
    }

    private void checkPassword(User user) throws IOException {
        String password = getPassword();

        if(user.password.equals(password)){
            System.out.println("Welcome" + user.name);
            showMenu();
        } else {
            System.out.println("Wrong password try again");
            checkPassword(user);
        }
    }

    private String getMail(){
        System.out.println("Enter your e-mail for login");
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

    private User getUser(File userFile, String mail) throws IOException {
        if (!isMailValid(mail)) {
            return null;
        }

        FileReader fileReader = new FileReader(userFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String userRecord;
        while ((userRecord = bufferedReader.readLine()) != null){
            User user = parseRecord(userRecord);
            if (user.mail.equals(mail)){
                return user;
            }
        }

        fileReader.close();
        return null;
    }

    private Boolean isMailValid(String mail) {
        if(mail.length()<10){
            return false;
        }
        return true;
    }

    public Boolean isValidPassword(String password){
        if(password.length()<4){
           return false;
        }
        return true;
    }

    public String getPassword() {
        System.out.println("Please enter your password");
        String password = sc.nextLine();
        if(!isValidPassword(password)){
            System.out.println("This password is invalid.");
            password = getPassword();
        }
        return password;
    }

    private User parseRecord(String userInfo){
        String [] userInfoArr = userInfo.split(";",3);
        return new User(userInfoArr[0], userInfoArr[1], userInfoArr[2]);
    }

    private void showMenu() throws IOException {
        System.out.println("Select Option: " + "\n" +"For Exit: Press 1 "+"\n"+"For Change Password: Press 2 ");
        char select = sc.nextLine().charAt(0);
        switch (select){
            case '1':
                start();
                break;
            case '2':
                System.out.println("code will continue...");
                break;
            default:
                System.out.println("Wrong selection.");
                showMenu();
        }
    }

    public static void main(String[] args) throws IOException {
        FileOperations fileOperations = new FileOperations("user.txt");
        fileOperations.start();
    }
}
