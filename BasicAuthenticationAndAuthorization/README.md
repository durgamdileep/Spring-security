# Types of Authorization in Spring Security

---

## 1. URL-Based Authorization (Request-Based Authorization) 🔗

- Controls access based on the requested URL or HTTP request.
- Configured via `HttpSecurity` in your security config.
- Most common and simplest form.

**Example:**
  ``` java
        http
       .authorizeHttpRequests()
       .requestMatchers("/admin/**").hasRole("ADMIN")
       .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
       .anyRequest().authenticated();
  ```

- `Access decisions are made before the request reaches your controllers`.
- Works well for coarse-grained control (e.g., which users can access which endpoints).

### Common Methods for URL-based Authorization

1. #### `authorizeHttpRequests(...)`
  - 🔑 `Entry point` for `configuring authorization rules` for HTTP requests.
  - 🕒 **When to use:**
    - Always use this to define fine-grained access control over endpoints.

2. #### `requestMatchers(...)`
  - 🎯 Defines the `HTTP request matcher` for which the following security rules will apply.
  - 🕒 **When to use:**
    - Use when you want to restrict access to certain URL patterns.
  - 💡 **Why to use:**
    - It allows you to `combine multiple rules` with different matchers.
    - It is flexible and readable.

3. #### `anyRequest()`
  - 🔄 Matches any `HTTP request not matched` by a `previous requestMatchers()`.
  - 🕒 **When to use:**
    - To define a `default rule for all unmatched` requests.
  - 💡 **Why use it:**
    - Acts as a `catch-all fallback`, making your authorization policy complete and predictable.

4. #### `permitAll()`
  - 🟢 Allows `unrestricted access` to the matched request.
  - ``` java
       .requestMatchers("/public/**").permitAll()
     ```
  - 🕒 **When to use:**
    - For `public endpoints` like home, login, registration, health checks, etc.
  - 💡 **Why use it:**
    - Ensures that endpoints `don't require authentication`.

5. #### `denyAll()`
  - ⛔ `Denies access` to everyone for the matched request.
  - 🕒 **When to use:**
    - When you want to `temporarily or permanently block access` to an endpoint.
  - 💡 **Why use it:**
    - Useful for `deprecated or disabled` endpoints.

6. #### `authenticated()`
  - 🔐 Requires the `user to be logged` in but `doesn’t restrict by roles`.
  - ``` java
        .anyRequest().authenticated()
     ```
  - 🕒 **When to use:**
    - For `endpoints that require login` but `no specific authority`.
  - 💡 **Why use it:**
    - `Prevents anonymous access` without enforcing roles.

7. #### `hasRole(String role)` and `hasAuthority(String authority)`
  - 🎟️ Restrict access to users with a specific role or authority.
  - ``` java
       .requestMatchers("/admin/**").hasRole("ADMIN")
       .requestMatchers("/api/**").hasAuthority("SCOPE_read")
     ```
  - 🕒 **When to use:**
    - Use `hasRole()` when you use `simple roles`.
    - Use `hasAuthority()` when working with `more granular permissions` (e.g., OAuth scopes).
  - 💡 **Why use:**
    - Provides fine-grained access control.
    - `hasRole("ADMIN")` maps to authority `"ROLE_ADMIN"` behind the scenes.

8. #### `hasAnyRole(...)` and `hasAnyAuthority(...)`
  - 🔓 Allow access if the `user has any one` of the specified roles or authorities.
  - ``` java
       .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
     ```
  - 🕒 **When to use:**
    - When `multiple roles` should have access to the `same endpoint`.
  - 💡 **Why use:**
    - `Reduces duplication` of security rules.
---
Types of Fine-Grained Permissions with hasAuthority()
1. Action-based authorities

These express what a user can do, not just who they are
``` java 
    hasAuthority("USER_READ")
    hasAuthority("USER_WRITE")
    hasAuthority("PRODUCT_DELETE")
```
Use case:
Control access to specific operations, like viewing, editing, or deleting resources.
2. Domain-specific permissions

Scoped to parts of the application:
``` java 
   hasAuthority("INVENTORY_VIEW")
   hasAuthority("SALES_REPORT_GENERATE")
   hasAuthority("HR_EMPLOYEE_MANAGE")
``` 
Use case:

Useful in modular applications where different parts (HR, Sales, etc.) need different rights
3. OAuth2 / JWT Scopes

In OAuth2/JWT-based security, the scope claim in the token maps to authorities like:
``` java 
    hasAuthority("SCOPE_profile")
    hasAuthority("SCOPE_read")
    hasAuthority("SCOPE_write")
```
Use case:

Used in REST APIs secured by OAuth2, where scopes define API access levels.

4. Granular system-level privileges

Example for a more fine-tuned backend permission model:
``` java 
  hasAuthority("CAN_APPROVE_PAYMENTS")
hasAuthority("CAN_EXPORT_DATA")
hasAuthority("ACCESS_DASHBOARD_ADMIN_VIEW")

```
Use case:

When you're building a platform with very specific permission logic, like financial apps or dashboards.


---


### 2. Method-Based Authorization 🛠️

- Controls access at the method level using annotations.
- Allows fine-grained access control inside your services or controllers.
- Common annotations:
    * `@PreAuthorize` 🔐
    * `@PostAuthorize` 🔒
    * `@Secured` 🛡️
    * `@RolesAllowed` 👥

**Example:**
   ``` java
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long userId) {
     // Only accessible by ADMIN role
    }

   ```

- Authorization logic can use complex expressions, e.g., check ownership or custom conditions.
- You need to enable it with `@EnableMethodSecurity`.

---

### 3. Domain Object or Business-Level Authorization (ACL or Custom Permission Evaluator) 📦

- Controls access based on business rules or permissions on specific domain objects.
- Spring Security supports ACL (Access Control List) for per-entity authorization.
- You can create custom `PermissionEvaluator`s for complex checks.

#### Use Cases:
- User can edit only their own posts. ✍️
- User can view order details only if they belong to the same department. 🏢

---

### 4. Role-Based vs Authority-Based Authorization 👥 vs 🔑

- Role-Based authorization is basically a special case of authorities.
- Roles are typically prefixed with `ROLE_`.
- Authorities can represent any granular permission (e.g., `READ_PRIVILEGE`, `WRITE_PRIVILEGE`).

| Method                        | What Spring Checks                                    |
|-------------------------------|------------------------------------------------------|
| `.hasRole("ADMIN")`           | Checks if `ROLE_ADMIN` exists in authorities (adds prefix `ROLE_`) |
| `.hasAuthority("ROLE_ADMIN")` | Checks exactly `ROLE_ADMIN` (no prefix added)        |

---

### 5. Custom Authorization with Voters and Decision Managers 🗳️

- Spring Security uses `AccessDecisionManager` and `AccessDecisionVoters` under the hood.
- You can create custom voters to define your own authorization logic.
- Useful in complex scenarios where multiple factors influence the decision.

---
