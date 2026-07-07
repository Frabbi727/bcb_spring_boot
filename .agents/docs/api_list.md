# BCB Meeting App - REST API Specification

This document details the complete REST API specification to replace **Firebase Authentication, Firestore, and Firebase Storage** with a custom backend system.

It defines endpoints, request/response JSON schemas, authentication mechanisms, step-by-step registration API endpoints, and role-specific permissions for **Players, Coaches, and Admins**.

---

## 🏗️ Architecture & General Guidelines

### 1. Base URLs
*   **Development**: `https://api.dev.bcb.gov.bd/v1`
*   **Staging**: `https://api.staging.bcb.gov.bd/v1`
*   **Production**: `https://api.bcb.gov.bd/v1`

### 2. Authentication Protocol (OAuth 2.0 / JWT)
To replace Firebase Auth, use JWT tokens passed via the `Authorization` header:
`Authorization: Bearer <access_token>`

All protected endpoints return `401 Unauthorized` if the token is expired. Token refreshing is handled via a dedicated refresh token endpoint.

### 3. Sealed Response Protocol (Standard Envelope)
Every API endpoint response must follow this standard format:
```json
{
  "success": true,
  "data": { ... },
  "message": "Operation completed successfully",
  "errors": null
}
```
In case of errors (e.g., HTTP `400`, `422`, `500`):
```json
{
  "success": false,
  "data": null,
  "message": "Validation failed",
  "errors": [
    {
      "field": "phoneNumber",
      "message": "Invalid phone number format."
    }
  ]
}
```

---

## 🔐 Global Authentication APIs

Authentication is fully OTP-based. Mobile phone numbers are verified before allowing registration or logging into an existing session.

### 1. Registration OTP Flow

#### `POST /auth/register/otp/send`
Requests an OTP code to verify a player's contact number before starting registration.
*   **Request Headers**: `Content-Type: application/json`
*   **Request Body**:
    ```json
    {
      "phoneNumber": "+8801711112233"
    }
    ```
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "sessionId": "reg_otp_sess_abc123",
        "expiresInSeconds": 120
      },
      "message": "OTP sent successfully."
    }
    ```

#### `POST /auth/register/otp/verify`
Verifies the OTP code sent to the player.
*   **Request Body**:
    ```json
    {
      "sessionId": "reg_otp_sess_abc123",
      "phoneNumber": "+8801711112233",
      "otp": "123456"
    }
    ```
*   **Response (200 OK)**:
    Returns a temporary `registrationToken` needed to access step-by-step registration endpoints.
    ```json
    {
      "success": true,
      "data": {
        "registrationToken": "temp_reg_token_xyz789",
        "phoneNumber": "+8801711112233"
      },
      "message": "OTP verified successfully. Proceed to step-by-step registration."
    }
    ```

---

### 2. Login OTP Flow (All Roles)

Coaches and Admins can log in using either OTP or their password fallback (for local/offline showcasing).

#### `POST /auth/login/otp/send`
Sends an OTP to a registered user's phone number.
*   **Request Body**:
    ```json
    {
      "phoneNumber": "+8801863098727"
    }
    ```
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "sessionId": "login_otp_sess_def456",
        "expiresInSeconds": 120
      },
      "message": "OTP sent successfully."
    }
    ```

