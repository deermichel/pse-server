# Vispar API specification

## General
API base URL: `http://<ipAddressOfServer>/api`  
Request authorization header: `Authorization: Bearer <token>`  
Response on error:
```json
{
    "error": "<code>"
}
```

## Overview
* [Authentication](#authentication)
* [Patterns](#patterns)
* [Sensors](#sensors)

## Endpoints

### Authentication

#### POST `/auth/login`
Logs a user in.
```json
// request parameters
{
    "username": "<username>",
    "password": "<sha512-encrypted password>"
}

// response on success
{
    "data": {
        "token": "<token>"
    }
}

// errors
1000 Invalid request format.
2000 User not found or password incorrect.
```

#### POST `/auth/logout`
Logs a user out.
```json
// no request parameters needed

// response on success
{}
```

### Patterns

#### GET `/patterns/all`
Returns a list of all patterns existing on the server (using proxy objects).
```json
// no request parameters needed

// response on success
{
    "data": [
        "<patternProxyObject>",
        "..."
    ]
}

// errors
1001 Not authorized.
```

#### GET `/patterns`
Returns the (complete) pattern from the server.
```json
// request parameters
{
    "id": "<patternId>"
}

// response on success
{
    "data": "<patternObject>"
}

// errors
1000 Invalid request format.
1001 Not authorized.
3000 Pattern not found.
```

#### POST `/patterns`
Creates or updates a pattern on the server.
```json
// request parameters
"<patternObject>"

// response on success
{
    "data": "<patternObject>"
}

// errors
1000 Invalid request format.
1001 Not authorized.
3001 Pattern previously edited by another user -> resend with new id.
```

#### POST `/patterns/delete`
Deletes the pattern from the server.
```json
// request parameters
{
    "id": "<patternId>"
}

// response on success
{}

// errors
1000 Invalid request format.
1001 Not authorized.
3000 Pattern not found.
```

#### POST `/patterns/deploy`
Deploys the pattern on the server.
```json
// request parameters
{
    "id": "<patternId>"
}

// response on success
{
    "data": "<patternObject>"
}

// errors
1000 Invalid request format.
1001 Not authorized.
3000 Pattern not found.
3002 Pattern already deployed.
```

#### POST `/patterns/undeploy`
Undeploys the pattern on the server.
```json
// request parameters
{
    "id": "<patternId>"
}

// response on success
{
    "data": "<patternObject>"
}

// errors
1000 Invalid request format.
1001 Not authorized.
3000 Pattern not found.
3003 Pattern not deployed.
```

### Sensors

#### GET `/sensors`
Returns a list of all sensors registered on the server.
```json
// no request parameters needed

// response on success
{
    "data": [
        "<sensorObject>",
        "..."
    ]
}

// errors
1001 Not authorized.
```
