/**
 * Created by eljah32 on 9/15/2016.
 */

function validateForm() {
    var x = document.forms["myForm"]["text"].value;
    if (x == null || x == "") {
        alert("Name must be filled out");
        return false;
    }
}


