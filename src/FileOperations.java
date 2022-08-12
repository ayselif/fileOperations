import java.io.*;
import java.util.ArrayList;
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
            System.out.println("Welcome " + user.name);
            showMenu(user);
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

    private String getNewPassword(){
        System.out.println("Enter your new password");
        String newPassword = getPassword();
        return newPassword;
    }

    private void writeNewPasswordToFile(File file, User user, String newPassword) throws IOException{
        ArrayList<String> userInfoList = getUsersListWithoutUser(file, user);
        FileWriter fileWriter = new FileWriter(file,false);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        String newUserInfo = user.name+";"+user.mail+";"+ newPassword;
        userInfoList.add(newUserInfo);
        String userInfoTextList =  String.join("\n", userInfoList);
        bufferedWriter.write(userInfoTextList);
        bufferedWriter.close();

        user.password = newPassword;
    }

    public ArrayList<String> getUsersListWithoutUser(File file, User user) throws IOException {
        ArrayList <String> users = new ArrayList<String>();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String userInfo;
        while ((userInfo = bufferedReader.readLine()) != null) {
            User currentUser = parseRecord(userInfo);
            System.out.println(currentUser.mail);
            System.out.println(userInfo);
            if(!user.mail.equals(currentUser.mail)){
                users.add(userInfo);
            }
        }

        System.out.println(users);
        return users;
    }


    private User parseRecord(String userInfo){
        String [] userInfoArr = userInfo.split(";",3);
        return new User(userInfoArr[0], userInfoArr[1], userInfoArr[2]);
    }

    private void showMenu(User user) throws IOException {
        System.out.println("Select Option: " + "\n" +"For Exit: Press 1 "+"\n"+"For Change Password: Press 2 ");
        char select = sc.nextLine().charAt(0);
        switch (select){
            case '1':
                start();
                break;
            case '2':
                String password = getNewPassword();
                writeNewPasswordToFile(file,user,password);
                break;
            default:
                System.out.println("Wrong selection.");
                showMenu(user);
        }
    }

    public static void main(String[] args) throws IOException {
        FileOperations fileOperations = new FileOperations("user.txt");
        fileOperations.start();
    }
}
