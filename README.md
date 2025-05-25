# 📦 Price Comparator API

An API designed to parse and compare prices of products across multiple retail stores (e.g., Lidl, Kaufland, Profi), track historical pricing data, highlight best value per unit, and manage dynamic shopping lists with optional price alerts.

---

## 🚀 How to Run the Project

### ✅ Prerequisites

* Java 17+
* Maven
* MySQL (or MariaDB)

### 📂 Database Setup

Create a database named `pricecomparator` in MySQL Workbench or your preferred tool.

```
CREATE DATABASE pricecomparator;
```

### ⚙️ application.properties

Update `src/main/resources/application.properties` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pricecomparator
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### ▶️ Run the App

```bash
mvn spring-boot:run
```

---

## 🔗 REST Endpoints

### 📘 Store Products

* `GET /api/store-products` — list all store-specific products
* `GET /api/store-products/{id}` — get details by ID
* `GET /api/store-products/best-value/{abstractProductId}` — get cheapest per-unit options
* `GET /api/store-products/price-history` — flexible price tracking

  * `productName` (required)
  * `store` (optional)
  * `start`, `end` (optional, ISO format `YYYY-MM-DD`, defaults to last 30 days)

### 💸 Discount Products

* `GET /api/discounts` — list all active discounts
* `GET /api/discounts/top?limit=5` — get top N highest discounts
* `GET /api/discounts/by-store?store=LIDL` — discounts from a specific store

### 📟 Price Alerts

* `GET /api/alerts` — get all alerts
* `GET /api/alerts/store-product/{id}` — alerts for a specific store product
* `GET /api/alerts/matched` — alerts that matched recent prices
* `POST /api/alerts/add` — add a new alert (`storeProductId`, `targetPrice`)
* `DELETE /api/alerts/{id}` — delete an alert

### 🛒 Shopping Lists

* `GET /api/shopping-lists` — list all shopping lists
* `GET /api/shopping-lists/{id}` — view a specific list
* `GET /api/shopping-lists/{id}/items` — items in a list
* `POST /api/shopping-lists/create?listId=weekly&name=Weekly%20List` — create new list
* `POST /api/shopping-lists/{id}/add?storeProductId=X&quantity=Y` — add item
* `DELETE /api/shopping-lists/items/{itemId}` — remove single item
* `DELETE /api/shopping-lists/{id}/clear` — remove all items from list

---

## 📂 File Input Format

### Standard Product File

Filename: `storename_yyyy-MM-dd.csv`
Header:

```
product_id;product_name;product_category;brand;package_quantity;package_unit;price;currency
```

### Discount Product File

Filename: `storename_discountyyyy-MM-dd.csv`
Header:

```
product_id;product_name;brand;package_quantity;package_unit;product_category;from_date;to_date;percentage_of_discount
```

Files are parsed and saved to the database on app start (or via scheduled batch trigger).

---

## 💡 Features

* Unit normalization (ml->l, g->kg)
* Value-per-unit computation
* Historical price tracking from parsed CSVs
* Custom alerts when a product price drops below a threshold
* Smart shopping list management with per-product quantity

---

## 🔧 Simplifications and Improvements

### Simplifications

* No user authentication; API is open to any client
* Store is handled as a string (not a separate entity)
* Product alerts and shopping lists are user-agnostic
* Price history is inferred from product re-imports rather than a dedicated table
* Duplicate filtering is based on extended equality checks in parsers

### Potential Improvements

* Add pagination and sorting for endpoints returning large datasets
* Use a dedicated `PriceHistory` entity for cleaner tracking and reporting
* Store entity normalization for better data consistency (e.g. store logo, location, etc.)
* Support CSV upload via REST endpoint instead of file system trigger
* Use Spring Profiles to separate dev/prod environments
* Optional authentication and API key access for user-based usage in the future
* Add Swagger/OpenAPI documentation
* Create DTOs to limit or restructure exposed JSON fields

---

## 🧪 Testing

Use Postman or curl to interact with the endpoints.

Example:

```bash
curl "http://localhost:8080/api/store-products/price-history?productName=lapte&store=LIDL"
```

---

## ✍️ Author

Raul Cucu — 2025