#### `POST /auth/login/otp/verify`
Verifies the login OTP code and issues JWT credentials.
*   **Request Body**:
    ```json
    {
      "sessionId": "login_otp_sess_def456",
      "phoneNumber": "+8801863098727",
      "otp": "654321"
    }
    ```
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "accessToken": "eyJhbGciOi...",
        "refreshToken": "eyJhbGciOi...",
        "expiresIn": 3600,
        "user": {
          "id": "usr_999",
          "name": "Coach Administrator",
          "email": "coach@example.com",
          "role": "COACH",
          "phoneNumber": "+8801863098727",
          "dob": "1988-03-01T00:00:00.000Z",
          "divisionId": "6",
          "districtId": "47"
        }
      },
      "message": "Login successful."
    }
    ```

---

### 3. Password Login (Developer / Emergency Fallback)
#### `POST /auth/login/password`
Allows login via email/phone and password for admins, coaches, or showcase purposes.
*   **Request Body**:
    ```json
    {
      "identity": "admin@example.com",
      "password": "password"
    }
    ```
*   **Response (200 OK)**: Same structure as OTP verification response.

### 4. Refresh Token & Session Terminate
#### `POST /auth/refresh`
Obtains a new access token using a refresh token.
*   **Request Body**: `{ "refreshToken": "..." }`
*   **Response (200 OK)**: `{ "accessToken": "...", "refreshToken": "..." }`

#### `POST /auth/logout`
Revokes active user sessions.
*   **Request Headers**: `Authorization: Bearer <access_token>`

---

## 🏃 Player APIs

### 1. Segmented Registration (Draft Mode)

To allow modularity, scaling, easy progress saving, and flexibility if step order changes, registration draft details are divided into endpoints named after their domain/responsibility (no step counts).
*   **Headers required**: `Authorization: Bearer <registrationToken>` (or standard `<access_token>` if editing a saved draft).
*   **Action type**: Saves state in backend as a `draft` status registration under the verified phone number.

To maintain integrity and facilitate changes without breaking relationships, dropdown selectors (e.g. positions, roles, divisions, schools) send unique IDs or Codes (e.g. `districtId`, `battingPositionCode`) instead of plain strings.

#### `GET /player/registration/draft`
Retrieves current saved progress of the registration.
*   **Response (200 OK)**: Returns the current partial `PlayerRegistrationModel` structure.

---

#### `PUT /player/registration/personal`
Saves Personal Information.
*   **Request Body**:
    ```json
    {
      "nameEn": "Fazle Rabbi",
      "nameBn": "ফজলে রাব্বি",
      "fatherName": "Md. Rafiqul Islam",
      "motherName": "Fatema Begum",
      "dob": "2010-05-15T00:00:00.000Z",
      "birthRegistrationNumber": "20102692518104523",
      "playerNidNumber": null
    }
    ```
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "id": "reg_555",
        "status": "draft"
      },
      "message": "Personal info saved successfully."
    }
    ```

---

#### `PUT /player/registration/cricket`
Saves Cricket Experience. References unique codes/IDs for roles, batting styles, and levels.
*   **Request Body**:
    ```json
    {
      "preferredBattingPositionCode": "BAT_POS_MIDDLE_ORDER",
      "primaryBowlingStyleCode": "BOWL_STYLE_OFFBREAK",
      "primaryRoleCode": "ROLE_ALLROUNDER",
      "isWicketkeeper": false,
      "highestCompetitiveLevelCode": "COMP_LEVEL_DISTRICT",
      "yearsOfExperience": 3,
      "academyClub": "Mirpur Cricket Academy",
      "participationRecord": ["U14 Championship 2025"],
      "participationEligibility": "Eligible"
    }
    ```
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "id": "reg_555",
        "status": "draft"
      },
      "message": "Cricket experience saved successfully."
    }
    ```

---

#### `PUT /player/registration/education`
Saves Education Details. Can be skipped by submitting blank/nulls. References dynamic schools by ID.
*   **Request Body**:
    ```json
    {
      "educationStatusCode": "EDU_STATUS_STUDYING",
      "schoolCollegeId": "sch_dhaka_01",
      "schoolClass": "Class 9",
      "examRollNo": "104",
      "examRegNo": "1002934",
      "examYear": "2025"
    }
    ```
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "id": "reg_555",
        "status": "draft"
      },
      "message": "Education details saved successfully."
    }
    ```

---

#### `PUT /player/registration/guardian`
Saves Guardian Information.
*   **Request Body**:
    ```json
    {
      "parentNid1Number": "19752692518104520",
      "parentNid2Number": "19782692518104521"
    }
    ```
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "id": "reg_555",
        "status": "draft"
      },
      "message": "Guardian info saved successfully."
    }
    ```

---

#### `PUT /player/registration/location`
Saves Present and Permanent Addresses. Links location divisions/districts/upazilas/unions by ID.
*   **Request Body**:
    ```json
    {
      "presentAddressObj": {
        "divisionId": "div_dhaka",
        "districtId": "dist_dhaka",
        "upazilaId": "upa_mirpur",
        "unionId": "uni_section12",
        "house": "House 12, Road 4",
        "village": "Mirpur",
        "po": "Mirpur-12",
        "postcode": "1216"
      },
      "permanentSameAsPresent": true,
      "permanentAddressObj": null
    }
    ```
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "id": "reg_555",
        "status": "draft"
      },
      "message": "Location info saved successfully."
    }
    ```

---

