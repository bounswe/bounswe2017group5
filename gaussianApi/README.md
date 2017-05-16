# Documentation
In this project, we use Django Rest framework. It is a powerful and flexible toolkit for building Web APIs.
  * ## Installation

Install Django and Rest framework
```shell
$ pip install django
$ pip install djangorestframework
```

Create and activate a virtual environment.
```shell
$ python3 -m venv gauss
$ source gauss/bin/activate
```

Create a project
```shell
$ django-admin startproject apiProject
```

Create the api apps
```shell
$ python manage.py startapp apiApp
```

Create an admin user
```shell
$ python manage.py createsuperuser
$ Username: admin
$ Password: pass12345
```

Create sqlite database.
```shell
$ python manage.py makemigrations
$ python manage.py migrate

```

The development server: to verify your Django project works.
```shell
$ python manage.py runserver

```
Try to show
```shell
http://127.0.0.1:8000/apiApp/
http://127.0.0.1:8000/admin/
```

[Tutorial](https://docs.djangoproject.com/en/1.11/intro/tutorial01/)

## Implementation of Api and Test

### Models of Interest Centric Community project
In this project, we created api and test of the project as multiple models. These models are
  * Post
  * Tag
  * Profile
  * Group
  * Comment
Both of them have parameters which are
<ol>
  <li><b>Post</b>: author, text, created, group</li>
  <li><b>Tag</b>: name, created</li>
  <li><b>Profile</b>: user, name, surname, created</li>
  <li><b>Group</b>: name, admin, members, created, isPublic, description, location_lat, location_lon</li>
  <li><b>Comment</b>: text, author, post, created</li>
</ol>

<br>

In this model, one of the class has many-to-many relationship such as <i>members parameter in Group class</i>, and one of them has one-to-one relationship such as <i>user parameter in Profile class</i>
<br>

### Structure of Tests 
In the test scenario, we have multiple class for each of our models such as 
  * LogInRegisterTests
  * UserTests
  * GroupTests
  * CommentTests
  * PostTests
  
These classes check different functionalities.
<ol>
  <li><b>LogInRegisterTests: </b> In this test class, we have <i>set up, and login</i> to test <i>register, login, login wrong credentials, and login required attribute</i></li>
  <li><b>UserTests: </b> In this test class, we have <i>set up</i> to test <i>get user(s), and edit a profile</i></li>
  <li><b>GroupTests: </b> In this test class, we have <i>set up</i> to test <i>get group(s), and create a group, edit a group, and remove a group</i></li>
  <li><b>CommentTests: </b> In this test class, we have <i>set up, add a comment</i> to test <i>create a comment, get comment(s) and delete a comment</i></li>
  <li><b>PostTests: </b> In this test class, we have <i>set up</i> to test <i>create a post, remove a post, and get post(s)</i></li>
</ol>

<br>
In the test scenarios, we have both public and private <i>Group, Post, and Comment</i>

## One of scenario of the system
You can show the project on [PythonAnywhere](http://swegroup5.pythonanywhere.com/). I give an example of the system. 
First of all, go to http://swegroup5.pythonanywhere.com/. <br>
Let's assume that you want to show <i>Users</i>. <br>
You need to type http://swegroup5.pythonanywhere.com/users/. <br>
You need to write <i>username: admin and password: **********</i>. Then you can show <i> User List</i>

```shell
GET /users/
```
```shell
HTTP 200 OK
Allow: GET, POST, HEAD, OPTIONS
Content-Type: application/json
Vary: Accept

{
    "count": 1,
    "next": null,
    "previous": null,
    "results": [
        {
            "id": 1,
            "username": "admin",
            "profile": null,
            "comments": [
                {
                    "id": 1,
                    "text": "sdfgasdfdfgdf",
                    "author": "admin",
                    "post": 1,
                    "created": "2017-05-15T19:39:57.919716Z"
                }
            ],
            "posts": [
                {
                    "id": 1,
                    "text": "qwertyertyuertywerw",
                    "author": "admin",
                    "group": 1
                }
            ],
            "owned_groups": []
        }
    ]
}
```
There is only one user who is 'admin', so his id is equal to 1. He has a comment, and post. You can show that what are the parameters of comments, posts models.
