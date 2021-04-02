<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
          name="viewport">
    <meta content="ie=edge" http-equiv="X-UA-Compatible">
    <title>Freemarker Sample Page</title>
    <link href="css/freemarker.css" rel="stylesheet">
</head>
<body>

<ul>
    <h1>Сотрудник!</h1>
    <ul>
        <#list customers as customer>
            <li>${customer.name} взял почитать ${customer.tookRead} прочитал ${customer.alreadyTook}</li>
        </#list>
    </ul>
</ul>

</body>
</html>