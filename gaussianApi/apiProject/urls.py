from django.conf.urls import include, url
from django.contrib import admin

urlpatterns = [
    url(r'^apiApp/', include('apiApp.urls')),
    url(r'^admin/', admin.site.urls),
]