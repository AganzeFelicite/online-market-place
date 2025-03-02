# Online Marketplace API

## Overview
This project is a comprehensive RESTful API for an online marketplace that enables users to buy and sell products, manage their inventory, and process orders. Built with Spring Boot and containerized with Docker, this application offers a scalable solution for e-commerce platforms.

## Features

### User Management
- User registration with email verification
- Authentication using JWT
- Role-based access control (Admin, Buyer, Seller)
- Profile management

### Product Management
- CRUD operations for products
- Product categorization
- Product tagging for improved searchability
- Featured product marking by admins

### Order Processing
- Order placement and tracking
- Order history viewing
- Asynchronous order processing using message queuing (RabbitMQ)
- Email notifications for order status updates

### Review System
- Product rating and reviewing
- Review management for purchased products only

### Admin Capabilities
- User management
- Product moderation
- Order oversight
- Category management

## Technology Stack

- Backend: Spring Boot
- Database: PostgreSQL
- Authentication: JWT (JSON Web Tokens)
- Containerization: Docker and Docker Compose
- Documentation: Swagger (OpenAPI 3.0)
- Message Queue: RabbitMQ
- Testing: JUnit, Mockito

## System Architecture
![App Screenshot](https://github.com/AganzeFelicite/online-market-place/blob/main/awesomity/UML.png)


## API Documentation

The API is fully documented using Swagger. After running the application, you can access the documentation at:
http://localhost:8080/swagger-ui/index.html

## Database Schema

The application uses a normalized database structure with the following key entities:
- User
- Product
- Category
- Order
- OrderItem
- Review
- Tag

## Installation and Setup

### Prerequisites
- Docker and Docker Compose

### Running the Application

1. Clone the repository
git clone https://github.com/AganzeFelicite/online-market-place.git
cd /awesomity

2. Start the application using Docker Compose
docker-compose up -d

3. The API will be available at http://localhost:8080

### Environment Variables
The application uses the following environment variables which can be configured in the .env file: for demo purposes i have not used .env i have created Testing accounts 



## Security Measures

- JWT-based authentication
- Password encryption
- Role-based access control
- Input validation and sanitization
- Protection against common web vulnerabilities (XSS, CSRF)

##  Future Enhancements / Missing Features

- Payment gateway integration
- Enhanced analytics for sellers
- Product recommendation system
- Mobile app integration using the API
- Multi-language support

## Troubleshooting

### Common Issues

1. Connection refused to database
   - Ensure PostgreSQL container is running: docker ps
   - Check logs: docker-compose logs db

2. JWT authentication issues
   - Verify your JWT token is valid and not expired
   - Ensure you're using the correct authorization header format

3. Email sending failures
   - Check email configuration in .env file
   - Verify email service logs: docker-compose logs email-service

## Contributing

1. Fork the repository
2. Create your feature branch (git checkout -b feature/amazing-feature)
3. Commit your changes (git commit -m 'Add some amazing feature')
4. Push to the branch (git push origin feature/amazing-feature)
5. Open a Pull Request


