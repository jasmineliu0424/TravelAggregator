# flightsearch

## API Docs

## Search flights

```http
GET /api/v1/flightsearch/flights?<queryStringParams> HTTP/1.1
Host: localhost:18085
```

| Attribute           | Type   | Optional | Description                                        |
|---------------------|--------|----------|----------------------------------------------------|
| departureAirport    | string | Yes      | [The IATA code of the departure airport.](https://www.iata.org/en/publications/directories/code-search/)                  |
| arrivalAirport      | string | Yes      | [The IATA code of the arrival airport.](https://www.iata.org/en/publications/directories/code-search/)                    |
| departureDatetime   | Date   | Yes      | The date and time of the departure in any machine date format.                 |
| airline             | string | Yes      | [The IATA code of the airline.](https://www.iata.org/en/publications/directories/code-search/)                            |
| maxPrice            | number | Yes      | The maximum price for the flight.                   |
| minPrice            | number | Yes      | The minimum price for the flight.                   |
| numPassengers       | number | Yes      | The number of passengers.                           |
| flightId            | string | Yes      | The identifier for the flight.                      |


### Example Request

```http
GET /api/v1/flightsearch/flights?departureAirport=JFK HTTP/1.1
Host: localhost:18085
```
