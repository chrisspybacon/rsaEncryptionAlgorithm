package rsa;

import BigInt.BigInt;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RsaRecipient extends RsaInstance
{
    private RsaInstance rsaKey;
    private String name;

    public RsaRecipient(String name, RsaInstance rsaInstance)
    {
        this.name = name;
        this.rsaKey = rsaInstance;
    }

    public String decryptMessage()
    { // entschlüsselt encryptedMessage mit dem private-key
        ArrayList<BigInt> encryptedMessage = new ArrayList<>();
        try
        {
            File readFile = new File("../Main/messages.txt");
            Scanner reader = new Scanner(readFile);
            while (reader.hasNextLine())
            {
                String nextLine = reader.nextLine();
                if (nextLine == "")
                {
                    continue;
                }
                encryptedMessage.add(new BigInt(nextLine));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.print("Error! Could not read file: ");
            System.out.print(e.toString());
        }
        ArrayList<BigInt> convertedMessage = new ArrayList<>();
        for (int i=0;i<encryptedMessage.size();i++)
        {
            convertedMessage.add(encryptedMessage.get(i).raiseToPowerMod(this.rsaKey.privateKey, this.rsaKey.publicKey.first()));
        }
        String message = convertArraylistToString(convertedMessage);
        return message;
    }

    private String convertArraylistToString(ArrayList<BigInt> message)
    {
        String result = "";
        for (int i=0;i<message.size();i++)
        {
            int currentCodePoint = 0;
            try
            {
                currentCodePoint = Integer.parseInt(message.get(i).getValueString());
            } catch (NumberFormatException e) {
                System.out.println("Number is too big to be parsed into an Integer!");
            }
            try
            {
                result += String.valueOf(Character.toChars(currentCodePoint));
            } catch (IllegalArgumentException e) {
                System.out.println(currentCodePoint + " is not a valid Unicode code point!");
            }
        }
        return result;
    }

    public String getName()
    {
        return this.name;
    }
}