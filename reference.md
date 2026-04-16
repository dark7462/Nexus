# SpringSocialMediaApp — Reference

> **Purpose**: Single source of truth. Before touching any file, read this first.
> **Rule**: Every time a meaningful change is made, update this file under the Changelog section.

---

## Stack

- **Framework**: Spring Boot 4.0.5
- **Language**: Java 17
- **Database**: PostgreSQL (`spcialmediaapp` db, user: `dark`, password: `password`, port: `5432`)
- **ORM**: Spring Data JPA / Hibernate (`ddl-auto=update`)
- **Security**: Spring Security (Stateless JWT) + BCrypt
- **Auth Token**: JJWT 0.12.5 — HS256, issuer: `Dark`, expiry: ~27 hours
- **Lombok**: Full usage across models and DTOs
- **Validation**: `spring-boot-starter-validation` — `@Valid` enforced at controller level
- **Cache**: `spring.cache.type=simple` (in-memory)
- **CORS**: Allows `localhost:3000` and `localhost:5173`

---

## Package Structure

```
com.dark
├── configuration/       # Security, JWT, CORS, Cache
├── controller/          # REST Controllers (7 controllers)
├── Exceptions/          # Custom exceptions + GlobalException handler
├── mapper/              # DtoMapper (entity → DTO conversions)
├── model/               # JPA Entities
├── repository/          # Spring Data JPA Repositories
├── request/             # Validated request bodies (inbound DTOs)
├── response/            # Response DTOs (outbound)
└── service/             # Service interfaces + implementations
```

---

## Models (JPA Entities)

### `User` → table: `users`
| Field | Type | Notes |
|---|---|---|
| `userId` | Integer (PK) | AUTO |
| `firstName` | String | stored as toLowerCase |
| `lastName` | String | stored as toLowerCase |
| `gender` | String | stored as toLowerCase |
| `email` | String | unique, stored as toLowerCase |
| `password` | String | BCrypt hashed, WRITE_ONLY in JSON |
| `followers` | Set\<Integer\> | ElementCollection of user IDs |
| `following` | Set\<Integer\> | ElementCollection of user IDs |
| `savedPosts` | Set\<Post\> | ManyToMany, @JsonIgnore |

> Custom `getId()` returns `userId`. Custom setters enforce `toLowerCase()`.

### `Post`
| Field | Type | Notes |
|---|---|---|
| `postId` | Integer (PK) | AUTO |
| `caption` | String | |
| `imageURL` | String | text URL |
| `videoURL` | String | text URL |
| `user` | User | ManyToOne LAZY |
| `likedUsers` | Set\<User\> | ManyToMany |
| `comments` | List\<Comment\> | OneToMany, CascadeAll, orphanRemoval |
| `createdAt` | LocalDateTime | default: now() |

### `Comment`
| Field | Type | Notes |
|---|---|---|
| `commentId` | Integer (PK) | AUTO |
| `content` | String | |
| `user` | User | ManyToOne LAZY |
| `post` | Post | ManyToOne LAZY, @JsonIgnore, joined on `postId` |
| `liked` | Set\<User\> | ManyToMany |
| `createdAt` | LocalDateTime | default: now() |

### `Chat`
| Field | Type | Notes |
|---|---|---|
| `id` | Integer (PK) | AUTO |
| `chatName` | String | |
| `chatImage` | String | |
| `users` | List\<User\> | ManyToMany |
| `messages` | List\<Message\> | OneToMany |
| `timeStamp` | LocalDateTime | |

### `Message`
| Field | Type | Notes |
|---|---|---|
| `id` | Integer (PK) | AUTO |
| `content` | String | |
| `image` | String | |
| `timeStamp` | LocalDateTime | |
| `user` | User | ManyToOne |
| `chat` | Chat | ManyToOne, @JsonIgnore |

### `Reels`
| Field | Type | Notes |
|---|---|---|
| `id` | Integer (PK) | AUTO |
| `title` | String | |
| `video` | String | text URL |
| `user` | User | ManyToOne, joined on `userId` |
| `createdAt` | LocalDateTime | default: now() |

---

## API Endpoints

All `/api/**` routes require `Authorization: Bearer <jwt>` header.
Auth routes (`/auth/**`) are public.

