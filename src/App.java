import java.util.*;
public class App{
    public static void main(String[] args){
        System.out.print("Compromised Password Checker\nCheck if your password has been found in databases of compromised passwords.");
        Scanner sc = new Scanner(System.in);
        String email = "";
        String password = "";
        System.out.println("Please enter your email: ");
        email = sc.next();
        System.out.println("Please enter your password to check if it is in the compromised passwords database: ");
        password = sc.next();
        
    }
}