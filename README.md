# MYRETAIL APP

My Retail App is a Spring Boot application configured to run at http://localhost:8181/myretail/  to get Product Details and to update Price.

MongoDB is used to Store the product information(productId, title,price and currency code) in product Collection.

# Sample Mongo Query to insert documents:
db.getCollection('Product').save({ "pid": 13860428, "title": "The Big Lebowski (Blu-ray)", "price": "69.87", "currencyCode": "USD"  })

**Myretail hosts two REST services:**

** 1) GET /products/{productId}:**
   
  This Rest Service aggregates price information from MongoDB and product Title from external Target API and
  provides a JSON Response.
  
  **Sample:**
  
  **Request:** 
  GET   http://localhost:8181/myretail/products/13860428
  Content-Type: application/json
  
  **Response:**
  {
    "id": 13860428,
    "name": "The Big Lebowski (Blu-ray)",
    "current_price": {
        "value": "100.0",
        "currency_code": "USD"
    }
}
  
** 2) PUT /products/{productid}:**
  
  This Rest Service is used to update the price of an existing product in MongoDB

  ** Sample:**
  PUT http://localhost:8181/myretail/products/13860428
  Content-Type: application/json
  
  **Request: **
   { "id": 13860428, "name": "The Big Lebowski (Blu-ray)", "current_price": { "value": "100.00", "currency_code": "USD" } }
   
   ** Response:**
   200 OK
