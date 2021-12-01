# book-class
API to create classes and book that class

github: https://github.com/kristianfantin/book-class

In order to see the created Stories, you can view the Project Kanban here:
- https://github.com/users/kristianfantin/projects/3/views/1

Running the Application: mvn spring-boot:run
- Swagger is configured in order to Document the Rest Api: 
  - http://localhost:8080 (access th url after run the application)

- Necessary: docker
  - run REDIS
    - docker run -it --name redis -p 6379:6379 redis:5.0.3 
