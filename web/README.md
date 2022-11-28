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

| Test Name | Test Steps | Expected Results |
| --- | --- | --- |
| Initial Webpage | access localhost:3000 | 'login' button is visible, example: <img width="625" alt="image" src="https://user-images.githubusercontent.com/16641040/204167223-95f64564-9a58-4e48-8125-1e6fe7a05ad2.png"> |
| Login | <ul><li>click on "login" button</li><li>login with username=bob, password=123456</li></ul> | login screen: <img width="387" alt="image" src="https://user-images.githubusercontent.com/16641040/204167349-18739579-3447-403c-b240-ade55fd91c5b.png"> <br/> logged-in contents: ![image](https://user-images.githubusercontent.com/16641040/204167417-8f708c39-f792-481a-8aec-3459a1d523a3.png) (key points: select museum input at top, total of 2 competitor museums) |
| View own museums | Click on "select museum" | 4 available museums to select from: <img width="971" alt="image" src="https://user-images.githubusercontent.com/16641040/204167683-f3ccfcf9-46b0-4691-8af8-536c9a0f33ef.png"> |
| Select museum | Select "columbia museum" within "select museum" | resulting webpage: ![image](https://user-images.githubusercontent.com/16641040/204167783-089174e0-c4bb-4f98-9e4a-e852fe23c2f6.png) (key points: Columbia Museum information is now visible, "Edit" button is visible for Columbia Museum) |
| Edit museum information | <ul><li>Click on "Edit" button</li><li>for both `description` and `address` append `-edit` to the end of the existing text</li><li>Click `confirm`</li></ul> | Dialog popup after clicking "edit": <img width="252" alt="image" src="https://user-images.githubusercontent.com/16641040/204167911-0d5c9e51-43bf-45ca-9c6b-1197c2ab5607.png"><br/> Dialog after editing text inputs: <img width="255" alt="image" src="https://user-images.githubusercontent.com/16641040/204167961-161e83e9-06fa-485a-988a-f38edf90983c.png"> <br/> Museum information is updated after clicking `confirm` <img width="957" alt="image" src="https://user-images.githubusercontent.com/16641040/204168001-11fabf46-5b25-4a9d-8592-c1964ccb8c36.png"> |
| Edit museum information cleanup | Use same steps as above edit museum information to remove the appended `-edit` from the text fields | Museum information returned to original values: <img width="976" alt="image" src="https://user-images.githubusercontent.com/16641040/204168191-aa9b1bef-8019-4bf5-8754-41ca385b9e91.png"> |
| View Replies | Click on first "View Replies" button in the "Columbia Museum" reviews | Replies dialog popup: <img width="196" alt="image" src="https://user-images.githubusercontent.com/16641040/204168341-24e0e2c2-5f7c-499c-944d-f688d459bdbd.png"> |
| Post new reply | <ul><li>Click on "Add Reply" button in the "Replies" dialog"</li><li>Input `New reply!` in the "Post Reply" text field</li><li>Click the "Confirm" Button</li></ul> | Initial replies dialog popup: <img width="197" alt="image" src="https://user-images.githubusercontent.com/16641040/204168564-15838f64-0391-42d9-97c8-a2bca62e0ba5.png"><br/> Dialog popup after clicking "Add Reply": <img width="256" alt="image" src="https://user-images.githubusercontent.com/16641040/204168588-e82c65ac-9713-4636-b3d4-097f81d5b310.png"> <br/> "Post Reply" dialog after text inputs: <img width="257" alt="image" src="https://user-images.githubusercontent.com/16641040/204168617-9f5bda7a-370e-4cab-aac7-cda94f7ab758.png"><br/> Updated "Replies" dialog after clicking "Confirm": <img width="291" alt="image" src="https://user-images.githubusercontent.com/16641040/204168670-287136bb-f3b6-4acd-8c12-77ebee68d7f4.png"> |
| Delete reply | Click on "Delete Reply" button for the newly created reply | Newly created reply is deleted: <img width="198" alt="image" src="https://user-images.githubusercontent.com/16641040/204168733-adef9571-36e7-4a07-a727-4b3cd138601e.png"> |
| Validate cannot reply to other museum's reviews | Click on the first "View Replies" button for Harlen Portrait museum | Replies dialog does not have an "Add Reply" button: <img width="182" alt="image" src="https://user-images.githubusercontent.com/16641040/204168839-ebe90c07-9b15-4686-aa4c-b409cb702b59.png"> |
| Logout | Click on "Logout" button at the bottom of the screen | Return to initial screen with login button: <img width="619" alt="image" src="https://user-images.githubusercontent.com/16641040/204168998-352fb03c-78a9-44b1-93f0-a3c355ca3b9a.png">|
| Login as different user | Use previous `login` step, but use username=cat, password=123456 | See different competitors for new user: ![image](https://user-images.githubusercontent.com/16641040/204169147-19f519d4-c58e-410c-b9be-352b3b1120f1.png)|
| Check different user's museums | Click on "Select museum" button | See list of 2 museums: <img width="743" alt="image" src="https://user-images.githubusercontent.com/16641040/204169214-d3bec960-5eda-439b-8a95-25e6e7dccc07.png"> |





