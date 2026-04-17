# Next Sprint — Feature Backlog

Features planned for the next development cycle, sourced from the project roadmap.  
Pick one, say go, and we implement it.

---

## 1. ☁️ File Uploads to Cloud Storage

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

## 2. 🛡️ Role-Based Access Control (RBAC)

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

