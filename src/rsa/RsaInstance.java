package rsa;

import BigInt.BigInt;
import BigInt.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder;

public class RsaInstance {
    protected final Pair publicKey;
    protected final BigInt privateKey;

    public RsaInstance()
    {
        String prime1 = "";
        String prime2 = "";
        try
        {
            String[] command = {"curl", "https://big-primes.ue.r.appspot.com/primes?digits=10"};
            ProcessBuilder builder = new ProcessBuilder(command);
            Process process = builder.start();
            InputStream input = process.getInputStream();
            int currentChar = 0;
            String response = "";
            while ((currentChar = input.read()) != -1)
            {
                response += String.valueOf((char)currentChar);
            }
            prime1 = response.substring(response.indexOf("\"Primes\":[\"") + 11, response.indexOf("\"]")); // extrahiert den Teil der response, welcher die Primzahl enthält
            process = builder.start();
            input = process.getInputStream();
            currentChar = 0;
            response = "";
            while ((currentChar = input.read()) != -1)
            {
                response += String.valueOf((char)currentChar);
            }
            prime2 = response.substring(response.indexOf("\"Primes\":[\"") + 11, response.indexOf("\"]"));
        } catch (IOException e) {
            System.out.println("Error while fetching prime numbers!");
            System.out.println(e.toString());
        }
        BigInt p = new BigInt(prime1);
        BigInt q = new BigInt(prime2);
        BigInt phiN = (p.subtract(new BigInt("1"))).multiply(q.subtract(new BigInt("1")));
        BigInt e = phiN.generateRandomSmallerThanThis();
        while (!(e.isGreaterThanOrEqual(phiN)))
        {
            BigInt gcd = e.greatestCommonDivisor(phiN);
            if (gcd.getDecimalPlaces() == 1 && gcd.getValueAtPos(0) == 1)
            {
                break;
            }
            e = e.add(new BigInt("1"));
        }
        Pair bezout = phiN.bezoutCoefficients(e);
        if (!bezout.second().getPositive())
        {
            BigInt xPositive = bezout.second().add(phiN.leftShift(bezout.second().getDecimalPlaces() - phiN.getDecimalPlaces() + 1)); // garantiert x >= 0, wichtig für mod-Methode
            bezout = new Pair(new BigInt(), xPositive.mod(phiN));
        }
        BigInt d = new BigInt(bezout.second());
        while (!(d.mod(phiN).isEqualTo(bezout.second().mod(phiN))))
        {
            d.mod(phiN).printValue();
            bezout.second().mod(phiN).printValue();
            d = d.add(new BigInt("1"));
        }
        this.publicKey = new Pair(p.multiply(q), e);
        this.privateKey = d;
    }

    public Pair getPublicKey()
    {
        return this.publicKey;
    }
}
