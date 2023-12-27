'''from django.db import models

# Create your models here.

class Student(models.Model):
    roll_number = models.CharField(max_length=10,  primary_key=True)
    cgpa = models.FloatField()
    gender = models.BooleanField()
    semester = models.IntegerField(default=0)

#    def __str__(self):
#        return self.roll_number '''
from django.db import models

class Contacts(models.Model):
    contact_id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50)
    phone_number = models.CharField(max_length=25)

   # def __str__(self):
   #     return f"{self.name} {self.surname}"


    