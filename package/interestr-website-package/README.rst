=================
Interestr Website
=================

This package includes the website of Interestr project. Requires
`interestr-api` package to be installed.

Quick Start
-----------

1. Add 'website' to your INSTALLED_APPS setting as shown below::

  INSTALLED_APPS = [
    ...
    'api',
    'website',
  ]

2. Include the URLconf in your project urls.py file::

  path('', include('website.urls')),

3. Start the development server with `python manage.py runserver`.

4. Visit http://127.0.0.1:8000/ to check if everything works fine.



See the Project Deployment document for information about deploying 
the project.