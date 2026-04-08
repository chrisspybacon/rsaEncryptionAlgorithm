This repository contains source code implementing the RSA encryption algorithm. It further contains a custom BigInt class which may be used for arbitrary precision integer arithmetic (and is also required by the RsaInstance class as the algorithm uses 15-digit primes).

WARNING:
This program uses curl to generate prime numbers. It accesses https://big-primes.ue.r.appspot.com/primes?digits=[DIGITS], an API which I found in the source code of https://bigprimes.org/. I first used this website to generate primes manually and hard-code them.

I tested this program recently on Fedora Workstation 43.

This program was an old project of mine that I updated and only now uploaded on GitHub.

---
How to use:

1. The user is prompted to input a message which should be encrypted.

2. The encrypted message is saved in messages.txt and can only be decrypted (well except if we implement an efficient method to factor primes ;)) by an instance of RsaRecipient that has the same RsaInstance as the respective RsaSender.
