# 🔐 Spring Security Authentication & Authorization Flow

1. ### 👤 User Sends Login Request

      - The user enters username and password and submits a login request.

2. ### ⚙️ AuthenticationManager Receives Request
      - The login request (with credentials) is wrapped inside an `Authentication` object. 
      - Spring Security creates an initial `Authentication` object (usually a `UsernamePasswordAuthenticationToken`)  
        contains `only` the `username and password`.
      - This `Authentication` object is passed to the `AuthenticationManager`.

3. ### 🔄 AuthenticationManager Delegates to AuthenticationProvider

      - The `AuthenticationManager` forwards the request to one or more `AuthenticationProviders`.
      - Type of AuthenticationProviders
           1.  `DaoAuthenticationProvider`
           2.  `LdapAuthenticationProvider`
           3.  `JwtAuthenticationProvider`
           4.  `OAuth2LoginAuthenticationProvider` 
           5.  `OpenIDAuthenticationProvider` 
           6.  `Custom AuthenticationProvider`

4. ### 📋 AuthenticationProvider Loads User Details

      - The `AuthenticationProvider` calls the `UserDetailsService`.loadUserByUsername(`String username`) method,  
         passing the `username extracted` from the `Authentication` object.
      - `UserDetailsService` fetches user info (username, hashed password, roles) from the database. 
      - It returns a `UserDetails` object with this info.

5. ### 🔑 AuthenticationProvider Validates Credentials
      - The `AuthenticationProvider` uses a `PasswordEncoder` to compare:
           - The raw password entered by the user.
           - The hashed password stored in the database. 
      - If the passwords match → authentication succeeds. 
      - Otherwise, authentication fails (exception thrown).

6. ### 🔐 Store Authentication in SecurityContext
      - On success, the fully authenticated `Authentication` object is stored in the `SecurityContext`.  
      - This Fully authenticated object contains (with user details and granted roles / permission, isAuthenticated = true and so on..) 
      - `SecurityContext` is managed by `SecurityContextHolder` and linked to the current session or thread.
   
      - ❓ So Who Puts the Authentication Object in the SecurityContext?

7. ### 🚪 User Accesses Secured Endpoints

      - For each subsequent request, Spring Security checks the `SecurityContext` to find the logged-in user. 
      - Based on the user’s roles/authorities, it decides if the user is authorized to access the requested endpoint.

8. ### ✅ Authorization Decision

     - If authorized → request proceeds.
     - If not → access is denied (e.g., 403 Forbidden).

9. ### 🔒 Logout or Session Expiry

     - When the user logs out or the session expires, the `SecurityContext` is cleared to remove authentication data.

---

## ✅ Common `AuthenticationProvider` Types in Spring Security

Spring Security provides several ways to authenticate users.  
Here’s a quick overview of the most common `AuthenticationProvider` types:

---

### 🔐 1. DaoAuthenticationProvider

- ✅ **Most commonly used.**
- 🔄 Delegates to a `UserDetailsService` to load user info from a database.
- 🔑 Compares passwords using a `PasswordEncoder`.

📌 **Use case:** Standard form login with database-stored users.

---

### 🧾 2. LdapAuthenticationProvider

- 🌐 Authenticates users against an **LDAP** (Lightweight Directory Access Protocol) server.
- 🧰 Uses `LdapUserDetailsService` internally.

📌 **Use case:** Enterprise apps using centralized directory services like **Active Directory**.

---

### 🪪 3. JwtAuthenticationProvider *(Custom)*

- 🧪 Validates **JWT tokens** in stateless authentication.
- 🛠️ You **implement this yourself**.

📌 **Use case:** REST APIs with **token-based security** (no sessions).

---

### 🌍 4. OAuth2LoginAuthenticationProvider

- 🔗 Used for **OAuth2 login flows** (Google, Facebook, GitHub, etc.).
- 🤝 Works with `OAuth2UserService`.

📌 **Use case:** Social login or **Single Sign-On (SSO)**.

---

### ⚠️ 5. OpenIDAuthenticationProvider *(Deprecated)*

- 📤 Used for **OpenID authentication**.
- 🕰️ Older standard, mostly replaced by **OAuth2/OpenID Connect**.

---

### 🧩 6. Custom AuthenticationProvider

- ✏️ You can create your own by implementing `AuthenticationProvider`.
- 🧬 Example: biometric login, OTP verification, API key auth, etc.

📌 **Use case:** When built-in providers don’t cover your authentication needs.

---

> 📘 **Tip:** Most real-world applications combine multiple providers to meet different authentication needs.

