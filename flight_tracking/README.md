# Flight Tracking

## API Documentation

```http
POST /flight-tracking/ HTTP/1.1
Host: localhost:18082
Content-Type: application/json
```

| Field                    | Type    | Required | Description                                        |
|--------------------------|---------|----------|----------------------------------------------------|
| data                     | object  | No      | Flight details                                     |
| data.flightid            | string  | No      | Unique identifier for the flight                   |
| data.carrier             | string  | No      | Carrier of the flight                              |
| data.flightnumber        | string  | Yes      | Flight number                                      |
| data.origin              | string  | No      | Origin airport code                                |
| data.destination         | string  | No      | Destination airport code                           |
| data.distancemiles       | number  | No      | Distance of the flight in miles                   |
| data.totalseats          | number  | No      | Total number of seats available                    |
| data.availableseats      | number  | No      | Number of available seats                           |
| data.aircraft            | string  | Yes     | Type of aircraft                                   |
| data.airtime             | number  | No      | Total airtime in minutes                           |
| data.departure_datetime   | string  | No      | Departure date and time (ISO 8601 format)        |
| data.flight_price        | number  | No      | Price of the flight                                |
| amount_paid              | number  | No       | Total amount paid for the booking                  |
| trip_id                  | number  | Yes*     | Unique identifier for the trip                     |
| responsible_user_ids     | array   | Yes*     | List of user IDs responsible for the booking       |

*Note: `trip_id` and `responsible_user_ids` are required if amount_paid is provided.

#### Example Request

```json
{
    "data": {
        "flightid": "b7382241-39a4-4c11-b9b6-a756d029014f",
        "carrier": "VX",
        "flightnumber": "407",
        "origin": "JFK",
        "destination": "LAX",
        "distancemiles": 2475,
        "totalseats": 151,
        "availableseats": 149,
        "aircraft": "Airbus A320",
        "airtime": 313,
        "departure_datetime": "2024-06-30T09:40:00.000Z",
        "flight_price": "284.99"
    },
    "amount_paid": 400,
    "trip_id": 1,
    "responsible_user_ids": [1, 2]
}
```

#### Example Response

```text
Flight booking received
```