#### `POST /player/registration/documents`
Uploads files and stores them. Replaces Firebase Storage.
*   **Request Format**: `multipart/form-data`
*   **Form Fields**:
    *   `faceFront`: File (image)
    *   `faceLeft`: File (image)
    *   `faceRight`: File (image)
    *   `birthCertificate`: File (pdf/image)
    *   `playerNid`: File (optional, image)
    *   `fatherNidFront`: File (image)
    *   `fatherNidBack`: File (image)
    *   `motherNidFront`: File (image)
    *   `motherNidBack`: File (image)
    *   `educationCert`: File (optional, pdf/image)
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "faceFrontPath": "https://storage.bcb.gov.bd/players/usr_100/face_front.jpg",
        "faceLeftPath": "https://storage.bcb.gov.bd/players/usr_100/face_left.jpg",
        "faceRightPath": "https://storage.bcb.gov.bd/players/usr_100/face_right.jpg",
        "birthCertificatePath": "https://storage.bcb.gov.bd/players/usr_100/birth_certificate.jpg",
        "fatherNidFrontPath": "https://storage.bcb.gov.bd/players/usr_100/father_nid_front.jpg",
        "fatherNidBackPath": "https://storage.bcb.gov.bd/players/usr_100/father_nid_back.jpg",
        "motherNidFrontPath": "https://storage.bcb.gov.bd/players/usr_100/mother_nid_front.jpg",
        "motherNidBackPath": "https://storage.bcb.gov.bd/players/usr_100/mother_nid_back.jpg",
        "playerNidPath": null,
        "examCertPath": null
      },
      "message": "Documents uploaded successfully."
    }
    ```

---

#### `POST /player/registration/submit`
Finalizes the registration, changes status from `draft` to `pending_approval`. Validates completeness of all required registration details.
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "id": "reg_555",
        "status": "pending_approval",
        "createdAt": "2026-07-06T21:38:31.000Z"
      },
      "message": "Registration submitted successfully for review."
    }
    ```

#### `DELETE /player/registration`
Discards the current draft.
*   **Response (200 OK)**: `{ "success": true, "message": "Draft discarded." }`

---

### 2. Registration Status & History
#### `GET /player/registrations`
Lists all registrations submitted by the logged-in player.
*   **Response (200 OK)**: Array of Player Registration models.

#### `GET /player/registration/status`
Fetches the status of the player's latest registration.
*   **Response (200 OK)**: Returns status values: `draft`, `pending_approval`, `under_review`, `approved`, or `rejected`.

---

### 3. Match Calendar & Schedules
#### `GET /player/matches`
Fetches the local or general match schedules assigned to the player's category.
*   **Query Parameters**: `category=U14|U16|U19`
*   **Response (200 OK)**: Array of match schedules.

---

## 🗂️ Dropdown & Metadata Retrieval APIs (All Roles)

These endpoints supply UI dropdown controls with dynamic values. Every selection option features a unique database `id` or `code` mapping to maintain relationships.

#### `GET /metadata/playing-roles`
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": [
        { "code": "ROLE_ALLROUNDER", "nameEn": "Allrounder", "nameBn": "অলরাউন্ডার" },
        { "code": "ROLE_BATSMAN", "nameEn": "Batsman", "nameBn": "ব্যাটসম্যান" },
        { "code": "ROLE_BOWLER", "nameEn": "Bowler", "nameBn": "বোলার" }
      ]
    }
    ```

#### `GET /metadata/batting-positions`
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": [
        { "code": "BAT_POS_OPENER", "nameEn": "Opening Batsman", "nameBn": "ওপেনার" },
        { "code": "BAT_POS_MIDDLE_ORDER", "nameEn": "Middle Order", "nameBn": "মিডল অর্ডার" },
        { "code": "BAT_POS_TAIL_ENDER", "nameEn": "Tail-ender", "nameBn": "টেইল-এন্ডার" }
      ]
    }
    ```

#### `GET /metadata/bowling-styles`
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": [
        { "code": "BOWL_STYLE_OFFBREAK", "nameEn": "Right-arm Offbreak", "nameBn": "রাইট-আর্ম অফব্রেক" },
        { "code": "BOWL_STYLE_PACER", "nameEn": "Right-arm Fast Medium", "nameBn": "রাইট-আর্ম ফাস্ট মিডিয়াম" },
        { "code": "BOWL_STYLE_LEFT_ORTHO", "nameEn": "Left-arm Orthodox", "nameBn": "লেফট-আর্ম অর্থোডক্স" }
      ]
    }
    ```

#### `GET /metadata/competitive-levels`
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": [
        { "code": "COMP_LEVEL_DISTRICT", "nameEn": "District League", "nameBn": "জেলা লীগ" },
        { "code": "COMP_LEVEL_DIVISION", "nameEn": "Divisional League", "nameBn": "বিভাগীয় লীগ" },
        { "code": "COMP_LEVEL_NATIONAL", "nameEn": "National Team", "nameBn": "জাতীয় দল" }
      ]
    }
    ```

