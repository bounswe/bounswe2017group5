from django.conf.urls import url, include

from . import views


app_name = 'website'

urlpatterns = [
    #views ayarlaninca buraya ekle.

    url(r'^login/$', views.UserLoginView.as_view(), name='login'),
    url(r'^register/$', views.UserRegisterView.as_view(), name='register'),
<<<<<<< HEAD
    url(r'^groups/$', views.GroupView.as_view(), name='groups'),


=======
>>>>>>> 5ccd37adc3ef131b15c55775ea0e4d06e9e13e50

    #url(r'^$'),
]
