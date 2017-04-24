from django.db import models


class Comment(models.Model):
	author = models.ForeignKey('auth.User', related_name='comment', on_delete=models.CASCADE)
	text = models.TextField()
	created = models.DateTimeField(auto_now_add=True)

    class Meta:
    	ordering = ('created',)
