# Vispar API specification

## General
API base URL: `http://<ipAddressOfServer>/api`  
Request authorization header: `Authorization: Bearer <token>`

## Overview
* [Authentication](#authentication)
* [Patterns](#patterns)
* [Sensors](#sensors)

## Endpoints

### Authentication

#### POST `/auth/login`
Logs an user in.
```json
// request parameters
{
    "username": "<username>",
    "password": "<sha512-encrypted password>"
}

// response (if succeeded, status = 200)
{
    "token": "<token>"
}
```

#### POST `/auth/logout`
Logs an user out.
```json
// no request parameters needed
// empty response (if succeeded, status = 204)
```

### Patterns

#### GET `/patterns`
Returns a list of all patterns existing on the server (using proxy objects).
```json
// no request parameters needed
// response (if succeeded, status = 200)
[
    "<patternProxyObject>",
    "..."
]
```

#### GET `/patterns`
Returns the (complete) pattern from the server.
```json
// request parameters
{
    "id": "<patternId>"
}

// response (if succeeded, status = 200)
"<patternObject>"
```

#### POST `/patterns`
Creates a new pattern on the server.
```json
// request parameters
"<patternObject>"

// response (if succeeded, status = 201)
"<patternObject>"
```

#### PUT `/patterns`
Updates the pattern on the server.
```json
// request parameters
"<patternObject>"

// response (if succeeded, status = 200)
"<patternObject>"
```

#### DELETE `/patterns`
Deletes the pattern from the server.
```json
// request parameters
"<patternObject>"

// empty response (if succeeded, status = 204)
```

#### POST `/patterns/deploy`
Deploys the pattern on the server.
```json
// request parameters
"<patternObject>"

// response (if succeeded, status = 200)
"<patternObject>"
```

#### POST `/patterns/undeploy`
Undeploys the pattern on the server.
```json
// request parameters
"<patternObject>"

// response (if succeeded, status = 200)
"<patternObject>"
```

### Sensors

#### GET `/sensors`
Returns a list of all sensors registered on the server.
```json
// no request parameters needed
// response (if succeeded, status = 200)
[
    "<sensorObject - we have to agree on a commonly used class??>",
    "..."
]
```
