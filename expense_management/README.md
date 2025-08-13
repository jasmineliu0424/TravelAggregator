# Expense Management

## API Documentation

### Add Expense

#### Request Body
| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| trip_id                | number | Yes      | Unique identifier for the trip                     |
| amount                 | number | Yes      | Total amount for the expense                        |
| description            | string | No       | Description of the expense                          |
| responsible_user_ids   | array[number]  | Yes      | List of user IDs responsible for the expense       |
| source                 | string | Yes      | Source of the expense (e.g., HOTEL)                |

#### Example Request

```http
POST /expenses/ HTTP/1.1
Host: localhost:18086
Content-Type: application/json

{
    "trip_id": 1,
    "amount": 150.75,
    "description": "Hotel",
    "responsible_user_ids": [1, 2],
    "source": "HOTEL"
}
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1
}
```

### Update Expense

#### Path Parameters

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| id                     | number | Yes      | Unique identifier for the expense                  |

#### Request Body

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| amount                 | number | Yes      | Total amount for the expense                        |

#### Example Request

```http
PUT /expenses/{id} HTTP/1.1
Host: localhost:18086
Content-Type: application/json

{
    "amount": 200.00
}
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1
}
```

### Remove Expense

#### Path Parameters

| Field                  | Type   | Required | Description                                        |
|------------------------|--------|----------|----------------------------------------------------|
| id                     | number | Yes      | Unique identifier for the expense                  |

#### Example Request

```http
DELETE /expenses/{id} HTTP/1.1
Host: localhost:18086
```

#### Example Response

```http
HTTP/1.1 204 No Content
```

### Query Expenses

#### Request Parameters

| Field  | Type   | Required | Description                                        |
|--------|--------|----------|----------------------------------------------------|
| tripId | number | Yes      | Unique identifier for the trip                     |
| userId | number | Yes      | Unique identifier for the user                     |

#### Example Request

```http
GET /expenses/?tripId=1&userId=1 HTTP/1.1
Host: localhost:18086
```

#### Example Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

150.75
```