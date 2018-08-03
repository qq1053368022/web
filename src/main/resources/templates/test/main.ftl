<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        .topClass{
            float: top;
        }
        .leftClass{
            float: left;
        }
        .rightClass{
            float: right;
        }
    </style>
</head>
<body>
<div class="topClass">
    <#include "top.ftl">
</div>
<div class="bottomClass">
    <div class="leftClass">
        <#include "left.ftl">
    </div>
    <div class="rightClass">
        <#include "right.ftl">
    </div>
</div>

</body>
</html>