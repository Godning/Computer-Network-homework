#!/usr/bin/env python
'''
@author: Godning
 date:2015,3
'''
from os import curdir,sep
from BaseHTTPServer import BaseHTTPRequestHandler,HTTPServer

class MyHandler(BaseHTTPRequestHandler):
    
    def do_GET(self):
        try:
            f=open(curdir+sep+self.path)
            self.send_response(200)
            self.send_header('Content-type','text/html')
            self.end_headers()
            self.wfile.write(f.read())
            f.close()
        except IOError:
            self.send_error(404,'File Not Found: %s' % self.path)
            
            
def main():
    try:
        server=HTTPServer(('',80),MyHandler)
        print 'Welcome to the machine'
        server.serve_forever() 
    except KeyboardInterrupt:
        print 'server Shut down'
        server.socket.close()
        
if __name__=='__main__':
    main()
        
            
        