from rest_framework import permissions

class IsOwnerOrReadOnly(permissions.BasePermission):
	
	def has_object_permission(self, request, view, obj):

		if request.method in permissions.SAFE_METHODS:
			return True
		try:
			return obj.author == request.user
		except AttributeError:
			try:
				return obj.admin == request.user
			except AttributeError:
				try:
					return obj.user == request.user
				except AttributeError:
					return False