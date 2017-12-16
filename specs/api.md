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
// response (if succeeded, status = 200)
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
```

#### GET `/patterns`
Returns the (complete) pattern from the server.
```json
// request parameters
// response (if succeeded, status = 200)
```

#### POST `/patterns`
Creates a new pattern on the server.
```json
// request parameters
// response (if succeeded, status = 201)
```

#### PUT `/patterns`
Updates the pattern on the server.
```json
// request parameters
// response (if succeeded, status = 200)
```

#### DELETE `/patterns`
Deletes the pattern from the server.
```json
// request parameters
// empty response (if succeeded, status = 204)
```

#### POST `/patterns/deploy`
Deploys the pattern on the server.
```json
// request parameters
// response (if succeeded, status = 200)
```

#### POST `/patterns/undeploy`
Undeploys the pattern on the server.
```json
// request parameters
// response (if succeeded, status = 200)
```

### Sensors

#### GET `/sensors`
Returns a list of all sensors registered on the server.
```json
// no request parameters needed
// response (if succeeded, status = 200)
```
