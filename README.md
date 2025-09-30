# guestbook-api
A simple REST API for managing guestbook entries with profanity checking.


# Prerequisites
* Java 24 or higher
* Docker and Docker Compose

# Quick Start

#### 1. Create `.env` file:
```sh
DB_ROOT_PASSWORD=root_password
DB_NAME=guestbook
DB_USERNAME=guestbook_user
DB_PASSWORD=guestbook_password
DB_CONNECTION_URL=jdbc:mariadb://localhost:3306/guestbook
```

#### 2. Start the database
```sh
docker-compose up -d
```

#### 3. Start the application
```sh
./gradlew bootRun
```

# API Endpoints
* `GET /api/v1/guestbook/all?page=0&size=10` - List all guestbook entries (paginated)
* `POST /api/v1/guestbook/create` - Create a new guestbook entry

Example `POST` request:
```sh
curl -X POST http://localhost:8080/api/v1/guestbook/create \
  -H "Content-Type: application/json" \
  -d '{"author": "jokvn", "message": "Hello world!"}'
```

# Notes
* Maximum length for name is 15 characters
* Maximum length for messages is 200 characters
* Only certain characters are allowed (letters, numbers, and some special characters)
* All entries are checked for profanity, posts containing inappropriate words will not be created