#### `GET /metadata/education-statuses`
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": [
        { "code": "EDU_STATUS_STUDYING", "nameEn": "Studying", "nameBn": "অধ্যয়নরত" },
        { "code": "EDU_STATUS_COMPLETED", "nameEn": "Completed", "nameBn": "সম্পন্ন" }
      ]
    }
    ```

#### `GET /metadata/schools`
Retrieves schools list (supports search filtering).
*   **Query Parameters**: `search=mirpur`
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": [
        { "id": "sch_dhaka_01", "nameEn": "Mirpur High School", "nameBn": "মিরপুর হাই স্কুল" },
        { "id": "sch_dhaka_02", "nameEn": "Tongi Govt. School", "nameBn": "টঙ্গী সরকারী স্কুল" }
      ]
    }
    ```

#### `GET /metadata/locations`
Fetches hierarchical geographic nodes.
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": [
        {
          "id": "div_dhaka",
          "nameEn": "Dhaka",
          "nameBn": "ঢাকা",
          "districts": [
            {
              "id": "dist_dhaka",
              "nameEn": "Dhaka",
              "nameBn": "ঢাকা",
              "upazilas": [
                {
                  "id": "upa_mirpur",
                  "nameEn": "Mirpur",
                  "nameBn": "মিরপুর",
                  "unions": [
                    { "id": "uni_section12", "nameEn": "Section 12", "nameBn": "সেকশন ১২" }
                  ]
                }
              ]
            }
          ]
        }
      ]
    }
    ```

---

## 🧢 Coach APIs

Coaches verify players at the district level.

### 1. Player Registrations Review
#### `GET /coach/registrations`
Lists registrations from the coach's assigned district.
*   **Query Parameters**:
    *   `status`: `pending` | `approved` | `rejected`
    *   `search`: Search string (by name, phone)
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": [
        {
          "id": "reg_555",
          "nameEn": "Fazle Rabbi",
          "contact": "+8801711112233",
          "status": "pending_approval",
          "presentAddressObj": {
            "districtId": "dist_dhaka"
          }
        }
      ]
    }
    ```

#### `GET /coach/registrations/{id}`
Returns full detailed view of a registration (including steps data and documents) for verification.

---

### 2. Verification / Rejection Actions
#### `POST /coach/registrations/{id}/verify`
Verifies a registration, setting `isCacheVerified = true` and adding comments/expertise.
*   **Request Body**:
    ```json
    {
      "playerExpertise": ["Batsman", "Top Order"],
      "coachComment": "Highly talented player. Passed physical verification check."
    }
    ```
*   **Response (200 OK)**:
    ```json
    {
      "success": true,
      "data": {
        "id": "reg_555",
        "status": "pending_approval",
        "isCacheVerified": true,
        "verifiedByCoach": {
          "id": "usr_999",
          "name": "Coach Administrator"
        }
      }
    }
    ```

#### `POST /coach/registrations/{id}/reject`
Rejects a registration, requesting changes.
*   **Request Body**:
    ```json
    {
      "feedbackChips": ["Invalid NID", "Blurry Face Capture"],
      "comment": "Face capture is not clear. Please capture again with proper light.",
      "resubmitAfterDate": "2026-07-15T00:00:00.000Z",
      "isPermanent": false
    }
    ```
*   **Response (200 OK)**: Returns updated registration status as `rejected`.

---

## 👑 Admin (Super Admin) APIs

Admins have full global oversight. They can manage match calendars, configure parameters, and edit dropdown selections dynamically to maintain data relationships.

### 1. Dropdown & Metadata Management (CRUD)
Administrators use these endpoints to add new values, modify labels (English/Bangla), or deprecate options in any dropdown list dynamically.

#### Playing Roles CRUD
*   `POST /admin/metadata/playing-roles` - Create playing role selection.
    *   **Request Body**: `{ "code": "ROLE_BATSMAN", "nameEn": "Batsman", "nameBn": "ব্যাটসম্যান" }`
*   `PUT /admin/metadata/playing-roles/{code}` - Modify playing role names.
    *   **Request Body**: `{ "nameEn": "Pure Batsman", "nameBn": "খাঁটি ব্যাটসম্যান" }`
*   `DELETE /admin/metadata/playing-roles/{code}` - Delete role option.

#### Batting Positions CRUD
*   `POST /admin/metadata/batting-positions`
*   `PUT /admin/metadata/batting-positions/{code}`
*   `DELETE /admin/metadata/batting-positions/{code}`

#### Bowling Styles CRUD
*   `POST /admin/metadata/bowling-styles`
*   `PUT /admin/metadata/bowling-styles/{code}`
*   `DELETE /admin/metadata/bowling-styles/{code}`

