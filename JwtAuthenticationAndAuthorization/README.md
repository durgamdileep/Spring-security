# 🔐 JWT Authentication and Authorization Flow

## 👤 User Sends Login Request

The user submits their username and password to a specific endpoint, typically `/login` or `/authenticate`.

---

## ⚙️ AuthenticationManager Receives Request

Spring Security wraps the credentials in an `Authentication` object (usually `UsernamePasswordAuthenticationToken`).

This object is passed to the `AuthenticationManager`.

---

## 🔄 AuthenticationManager Delegates to AuthenticationProvider

The `AuthenticationManager` forwards the authentication request to a suitable `AuthenticationProvider`, usually `DaoAuthenticationProvider`.

---

## 📋 AuthenticationProvider Loads User Details

The provider calls `UserDetailsService.loadUserByUsername(username)` to retrieve user data from the database.

A `UserDetails` object is returned with the user's:

- Username
- Hashed password
- Roles/authorities

---

## 🔑 AuthenticationProvider Validates Credentials

The entered password is verified using a `PasswordEncoder`.

If it matches the stored hashed password:  
✅ Authentication succeeds  
❌ Otherwise, an exception is thrown.

---

## 🧾 JWT is Generated and Returned

On successful authentication:

A JWT (JSON Web Token) is generated, typically using:

- User's username
- Roles/authorities
- Expiration timestamp
- Signature (using a secret key or private key)

The JWT is sent back to the client in the HTTP response body or Authorization header.

---

## 🧳 Client Stores JWT

The client stores the JWT (usually in `localStorage` or `sessionStorage`).

It includes this token in the `Authorization` header of every subsequent HTTP request.

``` java
   Authorization: Bearer <JWT>
```

## 🚪 User Accesses Secured Endpoints

The user makes a request to a protected endpoint, attaching the JWT.

---

## 🕵️ JWT Authentication Filter Kicks In

A custom `JwtAuthenticationFilter` (usually extending `OncePerRequestFilter`) intercepts the request.

It extracts the JWT from the `Authorization` header.

---

## 🔍 Validate JWT

The filter:

- Validates the token's signature
- Checks expiration
- Extracts username and roles from token claims

---

## 🧾 Load User Details (Optional)

(Optional, depending on implementation) The filter may call `UserDetailsService.loadUserByUsername()` to:

- Ensure user still exists
- Load updated roles/authorities

---

## 🔐 Build Authentication Object

If the JWT is valid:

- A new authenticated `UsernamePasswordAuthenticationToken` is created
- It includes user details, roles, and `isAuthenticated = true`

---

## 📥 Store Authentication in SecurityContext

The authentication object is set in the `SecurityContext` via `SecurityContextHolder.setContext(...)`

Now the user is considered authenticated for the current request.

---

## ✅ Authorization Decision

➡️ `FilterSecurityInterceptor` — the last filter in the chain — reads the Authentication object from the SecurityContextHolder,  
and checks if the user is allowed to access the requested endpoint.  
Spring Security uses the `SecurityContext` to authorize access based on roles/authorities.

- ✅ If allowed → request proceeds
- ❌ If not → 403 Forbidden or 401 Unauthorized

---

## 🔄 Stateless Nature of JWT

No session is stored on the server.

Each request is authenticated independently using the JWT.

---

## 🚪 Logout (Client-Side)

Since the server is stateless:

- Logout is handled on the client by simply deleting the JWT.
- Optionally, the token can be blacklisted on the server (e.g., using a token store or Redis) for enhanced security.

---
 
--- 

### 🔎 Why it's a problem:
   - In microservices, secured endpoints are hit frequently. 
   - If each request hits the database to load UserDetails, it adds:
        - 🔴 Latency 
        - 🔴 Increased DB load 
        - 🔴 Scalability issues

