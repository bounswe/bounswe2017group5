from django.conf.urls import url, include

from . import views


app_name = 'website'

urlpatterns = [
    #views ayarlaninca buraya ekle.

    url(r'^login/$', views.UserLoginView.as_view(), name='login'),
    url(r'^register/$', views.UserRegisterView.as_view(), name='register'),
    url(r'^groups/$', views.GroupView.as_view(), name='groups'),



    #url(r'^$'),
]
