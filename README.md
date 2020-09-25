Benjamin Skovgaard - cph-bs190@cphbusiness.dk

# Flow2-Week1-projects

## Note
There is no Monday and Thursday folder because:
1) We did a course assignment review on Monday, so no projects there.
2) We continued working on the project from Wednesday on Thursday (rest_jax_rs project in the Wednesday folder).

ALSO:

There are 3 projects in the Tuesday folder because there initially was an exercise document on the Tuesday page on our course website. I already completed the exercises when I discovered they had been replaced with a different exercise in the form of several videos. 

I have decided to keep them in the folder because I made some long comments in them and they helped my understanding of JPA and JPQL (specifically understanding how to set up relations between entities).

The projects that I completed the previous exercises in are: 'JPA_Entity_Mappings' and 'JPA_Relations'.

## Tuesday
Worked on 1 project: 'jpademo'. This project was made with the provided exercise videos, where we basically coded along with our teacher (Jon). We were introduced to different types of relations (one to one, one to many etc.) and played around with persisting different entities with relations and ensuring that the created database tables were set up correctly.

#### Project status
- 'jpademo' = completed

## Wednesday
Worked on 1 project: 'rest_jax_rs'. After being introduced to relations with JPA, we started incorporating REST in a more fleshed out project to create a person API where we can add and modify person objects in our database through JPA. We used Postman to test our endpoints and ensure that the responses from our endpoints contained the correct JSON objects.

We continued working on this project on Thursday, adding ExceptionMappers to handle different errors that might occur and returning prettier, easy to understand error messages in the form of JSON objects (with the HTTP status code, e.g. 404 if you try to delete a person that doesn't exist in the database).

Also expanded the capabilities of the api by adding an Address entity and creating a many-to-one relation between Person and Address, meaning several people can have the same address. Also spent time finishing Rest Assured tests and testing the api with Postman to make sure all endpoints worked as intended.

#### Project status
- 'rest_jax_rs' = completed

## Friday
Worked on 1 project: 'Examprep-JPA'. This was an optional exercise which was based on a provided domain diagram containing some different tables, from which we had to set up entity classes and relations between them. We also set up various methods to test that the relations worked (e.g. adding an order to a customer and ensuring that the order in the database contains the customer id).
#### Project status
- 'Examprep-JPA' = completed
