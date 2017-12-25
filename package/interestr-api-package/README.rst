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

  path('api/', include('api.urls')),

3. Run `python manage.py migrate` to create the api models.

4. Start the development server with `python manage.py runserver`.

5. Visit http://127.0.0.1:8000/admin/ to check if everything works fine.


See the Project Deployment document for information about deploying the
api project into the whole application.