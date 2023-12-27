# Generated by Django 5.0 on 2023-12-12 16:59

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('StudentDataBank', '0003_student_semester'),
    ]

    operations = [
        migrations.CreateModel(
            name='Contacts',
            fields=[
                ('contact_id', models.AutoField(primary_key=True, serialize=False)),
                ('name', models.CharField(max_length=50)),
                ('surname', models.CharField(max_length=50)),
                ('phone_number', models.CharField(max_length=25)),
            ],
        ),
        migrations.DeleteModel(
            name='Student',
        ),
    ]
