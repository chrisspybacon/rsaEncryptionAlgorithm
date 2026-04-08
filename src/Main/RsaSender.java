package Main;

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

import BigInt.BigInt;
import rsa.RsaInstance;

public class RsaSender {
    private RsaInstance rsaInstance;
    private String name;

    public RsaSender(String name, RsaInstance rsaInstance)
    {
        this.name = name;
        this.rsaInstance = rsaInstance;
    }

    public void encryptMessage(String message)
    { // verschlüsselt message anhand des public-keys
        ArrayList<BigInt> convertedMessage = this.convertStringToArraylist(message);
        ArrayList<BigInt> convertedEncryptedMessage = new ArrayList<>();
        for (int i=0;i<convertedMessage.size();i++)
        {
            convertedEncryptedMessage.add(convertedMessage.get(i).raiseToPowerMod(this.rsaInstance.getPublicKey().second(), this.rsaInstance.getPublicKey().first()));
        }
        try
        {
            FileWriter messageFile = new FileWriter("messages.txt", false);
            for (int i=0;i<convertedEncryptedMessage.size();i++)
            {
                messageFile.append(convertedEncryptedMessage.get(i).getValueString() + "\n");
            }
            messageFile.close();
        } catch (IOException e) {
            System.out.print("Error: ");
            System.out.print(e.toString());
        }
    }

    private ArrayList<BigInt> convertStringToArraylist(String message)
    {
        ArrayList<BigInt> result = new ArrayList<BigInt>();
        for (int i=0;i<message.length();i++)
        {
            char[] currentChar = new char[]{message.charAt(i)};
            result.add(new BigInt(Integer.toString(Character.codePointAt(currentChar, 0))));
        }
        return result;
    }

    public String getName()
    {
        return this.name;
    }
}
