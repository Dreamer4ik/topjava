<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <style>
        .normal {
            color: green;
        }
        .excess {
            color: red;
        }
    </style>
    <title>Meals</title>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <a href="meal?action = create"> Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">

        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr class="${meal.excess ? "excess" : "normal"}">
                <td>${meal.formattedDateTime}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action = update&id=${meal.id}">Update</a> </td>
                <td><a href="meal?action = delete&id =${meal.id}">Delete</a> </td>

            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>