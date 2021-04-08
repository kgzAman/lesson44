<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Login form</title>
    <link rel="stylesheet" href="css/forms.css">
</head>

<body>
   <h1> <form action="/login" method="post">
        <fieldset>
            <div class="legend">
                <p>Welcome!</p>
                <img src="/images/1.jpg" alt="coins" style="display:block; width: 200px; height: 200px;">
            </div>
            <div class="form-element">
                <label for="user-email">email</label>
                <input type="email" name="email" id="user-email" placeholder="your email" required autofocus>
            </div>
            <div class="form-element">
                <label for="login">login</label>
                <input type="input" name="login" id="login" placeholder="your login" required>
            </div>
            <div class="form-element">
                <label for="password">password</label>
                <input type="password" name="password" id="password" placeholder="your password" required>
            </div>
            <div class="hr-line">
                <span class="details">one more step to go</span>
            </div>
            <div class="form-element">
                <button class="register-button" type="submit">Login!</button>
            </div>
        </fieldset>
    </form></h1>
</main>
</body>

</html>