#### ✅ Optimal approach:
   - Use a cache (e.g. Redis, Caffeine, Ehcache) to store UserDetails:
        - After first load from DB, cache it. 
        - On next request, load from cache.
   - Keep JWT stateless (don't store JWT itself on the server),
        - But cache only user data used for validation (like roles).\
   - Token contains necessary info:
        - You can even embed roles/authorities in JWT claims to avoid DB/userDetails lookup, 
        - But that requires trust in token and proper signature validation.
   - Short-lived tokens + refresh flow 
        - Keep tokens fresh and updated frequently.
---

## ✅🔒 Approach 1: Validate JWT and hit the DB every request

- 🛂 You validate the JWT and then load user data from the DB (e.g., roles, permissions) every time a request comes in.

### ✔️ Correctness:
✅ This is correct and secure but less efficient due to frequent DB access.

You typically:
- ✅ Verify the JWT signature.
- 🧾 Parse the claims (like username).
- 🗂️ Load user details (roles, etc.) from the database on each request.

#### 💡 Use case:
When you need real-time updates of user data (e.g., if roles or account status can change frequently).

---

## 🔐✅ Approach 2: Validate JWT without DB hit by using claims

What you're saying:  
You embed data like username and roles in the JWT claims and avoid database hits by trusting the token's signature.

### ✔️ Correctness:
✅ This is correct and efficient, assuming:

- 🔏 The token is signed and tamper-proof (with a secret or private key).
- 🙆‍♂️ You’re okay with not revoking tokens easily.
- 📦 User data in the token is not stale.

### 💡 Use case:
Good for stateless APIs and microservices where performance matters more than immediate user state.

### ⚠️ Caution/ Problem:
If the user's role or account status changes, that won’t reflect until the token expires or is reissued.

---

## 🔐✅ Approach 3: Hybrid JWT Authentication with Cache & Optional Revocation

### What you're saying:

You validate the JWT (signature + expiration), extract the username or user ID from the claims, and then partially rely on a lightweight user lookup (from cache or DB) to ensure the user's current status and roles are valid. Optionally, you maintain a token revocation store (e.g., in Redis) for logout or banning.

### ✔️ Correctness:

✅ This is correct, scalable, and balances performance with security, assuming:

- 🔏 The token is signed and tamper-proof.
- 🔄 You validate the token on every request (signature, expiry).
- ⚡ User info is fetched from cache (e.g., `@Cacheable`) to avoid hitting the DB frequently.
- 🧠 You can optionally use a token blacklist to handle revocations (logout, banned users).
- 🔍 Token claims are trusted only for basic identity — roles/status are still verified live.

### 💡 Use case:

Perfect for APIs that:

- 🚀 Need high performance and low DB load
- ❌ Still require revoking tokens (logout, admin ban)
- 🔄 Must reflect user updates (roles, enabled/disabled) during token lifetime
- 🧊 Prefer caching over full DB dependency

### ⚠️ Caution / Problem:

- 🚫 Still requires minimal server-side state (user cache or blacklist store).
- 🧓 Cache may serve slightly stale user data if not synced or invalidated properly.
- 🔁 Requires extra logic for maintaining cache and revocation list.
- 🧰 Adds complexity compared to pure stateless JWT (Approach 2).


# 🔄 Final Flow: Hybrid JWT + Revocation

### ✅ Step-by-step:

### 🧾 1. Client sends a JWT

- JWT is included in the `Authorization: Bearer <token>` header.

### 🔐 2. Backend validates the token

- ✅ Check if the token is signed correctly (HMAC or RSA).
- ⏰ Check if the token is not expired (`exp` claim).
- ❌ If either fails, reject the request with `401 Unauthorized`.

### 📤 3. Extract claims

- 🧑 Get the username (or user ID) from the `sub` claim.
- 🆔 Get the `jti` (JWT ID) — optional, but needed for revocation.
  
  - #### 🔑 Where does jti come from?
       - jti is a standard JWT claim that stands for JWT ID — a unique identifier for the token
         ``` java
           String token = Jwts.builder()
                             .setSubject(username)
                             .setId(UUID.randomUUID().toString()) // 👈 jti
                             .setIssuedAt(now)
                             .setExpiration(exp)
                             .signWith(secretKey)
                             .compact();
         ```

### ⚡ 4. Load user details

- 🗂️ Use `UserDetailsService` to fetch user  
  (e.g., from DB or cache via `@Cacheable`).


### 🔍 5. Perform checks

- 🔓 `userDetails.isEnabled()` — is the user active?
- 🚫 `tokenBlacklistService.isTokenRevoked(jti)` — was the token revoked (e.g., after logout or ban)?
    ``` java
       @Service
       public class TokenBlacklistService {
     
         private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();
     
         public void revokeToken(String jti) {
             revokedTokens.add(jti);
         }
     
         public boolean isTokenRevoked(String jti) {
             return revokedTokens.contains(jti);
         }
       }
    ```
    - or use redis storage
    ``` java
       @Service
       public class TokenBlacklistService {

       @Autowired
       private RedisTemplate<String, String> redisTemplate;

       private static final String PREFIX = "revoked:";

        public void revokeToken(String jti, long expirationSeconds) {
            redisTemplate.opsForValue().set(PREFIX + jti, "revoked", expirationSeconds, TimeUnit.SECONDS);
        }

        public boolean isTokenRevoked(String jti) {
           return redisTemplate.hasKey(PREFIX + jti);
        }
       }
    ```



### ✅ 6. If all checks pass

- 🛡️ Create an `Authentication` object (e.g., `UsernamePasswordAuthenticationToken`).
- 🧠 Set it in the `SecurityContextHolder`.
- 📥 Proceed with the request.

---