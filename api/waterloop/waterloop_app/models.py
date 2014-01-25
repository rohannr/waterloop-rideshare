from django.db import models

import json

class User(models.Model):
    fbId = models.IntegerField(blank=False, null=False, unique=True)
    name = models.CharField(blank=False, null=False, max_length=100)

    def __unicode__(self):
        return json.dumps({'fbId' : self.fbId, 'name' : self.name})

class Ride(models.Model):
    origin = models.CharField(blank=False, null=False, max_length=50)
    destination = models.CharField(blank=False, null=False, max_length=50)
    datetime = models.DateTimeField(blank=False, null=False)
    driver = models.ForeignKey(User, related_name='driver')
    passengers = models.ManyToManyField(User, related_name='passengers')
    numSeats = models.IntegerField(blank=False, null=False)
    price = models.IntegerField(blank=False, null=False)
    link = models.CharField(max_length=255)
