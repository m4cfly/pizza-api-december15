# API Documentation

## Endpoints

### Test Endpoint
A simple GET request to the test endpoint. No need for login.

**GET** `{{url}}/auth/test/`

---

### Populate the Database with Some Data
Populate the database with pizza orders for testing purposes.

**POST** `{{url}}/pizza-orders/populate/`

---

### Create a New User
This endpoint allows you to create a new user. By default, the user will have the `USER` role.

**POST** `{{url}}/auth/register/`

```json
{
    "username": "user",
    "password": "test123"
}
```

---

### Login
Authenticate a user and retrieve a JWT token.

**POST** `{{url}}/auth/login/`

```json
{
    "username": "user",
    "password": "test123"
}
```

Upon successful login:

```javascript
{% 
    client.global.set("jwt_token", response.body.token);
    console.log("JWT Token:", client.global.get("jwt_token"));
%}
```

---

### Protected Endpoints

#### User Demo
Retrieve information accessible by `USER` roles.

**GET** `{{url}}/protected/user_demo/`

Headers:
```plaintext
Accept: application/json
Authorization: Bearer {{jwt_token}}
```

#### Admin Demo
Retrieve information accessible by `ADMIN` roles.

**GET** `{{url}}/protected/admin_demo/`

Headers:
```plaintext
Accept: application/json
Authorization: Bearer {{jwt_token}}
```

---

### Add Admin Role to a User
Grant `ADMIN` role to an existing user.

**POST** `{{url}}/auth/user/addrole/`

Headers:
```plaintext
Accept: application/json
Authorization: Bearer {{jwt_token}}
```

Body:
```json
{
    "role": "ADMIN"
}
```

---

### Pizza Orders

#### Get All Pizza Orders
Retrieve all pizza orders.

**GET** `{{url}}/pizza-orders`

Headers:
```plaintext
Accept: application/json
Authorization: Bearer {{jwt_token}}
```

#### Get User's Pizza Orders
Retrieve the pizza orders created by the logged-in user.

**GET** `{{url}}/pizza-orders/mine`

Headers:
```plaintext
Accept: application/json
Authorization: Bearer {{jwt_token}}
```

#### Get a Specific Pizza Order
Retrieve details of a specific pizza order by its ID.

**GET** `{{url}}/pizza-orders/1`

Headers:
```plaintext
Accept: application/json
Authorization: Bearer {{jwt_token}}
```

#### Create a New Pizza Order
Create a new pizza order.

**POST** `{{url}}/pizza-orders`

Headers:
```plaintext
Content-Type: application/json
Authorization: Bearer {{jwt_token}}
```

Body:
```json
{
  "pizzaName": "Margherita",
  "quantity": 2,
  "price": 15.99,
  "done": false,
  "user": {
    "username": "usertest",
    "password": null,
    "roles": ["USER"]
  }
}
```

#### Update a Pizza Order
Update an existing pizza order by its ID.

**PUT** `{{url}}/pizza-orders/1`

Headers:
```plaintext
Content-Type: application/json
Authorization: Bearer {{jwt_token}}
```

Body:
```json
{
  "pizzaName": "Pepperoni",
  "quantity": 3,
  "price": 18.99
}
```

#### Delete a Pizza Order
Delete a specific pizza order by its ID.

**DELETE** `{{url}}/pizza-orders/1`

Headers:
```plaintext
Authorization: Bearer {{jwt_token}}
```

---
