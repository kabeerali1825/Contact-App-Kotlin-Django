#from django.shortcuts import render

# Create your views here.
'''
from django.http import HttpResponse
from .models import Student
from django.core import serializers

def index(request):
    return HttpResponse("Welcome to Mobile App Development with BITF20")

def allStudents(request):
    data = serializers.serialize('json', Student.objects.all())
    return HttpResponse(data)

def delStudent(request):
    Student.objects.filter(roll_number=request.GET['roll_number']).delete()
    return HttpResponse("record of "+request.GET['roll_number']+" is deleted, if exist already!")

def addStudent(request):
    Student(
        roll_number = request.GET['roll_number'],
        cgpa = float(request.GET['cgpa']),
        gender = bool(request.GET['gender']),
        semester = int(request.GET['semester'])
        ).save()
    return HttpResponse("record of "+request.GET['roll_number']+" is added, if does not exist already!") '''

from django.http import HttpResponse
from .models import Contacts
from django.core import serializers

def index(request):
    return HttpResponse("Welcome to my Contact App!")

def all_contacts(request):
    data = serializers.serialize('json', Contacts.objects.all())
    return HttpResponse(data)



def add_contact(request):
    name = request.GET.get('name')

    phone_number = request.GET.get('phone_number')

    if name  and phone_number:
        contact = Contacts(name=name, phone_number=phone_number)
        contact.save()
        return HttpResponse(f"Contact {name}added successfully.")
    else:
        return HttpResponse("Name and phone number are required fields.")


def update_contact(request, contact_id):
    try:
        contact = Contacts.objects.get(pk=contact_id)
        
        # Update contact details based on the provided data
        name = request.GET.get('name')
        phone_number = request.GET.get('phone_number')

        if name and phone_number:
            contact.name = name
            contact.phone_number = phone_number
            contact.save()
            return HttpResponse(f"Contact with ID {contact_id} updated successfully.")
        else:
            return HttpResponse("Name, and phone number are required fields for update.")
    except contact.DoesNotExist:
        return HttpResponse(f"No contact found with ID {contact_id}.")


def delete_contact1(request, phone_number):
    try:
        contacts = Contacts.objects.filter(phone_number=phone_number)
        
        if contacts.exists():
            contacts.delete()
            return HttpResponse(f"All contacts with phone number {phone_number} deleted successfully.")
        else:
            return HttpResponse(f"No contacts found with phone number {phone_number}.")
        
    except contacts.ObjectDoesNotExist:
        return HttpResponse(f"An error occurred while deleting contacts with phone number {phone_number}.")

from django.shortcuts import get_object_or_404

# def add_contact1(request, name, phone_number):
#     if name and phone_number:
#         contact = Contacts(name=name, phone_number=phone_number)
#         contact.save()

#         # Use filter instead of get to handle multiple objects
#         contacts = Contacts.objects.filter(phone_number=phone_number)

#         # Check if any contacts were found
#         if contacts.exists():
#             for contact_obj in contacts:
#                 print(contact_obj.phone_number, contact_obj.name)
#             return HttpResponse(f"Contact {name} added successfully.")
#         else:
#             return HttpResponse("Contact not found.")  # Handle case when no contact is found
#     else:
#         return HttpResponse("Name and phone number are required fields for adding a contact.")
def add_contact1(request, name, phone_number):
    if len(phone_number) == 11:  # Check if phone number is of length 11
        existing_contact = Contacts.objects.filter(phone_number=phone_number)
        
        if existing_contact.exists():
            return HttpResponse("Contact with this phone number already exists.")
        else:
            if name:
                contact = Contacts(name=name, phone_number=phone_number)
                contact.save()
                return HttpResponse(f"Contact {name} added successfully.")
            else:
                return HttpResponse("Name is required for adding a contact.")
    else:
        return HttpResponse("Phone number must be 11 digits long.")
def update_contact1(request, old_phone, name, new_phone):
    try:
        contact = Contacts.objects.get(phone_number=old_phone)
        
        if name and new_phone:
            contact.name = name
            contact.phone_number = new_phone
            contact.save()
            return HttpResponse(f"Contact with old phone number {old_phone} updated successfully.")
        else:
            return HttpResponse("Name, surname, and phone number are required fields for updating a contact.")
        
    except Contacts.DoesNotExist:
        return HttpResponse(f"No contact found with old phone number {old_phone}.")
def delete_all_contacts(request):
    try:
        Contacts.objects.all().delete()
        return HttpResponse("All contacts deleted successfully")
    except Exception as e:
        return HttpResponse(f"Error occurred: {str(e)}")