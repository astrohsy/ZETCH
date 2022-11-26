# Web Frontend for ZETCH

## Build and Run
**Important:** See a ZETCH team member for a `.env` file specifying secret values, place the file
in `.`.

```bash
npm install
npm start # soon after this command, web app will be running on localhost:3000
```

## ETC
### Valid Users
| Username | Password |
| --- | --- |
| bob | 123456 |
| cat | 123456 |
| amy | 123456 |


# Manual Tests: 
(perform these tests after running npm start, perform tests in order)
do: access localhost:3000

expect: "login" button is visible


do: click on "login" button

expect: amazon cognito login screen


do: login with username=bob, password=123456

expect:
* redirect back to museum manager app
* page shows select museum dropdown
* competitors(count=1): Fireball Restaurant
    * Content: Caribbean, Address: 3920 Broadway, Average Rating: 5.0, Ratings Histogram - 1 5-rating
    * Reviews(count=1): Comment: Most authentic Jamaican food in Manhattan., Rating: 5, User: Bob R.
    * View Replies button: onclick => replies(count=1), Glad you enjoyed your meal!


do: select "Columbia Museum" in the "select museum" dropdown

expect:
* Museum: Columbia Museum, Located on the campus of Columbia University, this museum plays a major role in developing and collecting modern art, and is often identified as one of the largest and most influential museums of modern art in the world., Address: 569th W 120 St, Average Rating: 4.1, Ratings Histogram: 7 5-ratings, 4 4-ratings, 2 3-ratings, 2 2-ratings.
* "Edit" Button visible
    * Reviews(count=15)


do: click on "Edit" button

expect: dialog popup with two inputs that show the museum information.


do: add "-edit" to both inputs, click "confirm"

expect:
* dialog disappears, content is changed to: Located on the campus of Columbia University, this museum plays a major role in developing and collecting modern art, and is often identified as one of the largest and most influential museums of modern art in the world.-edit, Address: 569th W 120 St-edit


do: click on first "View Replies" button in the reviews

expect: 
* dialog showing current replies
* "Add Reply" button


do: click on "Add Reply" button

expect: dialog showing 1 input for a "reply"


do: input "new reply" in the input field, click on confirm

expect: return to "replies" dialog, the "new reply" is visible in the replies list







