#
# MuleSoft Examples
# Copyright 2014 MuleSoft, Inc.
#
# This product includes software developed at
# MuleSoft, Inc. (http://www.mulesoft.com/).
#

require 'webrick'
include WEBrick

s = HTTPServer.new(
  :Port => 2010, 
  :DocumentRoot => Dir::pwd
)
s.mount('/sh/scripts', WEBrick::HTTPServlet::FileHandler, '../scripts')
s.mount('/sh/styles', WEBrick::HTTPServlet::FileHandler, '../styles')
trap('INT') { s.stop }
s.start
