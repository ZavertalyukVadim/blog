<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="category" scope="request" type="blog.domain.Category"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${category.name}</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="../resources/css/style.css">
</head>
<body>

<div class="panel panel-default ">
    <div class="panel-heading container">
        Category: ${category.name}
        <input type="submit" value="Home" style="margin-left:80%" onclick="window.location='/'"><br>
    </div>
    <br>
    <c:forEach items="${category.posts}" var="post">
        <div class="container panel panel-default">
            <div class="panel-heading">
                <h2><a href="/post/${post.id}"> ${post.title}</a></h2>
            </div>
            <div class="panel panel-body">
                <form action="/category/${post.category.id}" method="get">
                    <button type="submit" class="btn btn-primary btn-xs">${post.category.name}</button>
                </form>
                <c:forEach items="${post.tags}" var="tag">
                    <div class="btn-group" role="group" aria-label="...">
                        <form action="/tag/${tag.id}" method="get">
                            <button type="submit" class="btn btn-info btn-xs">${tag.name}</button>
                        </form>
                    </div>
                </c:forEach>
                <br>
                <br>
                <blockquote>
                    <p class="lead">${post.description}</p>
                </blockquote>
            </div>
        </div>
    </c:forEach>

</div>
</body>
</html>
