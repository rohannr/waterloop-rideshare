from django.db import models

class User(models.Model):
    fbId = models.IntegerField()
    name = models.CharField(max_length=100)

class Ride(models.Model):
    source = models.CharField(max_length=50)
    dest = models.CharField(max_length=50)
    datetime = models.DateTimeField()
    drive = models.ForeignKey(User, related_name='driver')
    passengers = models.ManyToManyField(User, related_name='passengers')
    numSeats = models.IntegerField()
    price = models.IntegerField()
