# hotelsearch

## API Docs

## Search hotels

```http
POST /api/v1/hotelsearch/hotels HTTP/1.1
Host: localhost:18084
Content-Type: application/json
```

### Request Body

| Attribute        | Type                         | Optional | Description                              |
|------------------|------------------------------|----------|------------------------------------------|
| startDate        | Date                         | Yes      | The start date of the hotel stay.         |
| endDate          | Date                         | Yes      | The end date of the hotel stay.           |
| requestedRooms   | Map<RoomType, Integer>       | Yes      | The requested number of rooms by type. A map from `roomType` to number of rooms you wish to book of that type. `roomType` may be any combination of `one_person_rooms`, `two_person_rooms`, and `four_person_rooms`.     |
| priceRange       | Map<RoomType, PriceRange>    | Yes      | The price range for each room type. A map from `roomType` to number of rooms you wish to the price range you wish to pay for that room. `roomType` may be any combination of `one_person_rooms`, `two_person_rooms`, and `four_person_rooms`.          |
| priceRange.one_person_rooms.minPrice  | number         | Yes      | Minimum price for one person rooms.                         |
| priceRange.one_person_rooms.maxPrice  | number         | Yes      | Maximum price for one person rooms.                         |
| priceRange.two_person_rooms.minPrice  | number         | Yes      | Minimum price for two person rooms.                         |
| priceRange.two_person_rooms.maxPrice  | number         | Yes      | Maximum price for two person rooms.                         |
| priceRange.four_person_rooms.minPrice | number         | Yes      | Minimum price for four person rooms.                        |
| priceRange.four_person_rooms.maxPrice | number         | Yes      | Maximum price for four person rooms.                        |
| hotelId          | String                       | Yes      | The identifier for the hotel.             |
| name             | String                       | Yes      | The name of the hotel.                    |
| longitude        | Double                       | Yes      | The longitude of the hotel's location.    |
| latitude         | Double                       | Yes      | The latitude of the hotel's location.     |
| radius           | Double                       | Yes      | The search radius around the location.    |


### Example Request

```http
POST /api/v1/hotelsearch/hotels HTTP/1.1
Host: localhost:18084
Content-Type: application/json

{
    "startDate": "2024-01-01",
    "endDate": "2024-01-03",
    "requestedRooms": {
        "one_person_rooms": 1,
        "two_person_rooms": 0,
        "four_person_rooms": 0
    },
    "priceRange": {
        "one_person_rooms": {
            "minPrice": 0,
            "maxPrice": 180
        }
    },
    "name": "marriott",
    "latitude": 40,
    "longitude": -73,
    "radius": 10
}
```
### Example Response

```json
{
    "hotels": [
        {
            "hotelId": "ae85b885-0e00-420f-9209-3fe433a3c221",
            "boroughCode": "1",
            "block": "839",
            "lot": "1201",
            "streetNumber": "33",
            "streetName": "WEST 37TH STREET",
            "zipcode": 10018,
            "buildingClass": "RH",
            "ownerName": "MARRIOTT OWNERSHIP RESORTS, INC.",
            "borough": "1",
            "roomRates": {
                "ONE_PERSON_RATE": 100,
                "TWO_PERSON_RATE": 130,
                "FOUR_PERSON_RATE": 240
            },
            "latitude": 40.750864,
            "longitude": -73.98439,
            "area": "Midtown-Midtown South",
            "address": "Lot 1201, Block 839\n33, WEST 37TH STREET\n10018"
        },
        {
            "hotelId": "1393a4f1-e2a1-4259-ae2b-a2ebcca99f7f",
            "boroughCode": "1",
            "block": "839",
            "lot": "1202",
            "streetNumber": "33",
            "streetName": "WEST 37TH STREET",
            "zipcode": 10018,
            "buildingClass": "RH",
            "ownerName": "MARRIOTT OWNERSHIP RESORTS, INC.",
            "borough": "1",
            "roomRates": {
                "ONE_PERSON_RATE": 120,
                "TWO_PERSON_RATE": 150,
                "FOUR_PERSON_RATE": 210
            },
            "latitude": 40.750864,
            "longitude": -73.98439,
            "area": "Midtown-Midtown South",
            "address": "Lot 1202, Block 839\n33, WEST 37TH STREET\n10018"
        }
    ]
}
```
