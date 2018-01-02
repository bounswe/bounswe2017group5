from rest_framework.pagination import LimitOffsetPagination
from rest_framework.pagination import PageNumberPagination


class GroupLimitOffsetPagination(LimitOffsetPagination):
    default_limit = 5
    max_limit = 100

class PostLimitOffsetPagination(LimitOffsetPagination):
    default_limit = 10
    max_limit = 100

class UserLimitOffsetPagination(LimitOffsetPagination):
    default_limit = 5
    max_limit = 100

class DataTemplateLimitOffSetPagination(LimitOffsetPagination):
    default_limit = 5
    max_limit = 100
