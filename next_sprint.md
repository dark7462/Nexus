# Next Sprint — Feature Backlog

Features planned for the next development cycle, sourced from the project roadmap.  
Pick one, say go, and we implement it.

---

## 1. 🔌 Real-Time WebSockets for Chat

**Problem:** Chat currently works via REST polling — clients have to repeatedly call `GET /api/message/chat/{chatId}` to check for new messages. This is inefficient and not truly real-time.

**Solution:** Integrate **Spring WebSockets with STOMP** protocol so messages are pushed instantly to connected clients.

**What needs to be built:**

- Add `spring-boot-starter-websocket` dependency to `pom.xml`
- Create `WebSocketConfig.java` — configure STOMP endpoint and message broker
- Create `ChatWebSocketController.java` — handle `@MessageMapping` routes
- Emit new messages to subscribed clients via `SimpMessagingTemplate`
- Frontend subscribes to `/topic/chat/{chatId}` to receive live messages

**Impact:** Eliminates polling entirely. Messages appear instantly for all participants in a chat.

---

## 2. ☁️ File Uploads to Cloud Storage

**Problem:** `imageURL` and `videoURL` on `Post`, `Reel`, and `Message` are plain text strings. There is no actual file upload mechanism — users have to manually supply hosted URLs.

**Solution:** Accept **Multipart file uploads** via the API. Store files on **AWS S3** or **Cloudinary**, then save the returned hosted URL into the database.

**What needs to be built:**

- Add AWS S3 SDK (`software.amazon.awssdk:s3`) or Cloudinary SDK to `pom.xml`
- Create `FileUploadService.java` — handles upload and returns hosted URL
- Add `POST /api/upload` endpoint (or integrate as part of post/reel creation)
- Update `CreatePostRequest`, `CreateReelRequest`, `CreateMessageRequest` to accept `MultipartFile`
- Store the returned CDN URL in DB instead of user-provided string

**Impact:** Makes media uploads first-class. Removes dependency on users self-hosting their media.

---

## 3. 🛡️ Role-Based Access Control (RBAC)

**Problem:** All authenticated users have the same level of access. There's no way to restrict sensitive operations (like deleting any post or managing users) to admins only.

**Solution:** Introduce a `Role` enum on the `User` model and use Spring Security's `@PreAuthorize` to gate endpoint access by role.

**What needs to be built:**

- Add `Role` enum: `USER`, `ADMIN`
- Add `role` field to `User` entity (default: `USER`)
- Update `CustomerUserDetailService` to include role in `GrantedAuthority`
- Annotate sensitive endpoints with `@PreAuthorize("hasRole('ADMIN')")`
- Update `SignUpRequest` and `AuthServiceImpl` to assign default role on registration

**Impact:** Enables admin-only actions like global post deletion, user banning, etc.

---

## 4. 📖 Swagger / OpenAPI Documentation

**Problem:** The API has no interactive documentation. Frontend developers or testers have to read source code to understand available endpoints, request bodies, and response shapes.

**Solution:** Integrate `springdoc-openapi` to auto-generate live interactive API docs at `/swagger-ui.html`.

**What needs to be built:**

- Add `springdoc-openapi-starter-webmvc-ui` to `pom.xml`
- Optionally annotate controllers with `@Tag`, `@Operation`, `@ApiResponse` for richer docs
- Whitelist `/swagger-ui/**` and `/v3/api-docs/**` in `Appconfig.java` security config (permit without auth)

**Impact:** Every endpoint, request body, and response DTO becomes browsable and testable from a UI. Zero extra effort for API consumers.
