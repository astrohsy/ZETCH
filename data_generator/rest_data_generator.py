import requests
import random
import json

URL = "http://localhost:8080/users/"

person_name_set = [
    ""
]

def generate_users():
    name_list = json.load(open('./names.json'))['data']
    random_name = random.choice(name_list)
    print(random_name)
    requests.post(URL,
                json={
                    "username": random_name.lower(),
                    "name": random_name,
                    "email": f"{random_name}@me.com"
                },
                headers={"Content-Type": "application/json"})
    
generate_users()