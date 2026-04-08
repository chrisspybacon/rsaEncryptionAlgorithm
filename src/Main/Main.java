package Main;

import rsa.RsaInstance;
import rsa.RsaRecipient;

import java.util.*;

public class Main
{
    public void main(String[] args)
    {
        RsaInstance rsaInstance = new RsaInstance();
        RsaSender rsaSender = new RsaSender("Bob", rsaInstance);
        Scanner sc = new Scanner(System.in);
        System.out.println("Message to be encrypted:");
        String message = sc.nextLine();
        sc.close();
        RsaRecipient rsaRecipient = new RsaRecipient("Alice", rsaInstance);
        rsaSender.encryptMessage(message);
        System.out.println(rsaRecipient.decryptMessage());
    }
}