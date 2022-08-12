# JourneyJournal-App



JourneyJournal App is a project done while studying Android studio by Atish Ojha. This application simply helps to create a journal of any journey that a user wants. Here I have used Java as programing language and google firebase as the database of the application.

This is Android based application.


## Screenshots

### Splash Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184328296-7cf211e9-aba8-4e36-ab7c-32c1a372dceb.jpg">

Opening the application user will be greeted with this screen. This screen contains logo of the application which remains for 2-5 sec depending on user’s internet. As I have used firebase database to store all the information of the application. So internet is necessary to run application.

---

### SignIn Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184329508-c773f100-961e-440f-b27f-e25c19f49053.jpg">

After few seconds on splash screen user is directed sign in page. Here if user has created their account then they have to provide required information to sign in to application, if not than have to click on “New User | Sign Up!” to create a new account. User have an account but forgot password they can reset it by clicking on “Forgot Password?”
Data of user are saved on firebase and login system is also handled by authentication system of firebase.

---

### Forgot Password Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331198-0a099007-323b-4ca3-b42f-2928ce705d76.jpg">

When User clicked on “Forgot Password?” in sign in page they are redirected to this page. If user needs to reset their password they have just to provide registered email and click on reset password button. If email is registered in application than user get an email in same email address sand can reset password from there. If email is incorrect then an error message is shown to user. By clicking on “Go Back” user is redirected to sign in page.

---

### Sign Up Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331208-d436fe6a-1dc9-4891-a683-a59e9e5e89a5.jpg">

When User clicked on “New User | Sign Up!” in sign in page they are redirected to this page. Here user needs to provide required information and a unique email to create an account.
Providing data and clicking on Sign Up button system checks information and if everything is valid then user is redirected to login page where usher should provide required information to sign in.

---

### Home Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331196-b953c1af-ff42-4492-a24a-222581b1b033.jpg">

After giving correct credentials user is redirect to this page with a toast message. This page contains added journals by logged in users preview with image, posted date and title of journal. In the bottom right there is plus icon to add new journal and in top left there is a navigation menu icon. Just in right side of it user can see app name “JourneyJournal”.  Journal are shown in grid view of 2. They are shown by using recycle view.

---

### Navigation Drawer Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331205-1d2c488f-00dc-434a-a62f-0ddbb95c3664.jpg">

After giving correct credentials user is redirect to this page with a toast message. This page contains added journals by logged in users preview with image, posted date and title of journal. In the bottom right there is plus icon to add new journal and in top left there is a navigation menu icon. Just in right side of it user can see app name “JourneyJournal”.  Journal are shown in grid view of 2. They are shown by using recycle view.

---

### Add New Journal Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331192-5115661c-0200-4aee-918b-ff8d4e73c1ff.jpg">

After clicking plus icon at home or add new journal option from navigation drawer user is promoted to this page. Here user should give required information, select image from gallery by clicking image icon and choose a location from map icon user can add new journal to the database. Selecting image is necessary to add journal to database. After successful user is redirected to home page with toast message where they can see currently added journal.

---

### Gallery Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331185-9ce92c0a-daed-40a3-995e-85973099dde4.jpg">

By clicking Gallery from navigation drawer menu user is promoted to this page where user can get a look towards images that they have added to database.

---

### Graph Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331180-a057b23e-2a3e-40a4-b682-e113fcdbfb16.jpg">

By clicking Graph from navigation drawer menu user is promoted to this page where user can get a look towards date of the journal added with date when they were added.

---

### Map Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331178-e9ff9b86-d199-4cc0-9e3a-ba21ffafd316.jpg">

By clicking Map from navigation drawer menu user is promoted to this page where user can see their current location with blue dot in map and can search for places using search. This will help users to find out locations.

---

### Recycle view menu
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331172-77565c4b-c76a-4fa0-bcc7-6506764ae9ba.jpg">

In Home page user can get menu with each grid. By clicking menu they get options like edit and delete. By using this user can edit or delete journal without going to detail page.

---

### Journal Detail Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331163-72c996db-6708-4159-be5a-fa3274249afa.jpg">

After clicking any of the grid from home user is redirect with detail page of that journal. In this page user can have a detail view to what they have added on that particular journal. Here user get options to edit, delete and share the journal.

---

### Edit Journal Activity
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331168-f46a7a43-0930-4eb8-8a67-84b2234411c0.jpg">

By clicking on edit icon from detail page or clicking edit option from home page user is redirected to this page. Here user can add or remove required information and click update journal to update journal.

---

### Delete Journal
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331161-0e301feb-ad53-4b48-8097-d1ce19af7621.jpg">

By clicking on delete icon from detail page user get an alert dialog saying are you sure if user click ok then the journal is deleted from database and redirected to home page. If not than alert dialog disappear. 

---

### Share Journal
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331158-dc54f0e9-0125-4cf0-8e44-771d8f607937.jpg">

By clicking on delete icon from detail page user get an alert dialog saying are you sure if user click ok then the journal is deleted from database and redirected to home page. If not than alert dialog disappear. 

---

### Sign Out
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331153-fb22464e-35f3-4beb-9dca-4a941331c23a.jpg">

By clicking Sign Out from navigation drawer menu user is promoted to an alert dialog saying are you sure if user select ok then user get signed out from the application.

---

### After Sign Out Sucessful
<img width="325" height="700" src="https://user-images.githubusercontent.com/78890102/184331141-d9a38f79-858c-4f21-9179-62fdefdf5331.jpg">

After user sign out is successful they are directed to login page with a toast message. If user wants to exit application they can click the cross icon at Top right of the application which will allow user to exit application.

---

