from django.conf.urls import include, url
from django.contrib import admin
from rest_framework.documentation import include_docs_urls

urlpatterns = [
    url(r'^', include('apiApp.urls')),
    url(r'^docs/', include_docs_urls(title="SWEGroup5 API")),
    url(r'^admin/', admin.site.urls),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
]