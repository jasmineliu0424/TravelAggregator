# Flight Search

## API Documentation

### Search Flights

```http
GET /api/v1/flightsearch/flights HTTP/1.1
Host: localhost:18084
```

#### Query Parameters

| Parameter        | Type   | Required | Description                                 |
|------------------|--------|----------|---------------------------------------------|
| departureAirport | string | Yes      | The departure airport, in [IATA code](https://www.iata.org/en/publications/directories/code-search/)                 |
| arrivalAirport   | string | Yes      | The arrival airport, in [IATA code](https://www.iata.org/en/publications/directories/code-search/)                    |
| departureDatetime| string | No       | The departure date and time (ISO 8601 format)|
| airline          | string | No       | The airline carrier, in [IATA code](https://www.iata.org/en/publications/directories/code-search/)                    |
| maxPrice         | number | No       | The maximum flight price                    |
| minPrice         | number | No       | The minimum flight price                    |
| numPassengers    | number | No       | The number of passengers                    |

#### Example Request

```http
GET /api/v1/flightsearch/flights?departureAirport=JFK HTTP/1.1
Host: localhost:18084
```

#### Example Response

```json
{
    "flights": [
        {
            "flightId": "511b62a4-dd11-45a6-a0c7-f013cd8222b1",
            "departureDatetime": "2024-05-07T16:57:00Z",
            "airtimeMinutes": 216,
            "flightNumber": "329",
            "distanceMiles": 1598,
            "totalSeats": 500,
            "availableSeats": 500,
            "aircraft": "Boeing 787 Dreamliner",
            "flightPrice": 299.99,
            "origin": "JFK",
            "destination": "SJU",
            "carrier": "DL"
        },
        {
            "flightId": "c572e8b6-9b36-469c-9ae5-76d3df1e8ce8",
            "departureDatetime": "2024-12-08T08:59:00Z",
            "airtimeMinutes": 376,
            "flightNumber": "422",
            "distanceMiles": 2475,
            "totalSeats": 151,
            "availableSeats": 151,
            "aircraft": "Airbus A350 XWB",
            "flightPrice": 388.99,
            "origin": "JFK",
            "destination": "LAX",
            "carrier": "DL"
        }
    ]
}
```
