/**
 * Created by Godning on 2015/3/25.
 */
//WebSvr.js
// 一个演示Web服务器
//------------------------------------------------
/**
 * Created by Godning on 2015/3/25.
 */
//WebSvr.js
// 一个演示Web服务器
//------------------------------------------------
var http=require("http");
url=require("url");
path=require("path");
fs=require("fs");

http.createServer(function(req,res){
    var pathname=__dirname+url.parse(req.url).pathname;
    if(path.extname(pathname)=="")
    {
        pathname+="/";
    }
    if(pathname.charAt(pathname.length-1)=="/")
    {
        pathname+="index.html";
    }
    fs.exists(pathname,function(exists){
        if(exists) {
            switch (path.extname(pathname)) {
                case ".html":
                    res.writeHead(200, {"Content-Type": "text/html"});
                    break;
                default :
                    res.writeHead(200, {"Content-Type": "application/octet-stream"})
            }
            fs.readFile(pathname, function (err, data) {
                res.end(data);
            });
        }
        else{
            res.writeHead(404,{"Content-Type":"text/html"});
            res.end("<h1>404 Note Found</h1>");
        }
    });
}).listen(8080,"127.0.0.1");
console.log("Server running at http://127.0.0.1:8080/");
