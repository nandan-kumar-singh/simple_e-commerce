# simple-e-commerce
Kotlin language was used in the project, the project is a simple e-commerce application. In the first login section, you can enter the data from the fake API correctly and log in or continue in guest mode and reach the main page.
Login information;

username: ali1234

password: 123456789

username: atatürk1234

password: 123456789

User data is received through retrofit and when entered correctly, it is saved in the sharedpreferences database, so the entered user is transferred to the home page without logging in again. The sign up section is non-functional and consists of only images. 
The products are again pulled from the fake API and listed, you can add the products to the cart or favorites, Sqlite is used to add to the favorites and cart. The products added to the cart do not have a purchasing function, the basket total and the increase of the purchased products are available.

# API
**Products API :** https://fakestoreapi.com/products

**User API :** https://mocki.io/v1/1dfae9b9-3d74-400b-9706-c2f4ca3cb79d

# Implemention
**Navigation**

def nav_version = "2.5.3"

implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")

implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

**Retrofit**

implementation 'com.squareup.retrofit2:retrofit:(insert latest version)'

implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

**Glide**

implementation 'com.github.bumptech.glide:glide:4.14.2'

**Circle Image View**

implementation 'de.hdodenhof:circleimageview:3.1.0'

# Used In The Project

**-Glide**

**-RecyclerView**

**-Retrofit2**

**-Sqlite**

**-Shared Preferences**

**-Fragment-Navigation**

**-Activity**

**-Circle Image View**

**-Bottom Navigation**

# Project View

![1696194385077](https://github.com/epsilons42/simple_e-commerce/assets/120338137/679ee3cf-a76a-4734-bf9a-6f6179346dff)

![1696194409132](https://github.com/epsilons42/simple_e-commerce/assets/120338137/a2a03b17-3f34-4155-8f4a-97d54e388777)

![1696194427325](https://github.com/epsilons42/simple_e-commerce/assets/120338137/bd585085-f534-464a-90bb-b96f33e23c61)

![1696194441465](https://github.com/epsilons42/simple_e-commerce/assets/120338137/25e81a8f-36e1-41b7-b1bd-3f18881d6e9b)

![1696194463187](https://github.com/epsilons42/simple_e-commerce/assets/120338137/4c694363-e6df-4423-ad8e-cc443eed3f0f)

![1696194514750](https://github.com/epsilons42/simple_e-commerce/assets/120338137/e295b918-0ba7-4d22-9c66-e225aff9fc41)

![1696194478701](https://github.com/epsilons42/simple_e-commerce/assets/120338137/cb9ee607-901e-4bf0-bddb-3f99a80d0872)
