<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Калькулятор</title>
    <!-- CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">

        <div class="row">
            <div class="panel panel-default col-md-5">
                <div class="panel-body">
                    <form name="calc" id="calc" method="post" action="<c:url value="/calculator"/>">

                        <div id="last">
                            <div class="panel panel-default">
                                <div class="panel-body lastDigit">${digit}</div>
                            </div>

                            <div class="panel panel-default">
                                <div class="panel-body lastOper">${mathaction}</div>
                            </div>
                        </div>
                        <div class="form-group" id="digitInput">
                            <label for="digit">Введите число</label>
                            <input type="text" class="form-control" id="digit" name="digit">
                            <div class="error error-message"></div>
                        </div>
                    <div class="row">
                        <input type="submit" value="+" name="mathaction" class="btn btn-primary col-md-4 col-xs-4 math">
                        <input type="submit" value="-" name="mathaction" class="btn btn-primary col-md-4 col-xs-4 math">
                        <input type="submit" value="*" name="mathaction" class="btn btn-primary col-md-4 col-xs-4 math">
                        <input type="submit" value="/" name="mathaction" class="btn btn-primary col-md-4 col-xs-4 math">
                        <input type="submit" value="=" name="mathaction" class="btn btn-primary col-md-4 col-xs-4 math">
                    <a href="/history" class="btn btn-primary col-md-12 col-xs-12">История вычислений</a>    
                    </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.15.1/jquery.validate.js"></script>

    <script>
        var     form = $('#calc'),
                digit = $('#digit'),
                isError = true
        $(document).ready(function () {
            jQuery.validator.addMethod("numbers", function(value, element) {
                return this.optional(element) || /^-?\d+([.,]\d+)?/.test(value);
            }, "\<p class='error'>Допустимы только числа!</p>");

            form.validate({
                rules: {
                    digit: {
                        required: true,
                        numbers: {}
                    }
                },

                messages: {
                    digit: {
                        required: "\<p class='error'>Введите хотя бы одну цифру</p>",
                        number: "\<p class='error'>Допустимы только цифры!</p>"
                    }
                },
                validClass: "success"
            });
        });

    </script>
</body>
</html>