#### Competitive Levels CRUD
*   `POST /admin/metadata/competitive-levels`
*   `PUT /admin/metadata/competitive-levels/{code}`
*   `DELETE /admin/metadata/competitive-levels/{code}`

#### Education Statuses CRUD
*   `POST /admin/metadata/education-statuses`
*   `PUT /admin/metadata/education-statuses/{code}`
*   `DELETE /admin/metadata/education-statuses/{code}`

#### Schools Management CRUD
*   `POST /admin/metadata/schools`
    *   **Request Body**: `{ "id": "sch_dhaka_03", "nameEn": "Savar High School", "nameBn": "সাভার উচ্চ বিদ্যালয়" }`
*   `PUT /admin/metadata/schools/{id}`
    *   **Request Body**: `{ "nameEn": "Savar Model High School", "nameBn": "সাভার মডেল উচ্চ বিদ্যালয়" }`
*   `DELETE /admin/metadata/schools/{id}`

#### Locations Dynamic Hierarchy CRUD
Create, update, or delete divisions, districts, upazilas, or unions.
*   `POST /admin/metadata/locations/divisions`
    *   **Request Body**: `{ "id": "div_barishal", "nameEn": "Barishal", "nameBn": "বরিশাল" }`
*   `POST /admin/metadata/locations/districts`
    *   **Request Body**: `{ "id": "dist_bhola", "divisionId": "div_barishal", "nameEn": "Bhola", "nameBn": "ভোলা" }`
*   `POST /admin/metadata/locations/upazilas`
    *   **Request Body**: `{ "id": "upa_sadar", "districtId": "dist_bhola", "nameEn": "Bhola Sadar", "nameBn": "ভোলা সদর" }`
*   `POST /admin/metadata/locations/unions`
    *   **Request Body**: `{ "id": "uni_ilisha", "upazilaId": "upa_sadar", "nameEn": "Ilisha", "nameBn": "ইলিশা" }`
*   `PUT /admin/metadata/locations/{nodeType}/{id}`
    *   *Note: `nodeType` can be `divisions`, `districts`, `upazilas`, or `unions`.*
*   `DELETE /admin/metadata/locations/{nodeType}/{id}`

---

### 2. Global Registration Oversight
#### `GET /admin/registrations`
Lists all registrations in the system.
*   **Query Parameters**:
    *   `status`: `all` | `pending` | `approved` | `rejected`
    *   `districtId`: `dist_dhaka`
    *   `divisionId`: `div_dhaka`
    *   `search`: Filter by name, serial/playerId, phone
    *   `page`: `1`
    *   `limit`: `20`

#### `GET /admin/registrations/{id}`
Retrieves complete details of any registration doc.

---

### 3. Final Approval & Rejection
#### `POST /admin/registrations/{id}/approve`
Approves the registration. The server generates a unique player serial and QR code.
*   **Request Body**:
    ```json
    {
      "adminComment": "Verified against education certificates. Approved."
    }
    ```
*   **Response (200 OK)**:
    Returns the final approved player model, containing `playerId` (e.g. `U14-2026-0043`) and QR Code url.
    ```json
    {
      "success": true,
      "data": {
        "id": "reg_555",
        "status": "approved",
        "isVerified": true,
        "isAdminVerified": true,
        "playerId": "U14-2026-0043",
        "qrCodeData": "https://bcb.gov.bd/player/U14-2026-0043",
        "approvedBy": "Administrator",
        "approvedAt": "2026-07-06T21:40:00.000Z"
      }
    }
    ```

#### `POST /admin/registrations/{id}/reject`
Admin rejection (replaces player status to `rejected`).
*   **Request Body**:
    ```json
    {
      "feedbackChips": ["Permanently Ineligible"],
      "comment": "Age check failed via official database lookup.",
      "isPermanent": true
    }
    ```

---

### 4. Match Calendar Management (CRUD)
*   `POST /admin/matches` - Creates a new match entry.
*   `PUT /admin/matches/{id}` - Updates a match entry.
*   `DELETE /admin/matches/{id}` - Deletes a match schedule.

### 5. Sponsor Banners Management (CRUD)
*   `GET /admin/banners` - Get lists of banners.
*   `POST /admin/banners` - Upload / create a sponsor banner.
*   `PUT /admin/banners/{id}` - Edit image, title, target link.
*   `DELETE /admin/banners/{id}` - Delete banner.

---

## 📂 Shared Static/Config APIs

#### `GET /config/faqs`
Fetches localized FAQs.
*   **Query Parameters**: `lang=en|bn`

#### `GET /config/banners`
Fetches dynamic sponsor banners.
