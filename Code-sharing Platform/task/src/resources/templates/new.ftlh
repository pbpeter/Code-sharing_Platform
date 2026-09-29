<!DOCTYPE html>
<html>
<head>
    <title>Create</title>
</head>
<body>

<textarea id="code_snippet"></textarea>
<button id="send_snippet" type="button" onclick="send()">Submit</button>

<script>
    function send() {
        let code = document.getElementById("code_snippet").value;

        let xhr = new XMLHttpRequest();
        xhr.open("POST", "/api/code/new");
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(JSON.stringify({"code": code}));
    }
</script>

</body>
</html>