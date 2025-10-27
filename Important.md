# 🔒 Hashing vs Encryption

## 🟢 Hashing

Hashing is the `process of converting data` of arbitrary size into `a fixed-length string of characters`, called a **hash value** or **digest**, in such a way that it is **computationally infeasible to reverse** or recover the original data.  
- It is **irreversible** — you `cannot reconstruct the original data from the hash`.

**Example:**  
📝 `SHA-256` always produces a 256-bit hash, no matter the input size.

### 🛠️ Use Cases of Hashing

- 🔐 **Password Storage:** Store hashes of passwords instead of plain text.  
- 🧾 **Data Integrity:** Verify that files or messages haven’t been tampered with.  
- ✉️ **Digital Signatures:** Confirm the authenticity of messages.  
- 🔄 **Checksum Verification:** Ensure downloaded files are complete and unaltered.  
- ⚠️ **Detect Duplicates:** Quickly compare large datasets.

### ⚠️ Additional Points

- 🧩 **Salting:** Adding a random string to passwords before hashing to prevent rainbow table attacks.  
- ⚡ **Popular Algorithms:** MD5 (weak), SHA-1 (weak), SHA-256, SHA-3.  
- 🧬 **Collision Resistance:** Strong hash algorithms minimize the chance of two different inputs producing the same hash.  


> ⚠️ Hash collisions: Rarely, two different inputs can produce the same hash. Modern algorithms like SHA-256 minimize this risk.


---

## 🟡 Encryption

Encryption is the `process of converting data` into an `unreadable form (ciphertext)` so that `only authorized parties` with the `correct key` can reverse it and recover the original data.

🔐 Encryption converts data into a form that can **only be read if you have the correct key**.

### 🔑 Symmetric Encryption
- 🔄 Same key is used to `encrypt and decrypt`.  
- **Example:** AES (Advanced Encryption Standard)  
- ⚡ **Faster** than asymmetric encryption.

### 🗝️ Asymmetric Encryption (Public/Private Key)
- 🔹 Data encrypted with `public key` can only be decrypted with the corresponding `private key`, and vice versa.  
- **Example:** RSA  
- 🐢 **Slower**, mostly used for secure key exchange.

#### Example: Sending a confidential message
1. 👩 Alice wants to send a secret message to Bob.  
2. 📬 Bob shares his **public key** with Alice.  
3. ✉️ Alice encrypts the message with Bob’s **public key**.  
4. 🛡️ Only Bob can decrypt it with his **private key**. ✅  
   This keeps the message **confidential**.

### 🛠️ Use Cases of Encryption

- 🔐 **Secure Communication:** HTTPS, emails, messaging apps (WhatsApp, Signal).  
- 🗄️ **Data Storage:** Encrypt files or databases to protect sensitive data.  
- 🌐 **VPNs:** Encrypt internet traffic to ensure privacy.  
- 🏦 **Financial Transactions:** Secure online banking and payments.  
- 🔁 **Cloud Security:** Protect data stored in the cloud from unauthorized access.

### ⚠️ Additional Points

- 🔄 **Hybrid Encryption:** Often combines asymmetric (for key exchange) and symmetric (for data transfer) for efficiency.  
- ⚠️ **Deprecated Algorithms:** DES, RC4 are insecure. Use AES, RSA, ECC.  
- 🕰️ **Performance:** Symmetric is faster; asymmetric is slower but safer for key exchange.

> ⚠️ Unlike hashing, encryption is **reversible** if you have the correct key.


---

# 🌐 HTTP (Hypertext Transfer Protocol)

- HTTP is a set of rules that allows web browsers and servers to communicate and share information over the internet.

  - 💬 Lets your browser talk to a server.
  - 📄 Sends data in plain text → anyone can see it.
  - 🔒 Not secure.
  - ⚡ Fast and lightweight (no encryption).
  - 🔢 Default port: 80.

- 📝 Plain text means the `data is sent exactly as it is `, `without any encryption`. Anyone who can see it on the network can read it directly.  
  - If someone is listening on the network,  
    - 👂 They can read it easily because it’s not hidden.

### 🛠️ Use Cases of HTTP

- 🌐 **Browsing Websites:** Standard web page requests where security isn’t critical.  
- 📰 **Public APIs:** Open APIs that don’t require encryption.  
- 📚 **Documentation & Blogs:** Serving general information without sensitive data.  
- 🏷️ **Testing & Development:** Easy, fast, lightweight communication during development.

### ⚠️ Additional Points

- 🔄 **Versions:** HTTP/1.1 (persistent connections), HTTP/2 (multiplexing), HTTP/3 (QUIC protocol).  
- ⚠️ **Not for Sensitive Data:** Never use HTTP for login, password, or payment pages.

---

# 🔒 HTTPS (Hypertext Transfer Protocol Secure)

- HTTPS is a secure version of HTTP that uses encryption (SSL/TLS) to protect the data transferred between a web browser and a web server from being read or modified by others.

  - 💬 Same as HTTP but secure.
  - 🔐 Data is encrypted using SSL/TLS → only the server can read it.
  - 🗝️ Uses public and private keys to encrypt/decrypt data.
  - 📜 Requires an SSL certificate from a trusted Certificate Authority (CA).
  - 🐢 Slower than HTTP because encryption adds extra work.
  - 🔢 Default port: 443.
  - 🔏 `Encrypted text (ciphertext)`: looks like `gibberish`, e.g., 6f3d9a1b2c7e...

## How HTTPS works:

1. 🌐 The browser connects to the server using HTTPS.
2. 🏷️ The server sends its SSL certificate to the browser.
3. ✅ The browser checks if the certificate is valid and trusted.
4. 🔑 If it’s valid, the browser and server exchange keys securely.
5. 📩 The browser sends the request (like asking for a webpage) → it is encrypted.
6. 🖥️ The server decrypts the request, processes it, and prepares a response.
7. 📦 The server sends back the response → it is also encrypted.
8. 🖥️ The browser decrypts the response and displays the webpage to the user.


### 🛠️ Use Cases of HTTPS

- 🏦 **Online Banking & Payments:** Protect sensitive financial data.  
- 💌 **Secure Email & Messaging:** Keep communications private.  
- 🌐 **E-commerce Websites:** Protect customers’ personal information.  
- 🛡️ **Authentication & Login Pages:** Secure password transmission.  
- ☁️ **Cloud Services:** Encrypt data exchanged between client and server.  

### ⚠️ Additional Points

- 🏷️ **Certificate Validation:** Expired or self-signed certificates can trigger browser warnings.  
- 🔒 **TLS Versions:** TLS 1.2 and TLS 1.3 are recommended; SSL is outdated.  
- 🧩 **HSTS (HTTP Strict Transport Security):** Forces browsers to use HTTPS.  
- 🌍 **Mixed Content:** Loading HTTP resources on HTTPS pages can break security.  

> ✅ Recommended for all modern websites to ensure security and trust.



---