### Auth — `/auth`
| Method | URL | Body | Returns |
|---|---|---|---|
| POST | `/auth/signup` | `SignUpRequest` | `AuthResponse` (201) |
| POST | `/auth/signin` | `SignInRequest` | `AuthResponse` (200) |

### User — `/api`
| Method | URL | Returns |
|---|---|---|
| GET | `/api/users` | `Page<UserDto>` (pageable, sort: firstName) |
| GET | `/api/user/{id}` | `UserDto` |
| PUT | `/api/user` | `UserDto` (body: User entity) |
| PUT | `/api/users/follow/{userid2}` | `UserDto` |
| DELETE | `/api/users/unfollow/{userid2}` | `UserDto` |
| GET | `/api/user/{userid}/followers/count` | String |
| GET | `/api/user/{userid}/following/count` | String |
| GET | `/api/user/search?query=` | `Page<UserDto>` |
| GET | `/api/users/profile` | `UserDto` (from JWT) |

### Post — `/api`
| Method | URL | Body | Returns |
|---|---|---|---|
| POST | `/api/post` | `CreatePostRequest` | `PostDto` (201) |
| DELETE | `/api/post/{postId}` | — | `ApiResponse` |
| GET | `/api/posts` | — | `List<PostDto>` (own posts from JWT) |
| GET | `/api/allposts` | — | `Page<PostDto>` (sort: createdAt DESC) |
| PUT | `/api/post/savepost/{postId}` | — | `PostDto` |
| PUT | `/api/post/likepost/{postId}` | — | `PostDto` |

### Comment — `/api`
| Method | URL | Body | Returns |
|---|---|---|---|
| POST | `/api/commnet/create/{postId}` | `CreateCommentRequest` | `CommentDto` (201) |
| POST | `/api/like/{commentId}` | — | `CommentDto` |
| GET | `/api/comment/{commentId}` | — | `CommentDto` |
| GET | `/api/post/{postId}` | — | `List<CommentDto>` |
| DELETE | `/api/comment/{commentId}/{postId}` | — | `ApiResponse` |

> Note: `createComment` URL has a typo — `/commnet/` (as-is in code).

### Chat — `/api/chats`
| Method | URL | Body | Returns |
|---|---|---|---|
| POST | `/api/chats/create` | `CreatChatRequest` | `ChatDto` (201) |
| GET | `/api/chats/{chatId}` | — | `ChatDto` |
| GET | `/api/chats/user/{userId}` | — | `List<ChatDto>` (from JWT) |

### Message — `/api/message/`
| Method | URL | Body | Returns |
|---|---|---|---|
| POST | `/api/message/create/{chatId}` | `CreateMessageRequest` | `MessageDto` (201) |
| GET | `/api/message/chat/{chatId}` | — | `List<MessageDto>` |

### Reel — `/api`
| Method | URL | Body | Returns |
|---|---|---|---|
| POST | `/api/reel` | `CreateReelRequest` | `ReelDto` (201) |
| GET | `/api/reels` | — | `Page<ReelDto>` (sort: createdAt DESC) |
| GET | `/api/reels/{userId}` | — | `List<ReelDto>` |

---

## Request DTOs (Inbound, all validated)

| Class | Fields | Validations |
|---|---|---|
| `SignUpRequest` | firstName, lastName, gender, email, password | firstName @NotBlank @Size(2-50), gender @NotBlank, email @Email, password @Size(min 6) |
| `SignInRequest` | email, password | — |
| `CreatePostRequest` | caption, imageURL, videoURL | caption @NotBlank |
| `CreateCommentRequest` | content | @NotBlank |
| `CreateMessageRequest` | content, image | content @NotBlank |
| `CreateReelRequest` | title, video | both @NotBlank |
| `CreatChatRequest` | reciverId (Integer) | — |

---

## Response DTOs (Outbound)

| Class | Fields |
|---|---|
| `UserDto` | id, firstName, lastName, email, gender, followers, following |
| `PostDto` | postId, caption, imageURL, videoURL, user (UserDto), likedUsers, comments, createdAt |
| `CommentDto` | commentId, content, user (UserDto), liked (List\<UserDto\>), createdAt |
| `ChatDto` | id, chatName, chatImage, users (List\<UserDto\>), timestamp |
| `MessageDto` | id, content, image, chat (ChatDto), user (UserDto), timestamp |
| `ReelDto` | id, title, video, user (UserDto) |
| `AuthResponse` | token, message |
| `ApiResponse` | message, status (boolean) |

