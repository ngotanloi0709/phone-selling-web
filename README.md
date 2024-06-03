# __Introduction__
## __About__
This project is made as the Final Report for the course "Java Technology" at Ton Duc Thang University
## __Technique__
- Spring Web
- Spring JPA
- Spring Security
- Thymeleaf
- MySQL
## __Feature__
### General Features
- User authentication and authorization: Secure login and registration for both customers and administrators.
- Product catalog: Display a wide range of phone products with details like images, descriptions, specifications, prices, and availability.
- Search and filtering: Allow users to easily search for specific phone models or filter by brand, price range, features, etc.
- Shopping cart: Enable users to add products to their cart, modify quantities, and proceed to checkout.
- Order management: Track order history, view order status, and manage returns or cancellations.
- Payment gateway integration: Securely process payments through various payment methods (credit/debit cards, online banking, e-wallets).
- Customer reviews: Allow customers to leave reviews.
### Customer-Specific Features
- Account management: Manage personal information, shipping addresses, and payment methods.
- Wishlist: Create and manage a wishlist of desired products.
### Administrator-Specific Features
- Product management: Add, edit, or remove phone products from the catalog.
- Inventory management: Track stock levels and update product availability.
- Order fulfillment: Process and ship orders.
- User management: Manage customer accounts and permissions.
## __Contributors__
### Ngo Tan Loi (ngotanloi0709@gmail.com)
### Nguyen Minh Phuong (phuongit9902@gmail.com)
# __Installation__
This project requires below technologies to implement:
- IntelliJ 2023.2.1 (Optional)
- JDK 17
- MySQL 8.0.30
## __Setup__
- Clone this repository
- Open the repository through any IDE support Spring build
- Run MySQL and create database with name "phone-selling-web"
- As using IntelliJ, Run the project as default setting
- As running project through command line, type `mvn spring-boot:run`
- Go to http://localhost:8080/
