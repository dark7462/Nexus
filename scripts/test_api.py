import urllib.request
import json
import uuid
import sys
import time

BASE_URL = "http://localhost:8080"

# Colors for terminal output
GREEN = '\033[92m'
RED = '\033[91m'
YELLOW = '\033[93m'
RESET = '\033[0m'

def print_header(title):
    print(f"\n{YELLOW}{'=' * 50}")
    print(f" 🧪 {title}")
    print(f"{'=' * 50}{RESET}")

def print_result(success, endpoint, status, message="", body=None):
    if success:
        print(f"{GREEN}[PASS]{RESET} {endpoint} (HTTP {status}) {message}")
    else:
        err_detail = body if body else ""
        print(f"{RED}[FAIL]{RESET} {endpoint} (HTTP {status}) {message} | Err: {err_detail}")

class APIClient:
    def __init__(self):
        self.headers = {"Content-Type": "application/json"}
        self.token = None

    def set_token(self, token):
        self.token = token
        self.headers["Authorization"] = f"Bearer {token}"

    def make_request(self, method, path, body=None):
        url = BASE_URL + path
        data = json.dumps(body).encode('utf-8') if body else None
        req = urllib.request.Request(url, data=data, headers=self.headers, method=method)
        try:
            with urllib.request.urlopen(req) as response:
                return response.status, json.loads(response.read().decode('utf-8'))
        except urllib.error.HTTPError as e:
            try:
                body_error = json.loads(e.read().decode('utf-8'))
            except:
                body_error = str(e)
            return e.code, body_error
        except Exception as e:
            return 0, str(e)

def run_tests():
    print(f"Starting API Test Suite targeting {BASE_URL}...\n")
    client1 = APIClient()
    client2 = APIClient()

    # ---------------------------------------------------------
    print_header("Authentication Module")
    # ---------------------------------------------------------
    uid1 = str(uuid.uuid4())[:8]
    uid2 = str(uuid.uuid4())[:8]
    
    # 1. Signup User 1
    status, body = client1.make_request("POST", "/auth/signup", {
        "firstName": "John", "lastName": "Doe", "gender": "male", 
        "email": f"johndoe_{uid1}@test.com", "password": "password"
    })
    success = status == 201
    print_result(success, "POST /auth/signup (User 1)", status, body=body)
    if not success: sys.exit(1)
    
    # Login & get token
    status, body = client1.make_request("POST", "/auth/signin", {
        "email": f"johndoe_{uid1}@test.com", "password": "password"
    })
    success = status == 200 and "token" in body
    client1.set_token(body.get("token"))
    print_result(success, "POST /auth/signin (User 1)", status, body=body)

    # 2. Signup User 2
    status, body = client2.make_request("POST", "/auth/signup", {
        "firstName": "Jane", "lastName": "Smith", "gender": "female", 
        "email": f"janesmith_{uid2}@test.com", "password": "password"
    })
    success = status == 201
    print_result(success, "POST /auth/signup (User 2)", status, body=body)
    
    status, body = client2.make_request("POST", "/auth/signin", {
        "email": f"janesmith_{uid2}@test.com", "password": "password"
    })
    client2.set_token(body.get("token"))

    # ---------------------------------------------------------
    print_header("User Module")
    # ---------------------------------------------------------
    
    # 3. Get Profile User 2
    status, body = client2.make_request("GET", "/api/users/profile")
    success = status == 200 and "email" in body
    user2_id = body.get("id")
    print_result(success, "GET /api/users/profile", status, f"Got ID: {user2_id}", body=body)

    # 4. Search User
    status, body = client1.make_request("GET", f"/api/user/search?query=jane")
    success = status == 200 and len(body.get("content", [])) > 0
    print_result(success, "GET /api/user/search?query=jane", status, body=body)

    # 5. Follow User
    status, body = client1.make_request("PUT", f"/api/users/follow/{user2_id}")
    success = status in [200, 202]
    print_result(success, f"PUT /api/users/follow/{user2_id}", status, body=body)

    # ---------------------------------------------------------
    print_header("Post Module")
    # ---------------------------------------------------------

    # 6. Create Post
    status, body = client1.make_request("POST", "/api/post", {
        "caption": "Hello world from test script!", "imageURL": "http://example.com/img.png"
    })
    success = status == 201
    post_id = body.get("postId")
    print_result(success, "POST /api/post", status, f"Created Post ID: {post_id}", body=body)
    
    # 7. Get All Posts (Global Feed with Pagination)
    status, body = client2.make_request("GET", "/api/allposts")
    success = status == 200 and "content" in body
    print_result(success, "GET /api/allposts", status, f"Total Elements: {body.get('totalElements')}", body=body)

    # 8. Like Post
    status, body = client2.make_request("PUT", f"/api/post/likepost/{post_id}")
    success = status in [200, 202]
    print_result(success, f"PUT /api/post/likepost/{post_id}", status, body=body)

    # 9. Save Post
    status, body = client2.make_request("PUT", f"/api/post/savepost/{post_id}")
    success = status in [200, 202]
    print_result(success, f"PUT /api/post/savepost/{post_id}", status, body=body)

    # ---------------------------------------------------------
    print_header("Comment Module")
    # ---------------------------------------------------------

    # 10. Create Comment
    status, body = client2.make_request("POST", f"/api/commnet/create/{post_id}", {
        "content": "This is a test comment!"
    })
    success = status == 201
    comment_id = body.get("commentId")
    print_result(success, f"POST /api/commnet/create/{post_id}", status, f"Comment ID: {comment_id}", body=body)

    # 11. Like Comment
    status, body = client1.make_request("POST", f"/api/like/{comment_id}")
    success = status in [200, 202]
    print_result(success, f"POST /api/like/{comment_id}", status, body=body)

    # 12. Get Comments for Post
    status, body = client1.make_request("GET", f"/api/post/{post_id}")
    success = status == 200 and isinstance(body, list) and len(body) > 0
    print_result(success, f"GET /api/post/{post_id}", status, body=body)

    # ---------------------------------------------------------
    print_header("Reel Module")
    # ---------------------------------------------------------

    # 13. Create Reel
    status, body = client1.make_request("POST", "/api/reel", {
        "title": "My first test reel", "video": "http://example.com/vid.mp4"
    })
    success = status == 201
    print_result(success, "POST /api/reel", status, body=body)

    # 14. Get All Reels
    status, body = client2.make_request("GET", "/api/reels")
    success = status == 200 and "content" in body
    print_result(success, "GET /api/reels", status, body=body)

    # ---------------------------------------------------------
    print_header("Chat & Messaging Module")
    # ---------------------------------------------------------

    # 15. Create Chat
    status, body = client1.make_request("POST", "/api/chats/create", {
        "reciverId": user2_id
    })
    success = status == 201
    chat_id = body.get("id")
    print_result(success, "POST /api/chats/create", status, f"Chat ID: {chat_id}", body=body)

    # 16. Send Message
    status, body = client1.make_request("POST", f"/api/message/create/{chat_id}", {
        "content": "Hello Jane! This is an automated message."
    })
    success = status == 201
    print_result(success, f"POST /api/message/create/{chat_id}", status, body=body)

    # 17. Get Chat History
    status, body = client2.make_request("GET", f"/api/message/chat/{chat_id}")
    success = status == 200 and isinstance(body, list) and len(body) > 0
    print_result(success, f"GET /api/message/chat/{chat_id}", status, body=body)

    print(f"\n{GREEN}✨ All System Tests Executed ✨{RESET}")

if __name__ == "__main__":
    run_tests()
