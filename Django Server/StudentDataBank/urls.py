'''
Created on 20-Nov-2023

@author: mianm
'''
'''
from django.urls import path

from . import views

urlpatterns = [
    path("", views.index, name="index"),
    path("allStudents", views.allStudents, name="allStudents"),
    path("delStudent", views.delStudent, name="delStudent"),
    path("addStudent", views.addStudent, name="addStudent"),
] '''

from django.urls import path
from . import views
urlpatterns = [
    path("", views.index, name="index"),
   
   # path("addContact/", views.add_contact, name="add_contact"),
    #path("update_contact/<int:contact_id>/", views.update_contact, name="update_contact"),
    #different Kind of URLS
    path("allContacts/", views.all_contacts, name="all_contacts"),
    path("deleteContact/<str:phone_number>/", views.delete_contact1, name="delete_contact"),
    path("addContact/<str:name>/<str:phone_number>/", views.add_contact1, name="add_contact"),
    path("updateContact/<str:old_phone>/<str:name>/<str:new_phone>/", views.update_contact1, name="update_contact1"),
    path("deleteallContact/", views.delete_all_contacts, name="delete_contact"),
]