All conversions done by `DtoMapper` (static utility class in `com.dark.mapper`).

---

## Security & JWT

- **Filter**: `jwtValidator` runs before `BasicAuthenticationFilter`
- **JWT generation**: `JwtProvider.generateToken(Authentication)` — stores `email` claim
- **JWT parsing**: `JwtProvider.getEmailFromJwtToken(String jwt)` — strips `Bearer ` prefix (first 7 chars)
- **Secret key**: in `jwtConstant.SECRET_KEY`
- **Public routes**: anything not under `/api/**`
- **Protected routes**: all `/api/**`

---

## Exception Handling

`GlobalException.java` (@ControllerAdvice) handles:
- `UserException` → 404
- `PostException` → 404
- `CommentException` → 404
- `ChatException` → 404
- `MessageException` → 404
- `ReelException` → 404
- `MethodArgumentNotValidException` → 400 (with field-level error messages)
- `Exception` (fallback) → 500

Error body: `ErrorDetails { message, description, timestamp }`

---

## Repositories (Custom Queries)

- `UserRepository`: `findByEmail`, `searchUser` (by name/email, List + Page)  
- `PostRepository`: `findAllPostByUserId`  
- `ChatRepository`: `findByUsersId`, `findChatByUsersId` (by sender+receiver)  
- `MessageRepository`: `findByChatId`  
- `ReelRepository`: `findByUserId`  
- `CommentRepository`: no custom queries  

---

## Known Issues / Notes

- `PostContoller.java` has a typo in filename (missing 'r' — `PostContoller` not `PostController`).
- Comment create URL has a typo: `/api/commnet/` instead of `/api/comment/`.
- `imageURL` and `videoURL` on Post/Reel are plain text strings, not actual file storage.
- `User.updateUser()` in `UserServiceImplementation` does not update `password` — intentional.
- Followers/following stored as `Set<Integer>` (user IDs), not entity relations.

---

## Roadmap (Not Yet Built)

1. **WebSockets for Real-Time Chat** — STOMP over WebSockets
2. **File Uploads** — Multipart uploads to AWS S3 or Cloudinary (replacing string URLs)
3. **Role-Based Access Control (RBAC)** — `ADMIN`/`USER` enum + `@PreAuthorize`
4. **Swagger/OpenAPI Docs** — `springdoc-openapi-starter-webmvc-ui`

---

## Changelog

| Date | Change |
|---|---|
| 2026-04-05 | Initial project setup — Spring Boot, PostgreSQL, JWT auth |
| 2026-04-05 | Implemented all 6 domain models (User, Post, Comment, Chat, Message, Reels) |
| 2026-04-05 | Built 7 REST controllers with basic CRUD endpoints |
| 2026-04-07 | Fixed `ChatRepository.findByUsersId` query method |
| 2026-04-08 | Migrated DB from MySQL → PostgreSQL; renamed table to `users` to avoid reserved keyword |
| 2026-04-10 | Lombok refactoring — removed all boilerplate getters/setters across all models |
| 2026-04-10 | Decoupled auth logic — extracted `AuthService` + `AuthServiceImpl` from `AuthController` |
| 2026-04-10 | Added custom domain exceptions (`UserException`, `PostException`, etc.) |
| 2026-04-10 | `GlobalException` handler — domain exceptions → 404, validation → 400, fallback → 500 |
| 2026-04-10 | Standardized all controllers to return `ResponseEntity` with correct HTTP status codes |
| 2026-04-12 | Spring Boot version bumped: `4.0.3` → `4.0.5` |
| 2026-04-14 | DTO pattern implemented — `Request` DTOs for inbound, `Response` DTOs for outbound |
| 2026-04-14 | `DtoMapper` created — all entity → DTO conversions centralized |
| 2026-04-14 | All 7 controllers updated — no raw JPA entities exposed, `@Valid` enforced on all request bodies |
| 2026-04-14 | `AuthService.signUp` updated to accept `SignUpRequest` instead of raw `User` entity |
| 2026-04-16 | `reference.md` created — single source of truth for entire project |
