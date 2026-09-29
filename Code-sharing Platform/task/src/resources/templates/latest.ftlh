<!DOCTYPE html>
<html>
<head>
    <title>Latest</title>

    <link rel="stylesheet"
          href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">

    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>

<body>

<#list snippets as snippet>
    <span class="load_date">${snippet.date}</span>
    <pre class="code_snippet"><code>${snippet.code}</code></pre>
</#list>

</body>
</html>