=============
Interestr API
=============

This package includes the API of Interestr project. This package includes
models that website app uses. So install this before the website.

Quick Start
-----------

1. Add 'api' to your INSTALLED_APPS setting like shown below::

  INSTALLED_APPS = [
    ...
    'api',
  ]

2. Include the api URLconf in your project urls.py file::

  from rest_framework.documentation import include_docs_urls

  path('api/v1/docs/', include_docs_urls(title='Interestr API')),
  path('api/', include('api.urls')),

3. Run `python manage.py migrate` to create the api models.

4. Start the development server with `python manage.py runserver`.

5. Visit http://127.0.0.1:8000/admin/ to check if everything works fine.



API exposes information through /api/v1/* route. For more information 
about the endpoints, please check out the API documentation provided
via the package itself. After you run the server, visit
http://127.0.0.1:8000/api/v1/docs/ for the documentation.


See the Project Deployment document for more information about 
deploying the project.