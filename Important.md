# 🔒 Hashing vs Encryption

## 🟢 Hashing

Hashing is the `process of converting data` of arbitrary size into `a fixed-length string of characters`, called a **hash value** or **digest**, in such a way that it is **computationally infeasible to reverse** or recover the original data.  
- It is **irreversible** — you `cannot reconstruct the original data from the hash`.

**Example:**  
📝 `SHA-256` always produces a 256-bit hash, no matter the input size.

---

## 🟡 Encryption

Encryption is the `process of converting data` into an `unreadable form (ciphertext)` so that `only authorized parties` with the `correct key` can reverse it and recover the original data.

🔐 Encryption converts data into a form that can **only be read if you have the correct key**.

### 🔑 Symmetric Encryption
- 🔄 Same key is used to `encrypt and decrypt`.

### 🗝️ Asymmetric Encryption (Public/Private Key)
- 🔹 Data encrypted with `public key` can only be decrypted with the corresponding `private key`, and vice versa.

#### Example: Sending a confidential message
1. 👩 Alice wants to send a secret message to Bob.  
2. 📬 Bob shares his **public key** with Alice.  
3. ✉️ Alice encrypts the message with Bob’s **public key**.  
4. 🛡️ Only Bob can decrypt it with his **private key**. ✅  
   This keeps the message **confidential**.

> ⚠️ Unlike hashing, encryption is **reversible** if you have the correct key.
